package com.example.logstrive.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.logstrive.presentation.adapter.HabitAdapter
import com.example.logstrive.presentation.viewModel.HabitViewModel
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory
import com.example.logstrive.MyApp
import com.example.logstrive.util.SessionManager
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.databinding.FragmentHabitBinding
import kotlinx.coroutines.launch

class HabitFragment : Fragment(), AddHabitDialog.AddHabitListener,
    EditHabitDialog.EditHabitListener, HabitAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHabitBinding
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application ,(requireActivity().application as MyApp).habitRepository)
    }
    private lateinit var habitAdapter: HabitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitAdapter = HabitAdapter( this)
        binding.habitsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = habitAdapter
        }

        binding.addHabitFab.setOnClickListener {
            AddHabitDialog(this).show(parentFragmentManager, "AddHabitDialog")
        }

        habitViewModel.allHabits.observe(viewLifecycleOwner) { habits ->
            habits?.let { habitAdapter.setHabits(it) }
        }
    }

    override fun onHabitAdded(habitName: String, categoryId: Int) {
        val userId = SessionManager.getId(requireContext())
        if (userId != -1) {
            val habit = Habit(habitName = habitName, categoryId = categoryId, userId = userId)
            lifecycleScope.launch {
                habitViewModel.addHabit(habit)
            }
        }
    }

    override fun onHabitEdited(habit: Habit){
        lifecycleScope.launch {
            habitViewModel.updateHabit(habit)
        }
    }

    override fun onHabitDeleted(habit: Habit) {
        lifecycleScope.launch {
            habitViewModel.deleteHabit(habit)
        }
    }

    override fun onItemClick(habit: Habit) {
        EditHabitDialog(habit, this).show(parentFragmentManager, "EditHabitDialog")
    }
}
