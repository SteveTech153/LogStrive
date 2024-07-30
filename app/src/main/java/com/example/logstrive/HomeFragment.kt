package com.example.logstrive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logstrive.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

class HomeFragment : Fragment(), AddHabitRecordDialog.AddHabitRecordListener {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application, (requireActivity().application as MyApp).habitRepository)
    }

    private var habits: List<Habit> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = SessionManager.getId(requireContext())

        binding.tvGreeting.text = "Hi ${SessionManager.getUsername(requireContext())}"

        homeViewModel.quote.observe(viewLifecycleOwner) { quote ->
            binding.tvQuote.text = quote.q
            binding.tvAuthor.text = "- ${quote.a}"
        }

        binding.homeFab.setOnClickListener {
            AddHabitRecordDialog(this).show(parentFragmentManager, "AddHabitRecordDialog")
        }

        habitViewModel.allHabits.observe(viewLifecycleOwner) { habitsList ->
            habits = habitsList
            updateHabitLogs(userId)
            updateYesterdayHabitLogs(userId)
        }

        // Initialize RecyclerViews
        binding.todaysRecordRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.yesterdaysRecordRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun updateHabitLogs(userId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val logs = habitViewModel.getHabitLogsForUserOn(userId, convertDateToLong(Date())).await()
                val habitMap = habits.associateBy { it.habitId }

                val habitCardItems = logs?.mapNotNull { habitLog ->
                    val habit = habitMap[habitLog.habitId]
                    if (habit != null) {
                        HabitCardItem(
                            name = habit.habitName,
                            duration = minutesToTimeString(habitLog.duration),
                            imageName = "habit_icon_${habit.categoryId}"
                        )
                    } else {
                        null
                    }
                }

                if (!habitCardItems.isNullOrEmpty()) {
                    val habitCardAdapter = HabitCardAdapter(habitCardItems)
                    binding.todaysRecordRecyclerView.adapter = habitCardAdapter
                } else {
                    binding.todaysRecordRecyclerView.adapter = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateYesterdayHabitLogs(userId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val logs = habitViewModel.getHabitLogsForUserOn(userId, convertDateToLong(getYesterdayDate())).await()
                val habitMap = habits.associateBy { it.habitId }

                val habitCardItems = logs?.mapNotNull { habitLog ->
                    val habit = habitMap[habitLog.habitId]
                    if (habit != null) {
                        HabitCardItem(
                            name = habit.habitName,
                            duration = minutesToTimeString(habitLog.duration),
                            imageName = "habit_icon_${habit.categoryId}"
                        )
                    } else {
                        null
                    }
                }

                if (!habitCardItems.isNullOrEmpty()) {
                    val habitCardAdapter = HabitCardAdapter(habitCardItems)
                    binding.yesterdaysRecordRecyclerView.adapter = habitCardAdapter
                } else {
                    binding.yesterdaysRecordRecyclerView.adapter = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onHabitLogAdded(habitLog: HabitLog) {
        viewLifecycleOwner.lifecycleScope.launch {
            habitViewModel.insertHabitLog(habitLog)
            // Call updateHabitLogs immediately after inserting the log
            updateHabitLogs(SessionManager.getId(requireContext()))
            updateYesterdayHabitLogs(SessionManager.getId(requireContext()))
        }
    }

    private fun minutesToTimeString(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return String.format("%02d:%02d:00", hours, remainingMinutes)
    }

    fun convertDateToLong(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getYesterdayDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }

    suspend fun <T> LiveData<T>.await(): T? = suspendCancellableCoroutine { cont ->
        val observer = object : Observer<T> {
            override fun onChanged(t: T) {
                if (t != null) {
                    cont.resume(t)
                    this@await.removeObserver(this)
                }
            }
        }
        this.observeForever(observer)
        cont.invokeOnCancellation {
            this.removeObserver(observer)
        }
    }
}
