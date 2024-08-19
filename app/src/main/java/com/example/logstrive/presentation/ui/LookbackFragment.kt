package com.example.logstrive.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logstrive.MyApp
import com.example.logstrive.R
import com.example.logstrive.data.entity.DailyLog
import com.example.logstrive.data.entity.Emoji
import com.example.logstrive.data.entity.HabitCardItem
import com.example.logstrive.data.entity.HabitLog
import com.example.logstrive.databinding.FragmentLookbackBinding
import com.example.logstrive.presentation.adapter.YesterdayHabitCardAdapter
import com.example.logstrive.presentation.factory.ViewModelFactory
import com.example.logstrive.presentation.viewModel.HabitViewModel
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory
import com.example.logstrive.presentation.viewModel.UserViewModel
import com.example.logstrive.util.Helper
import com.example.logstrive.util.Helper.convertDateToLong
import com.example.logstrive.util.Helper.minutesToTimeString
import com.example.logstrive.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class LookbackFragment : Fragment() {
    private lateinit var binding: FragmentLookbackBinding
    private var selectedDate: Long ? = null
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory((requireActivity().application as MyApp).userRepository)
    }
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(
            requireActivity().application,
            (requireActivity().application as MyApp).habitRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(selectedDate==null){
            selectedDate = convertDateToLong(Date())
        }else{
            selectedDate?.let { updateOnDate(selectedDate!!) }
        }
        binding.calendarView.date = selectedDate!!
        lifecycleScope.launch {
            binding.calendarView.minDate =
                userViewModel.getAccountCreatedDate(SessionManager.getId(requireContext()))
        }
        binding.calendarView.maxDate = convertDateToLong(Date())

        binding.rvHabitRecord.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            selectedDate = calendar.timeInMillis
            updateOnDate( selectedDate!!)
        }
        updateOnDate( selectedDate!!)

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedDate?.let { outState.putLong("selectedDate", selectedDate!!) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getLong("selectedDate")?.let {
            selectedDate = it
            binding.calendarView.setDate(selectedDate!!)
            println("onViewStateRestored being called !!!")
            updateOnDate(selectedDate!!)
        }
    }

    private fun updateOnDate(dateLong: Long) {
        lifecycleScope.launch {
            val userId = SessionManager.getId(requireContext())

            val logDeferred = async(Dispatchers.IO) {
                habitViewModel.getDailyLog(userId, dateLong)
            }
            val logsDeferred = async(Dispatchers.IO) {
                habitViewModel.getHabitLogsForUserOn(userId, dateLong)
            }

            val log = logDeferred.await()
            val logs = logsDeferred.await()
            var habitCards: List<HabitCardItem>? = null

            habitViewModel.allHabits.observe(viewLifecycleOwner) { habits ->
                    if (habits != null) {
                        val habitMap = habits.associateBy { it.habitId }
                        habitCards = logs?.mapNotNull { habitLog ->
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
                        updateUI(log, habitCards)
                    }else{
                        updateUI(log, habitCards)
                    }
            }
        }
    }
    private fun updateUI(dailyLog: DailyLog?, habitCards: List<HabitCardItem>?){
        lifecycleScope.launch {
        withContext(Dispatchers.Main) {

            if (dailyLog != null || !habitCards.isNullOrEmpty()) {
                binding.tvDateSummary.visibility = View.VISIBLE
                binding.tvSummary.visibility = View.VISIBLE
                binding.tvOverallmood.visibility = View.VISIBLE

                binding.tvDateSummary.text = "${Helper.formatDate(Date())} ${getString(R.string.summary)}"
                binding.tvSummary.text = dailyLog?.summary ?: ""
                binding.tvEmoji.visibility = View.GONE
                dailyLog?.overallMood?.let {
                    binding.tvEmoji.visibility = View.VISIBLE
                    binding.tvEmoji.text = Emoji.entries[dailyLog.overallMood].emojiString
                }
                binding.rvHabitRecord.visibility = View.VISIBLE
                binding.rvHabitRecord.adapter = YesterdayHabitCardAdapter(habitCards ?: emptyList())
                binding.rvHabitRecord.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            } else {
                binding.tvDateSummary.visibility = View.GONE
                binding.tvSummary.visibility = View.GONE
                binding.tvOverallmood.visibility = View.GONE
                binding.tvEmoji.visibility = View.GONE
                binding.rvHabitRecord.visibility = View.GONE
                Snackbar.make(binding.root, getString(R.string.no_entries), Snackbar.LENGTH_SHORT).setAnchorView(binding.invisibleBtn).show()
            }
        }

        }
    }


}