package com.example.logstrive.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logstrive.data.entity.HabitCardItem
import com.example.logstrive.MyApp
import com.example.logstrive.R
import com.example.logstrive.data.entity.DailyLog
import com.example.logstrive.util.SessionManager
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.data.entity.HabitLog
import com.example.logstrive.databinding.FragmentHomeBinding
import com.example.logstrive.presentation.adapter.HabitCardAdapter
import com.example.logstrive.presentation.adapter.YesterdayHabitCardAdapter
import com.example.logstrive.presentation.viewModel.HabitViewModel
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory
import com.example.logstrive.presentation.viewModel.HomeViewModel
import com.example.logstrive.util.Helper
import com.example.logstrive.util.Helper.minutesToTimeString
import com.example.logstrive.util.NavigationConstants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Time
import java.util.*

class HomeFragment : Fragment(), AddHabitRecordDialog.AddHabitRecordListener, AddSummaryDialog.AddSummaryListener
{
    private val thisKeyword = this
    private lateinit var binding: FragmentHomeBinding
    private var isFabMenuOpen = false
    private var habitRecordDialog: AddHabitRecordDialog? = null
    private var summaryDialog: AddSummaryDialog? = null
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application, (requireActivity().application as MyApp).habitRepository)
    }

    private var habits: List<Habit> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = SessionManager.getId(requireContext())

        binding.tvGreeting.text = "${getString(R.string.hi)} ${SessionManager.getUsername(requireContext())}"

        binding.cvQuote.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkpurple_half_transparent))
        homeViewModel.quote.observe(viewLifecycleOwner) { quote ->
            binding.tvQuote.text = quote.quote
            binding.tvAuthor.text = "- ${quote.author}"
            binding.cvQuote.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkpurple))
        }

        binding.homeFab.setOnClickListener {
            mainFabClicked()
        }
        binding.fabHabitAdd.setOnClickListener{
            habitRecordDialog = AddHabitRecordDialog.newInstance(thisKeyword)
            habitRecordDialog!!.show(
                childFragmentManager,
                "AddHabitRecordDialog"
            )
        }

        binding.fabSummaryAdd.setOnClickListener{
            summaryDialog = AddSummaryDialog.newInstance(thisKeyword)
            summaryDialog!!.show(
                childFragmentManager,
                "AddSummaryDialog"
            )
        }

        habitViewModel.allHabits.observe(viewLifecycleOwner) { habitsList ->
            habits = habitsList
            lifecycleScope.launch {
                updateHabitLogs()
                updateYesterdayHabitLogs(userId)
            }
        }


        binding.todaysRecordRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.yesterdaysRecordRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun updateHabitLogs() {
        habitViewModel.todaysHabitLogsOfUser.observe(viewLifecycleOwner){ logs ->
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
                binding.tvAddRecordsToGetStarted.visibility = View.INVISIBLE
                binding.tvTodaysRecord.visibility = View.VISIBLE
                val habitCardAdapter = YesterdayHabitCardAdapter(habitCardItems)
                binding.todaysRecordRecyclerView.adapter = habitCardAdapter
            } else {
                binding.todaysRecordRecyclerView.adapter = null
                binding.tvAddRecordsToGetStarted.visibility = View.VISIBLE
            }
        }

    }

    private fun updateYesterdayHabitLogs(userId: Int) {
        lifecycleScope.launch { //changed here
            val logs = habitViewModel.getHabitLogsForUserOn(
                userId,
                Helper.convertDateToLong(Helper.getYesterdayDate())
            )
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
            withContext(Dispatchers.Main) {
                if (!habitCardItems.isNullOrEmpty()) {
                    binding.tvYesterdaysRecord.visibility = View.VISIBLE
                    val habitCardAdapter = HabitCardAdapter(habitCardItems)
                    binding.yesterdaysRecordRecyclerView.adapter = habitCardAdapter
                } else {
                    binding.yesterdaysRecordRecyclerView.adapter = null
                }
            }
        }
    }

    override fun onHabitLogAdded(habitLog: HabitLog) {
        lifecycleScope.launch {
            habitViewModel.insertHabitLog(habitLog)
            updateHabitLogs()
            updateYesterdayHabitLogs(SessionManager.getId(requireContext()))
        }
    }

    private fun openFabMenu(vararg views: View) {
        views.forEach { view ->
            view.visibility = View.VISIBLE
            view.animate().translationY(0f).alpha(1f).setDuration(300).start()
        }
    }

    private fun closeFabMenu(vararg views: View) {
        views.forEach { view ->
            view.animate().translationY(view.height.toFloat()).alpha(0f).setDuration(300).withEndAction {
                view.visibility = View.GONE
            }.start()
        }
    }
    private fun mainFabClicked(){
        habitViewModel.habitsCount.observe(viewLifecycleOwner){ habitsCount ->
            if (habitsCount == 0) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.please_add_habit),
                    Snackbar.LENGTH_LONG
                ).setTextMaxLines(5).setAnchorView(binding.homeFab).show()
                lifecycleScope.launch {
                    delay(1500)
                    (activity as? HomeActivity)?.navigateTo(NavigationConstants.HABITS)
                }
            }else {
                val fabOption1 = binding.fabHabitAdd
                val fabOption2 = binding.fabSummaryAdd
                val textOption1 = binding.tvAddHabit
                val textOption2 = binding.tvAddSummary
                if (isFabMenuOpen) {
                    closeFabMenu(fabOption1, textOption1, fabOption2, textOption2)
                } else {
                    lifecycleScope.launch {
                        if(habitViewModel.getDailyLog(SessionManager.getId(requireContext()), Helper.convertDateToLong(Date())) == null){
                            binding.fabSummaryAdd.visibility = View.GONE
                            openFabMenu(fabOption1, textOption1, fabOption2, textOption2)
                        }else {
                            openFabMenu(fabOption1, textOption1)
                        }
                    }
                }
                isFabMenuOpen = !isFabMenuOpen
            }
        }
    }

    override fun onSummaryAdd(dailyLog: DailyLog) {
        lifecycleScope.launch {
            if (habitViewModel.getDailyLog(SessionManager.getId(requireContext()), Helper.convertDateToLong(Date())) == null) {
                habitViewModel.insertDailyLog(dailyLog)
                Snackbar.make(requireView(), getString(R.string.summary_added), Snackbar.LENGTH_SHORT).setAnchorView(binding.homeFab).show()
            } else {
                Snackbar.make(requireView(), getString(R.string.summary_already_added), Snackbar.LENGTH_LONG).setAnchorView(binding.homeFab).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        habitRecordDialog?.isVisible?.let { outState.putBoolean("habitRecordDialogShown", it) }
        summaryDialog?.isVisible?.let { outState.putBoolean("summaryDialogShown", it) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState?.getBoolean("habitRecordDialogShown") == true){
            habitRecordDialog?.show(parentFragmentManager, "AddHabitRecordDialog")
        }
        else if(savedInstanceState?.getBoolean("summaryDialogShown") == true){
            summaryDialog?.show(parentFragmentManager, "AddSummaryDialog")
        }
    }
}