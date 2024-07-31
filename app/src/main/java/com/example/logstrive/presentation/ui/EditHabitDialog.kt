package com.example.logstrive.presentation.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.logstrive.MyApp
import com.example.logstrive.data.entity.Category
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.databinding.DialogEditHabitBinding
import com.example.logstrive.presentation.viewModel.HabitViewModel
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory

class EditHabitDialog(
    private val habit: Habit,
    private val listener: EditHabitListener,
) : DialogFragment() {

    interface EditHabitListener {
        fun onHabitEdited(habit: Habit)
        fun onHabitDeleted(habit: Habit)
    }

    private lateinit var binding: DialogEditHabitBinding

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application, (requireActivity().application as MyApp).habitRepository)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditHabitBinding.inflate(layoutInflater, null, false)

        val builder = AlertDialog.Builder(requireContext())

        // Pre-fill the habit details
        binding.editHabitName.setText(habit.habitName)
        lateinit var categoriesList: List<Category?>
        // Populate the spinner with categories
        habitViewModel.getAllCategories().observe(this) { categories ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories.map { it.categoryName } // Assuming Category has a categoryName field
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.editHabitCategorySpinner.adapter = adapter
            categoriesList = categories
            // Set the spinner to the current category of the habit
            val categoryPosition = categories.indexOfFirst { it.categoryId == habit.categoryId }
            if (categoryPosition != -1) {
                binding.editHabitCategorySpinner.setSelection(categoryPosition)
            }
        }

        // Handle button clicks
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.deleteButton.setOnClickListener {
            listener.onHabitDeleted(habit)
            dismiss()
        }

        binding.editButton.setOnClickListener {
            val newHabitName = binding.editHabitName.text.toString()
            val selectedCategoryName = binding.editHabitCategorySpinner.selectedItem as String
            val selectedCategoryId = categoriesList.firstOrNull { it?.categoryName == selectedCategoryName }?.categoryId
            println("selected category id : ${selectedCategoryId}, category name : ${selectedCategoryName}")
            println(categoriesList)
            if (newHabitName.isNotEmpty() && selectedCategoryId != null) {
                val updatedHabit = habit.copy(habitName = newHabitName, categoryId = selectedCategoryId)
                listener.onHabitEdited(updatedHabit)
                dismiss()
            } else {
                println("Error: Habit name cannot be empty or category not selected")
            }
        }

        builder.setView(binding.root)
        return builder.create()
    }
}
