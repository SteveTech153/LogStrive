package com.example.logstrive.presentation.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.logstrive.MyApp
import com.example.logstrive.R
import com.example.logstrive.data.entity.Category
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.databinding.DialogAddHabitBinding
import com.example.logstrive.databinding.DialogEditHabitBinding
import com.example.logstrive.presentation.ui.AddHabitDialog.AddHabitListener
import com.example.logstrive.presentation.viewModel.HabitViewModel
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory

class EditHabitDialog() : DialogFragment() {
    private var habit: Habit? = null
    private var listener: EditHabitListener? = null
    private var habitNameEntered: String? = null
    private var habitCategoryPositionSelected: Int? = null

    interface EditHabitListener {
        fun onHabitEdited(habit: Habit)
        fun onHabitDeleted(habit: Habit)
    }

    private lateinit var binding: DialogEditHabitBinding
    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application, (requireActivity().application as MyApp).habitRepository)
    }

    companion object{
        fun newInstance(habit: Habit, listener: EditHabitListener): EditHabitDialog{
            val dialog = EditHabitDialog()
            dialog.habit = habit
            dialog.listener = listener
            return dialog
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? EditHabitListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditHabitBinding.inflate(LayoutInflater.from(context), null, false)
        val builder = AlertDialog.Builder(requireContext())

        // Restore state
        habitNameEntered = savedInstanceState?.getString("habitName")
        habitCategoryPositionSelected = savedInstanceState?.getInt("habitCategoryPositionSelected")

        binding.editHabitName.setText(habitNameEntered)
        binding.editHabitCategorySpinner.post {
            habitCategoryPositionSelected?.let { position ->
                binding.editHabitCategorySpinner.setSelection(position)
            }
        }


        binding.editHabitName.setText(habit?.habitName)
        lateinit var categoriesList: List<Category?>
        habitViewModel.getAllCategories().observe(this) { categories ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories.map { it.categoryName }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.editHabitCategorySpinner.adapter = adapter
            categoriesList = categories
            habitCategoryPositionSelected?.let { position ->
                binding.editHabitCategorySpinner.setSelection(position)
            }

            val categoryPosition = categories.indexOfFirst { it.categoryId == habit?.categoryId }
            if (categoryPosition != -1) {
                binding.editHabitCategorySpinner.setSelection(categoryPosition)
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.deleteButton.setOnClickListener {
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogTheme)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes") { dialog, _ ->
                    habit?.let { listener?.onHabitDeleted(habit!!) }
                    dialog.dismiss()
                    super.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.editButton.setOnClickListener {
            val newHabitName = binding.editHabitName.text.toString()
            val selectedCategoryName = binding.editHabitCategorySpinner.selectedItem as String
            val selectedCategoryId = categoriesList.firstOrNull { it?.categoryName == selectedCategoryName }?.categoryId
            println("${getString(R.string.selected_category_id)} ${selectedCategoryId}, ${getString(R.string.category_name)} ${selectedCategoryName}")
            println(categoriesList)
            if (newHabitName.isNotEmpty() && selectedCategoryId != null) {
                val updatedHabit = habit?.copy(habitName = newHabitName, categoryId = selectedCategoryId)
                updatedHabit?.let {  listener?.onHabitEdited(updatedHabit) }
                dismiss()
            } else {
                binding.tvHabitnameErrorMsg.visibility = View.VISIBLE
            }
        }

        builder.setView(binding.root)
        return builder.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        habitNameEntered = binding.editHabitName.text.toString()
        habitCategoryPositionSelected = binding.editHabitCategorySpinner.selectedItemPosition
        outState.putString("habitName", habitNameEntered)
        outState.putInt("habitCategoryPositionSelected", habitCategoryPositionSelected!!)
        habitCategoryPositionSelected = binding.editHabitCategorySpinner.selectedItemPosition
        outState.putString("habitName", habitNameEntered)
        outState.putInt("habitCategoryPositionSelected", habitCategoryPositionSelected!!)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {  /// L
        super.onViewStateRestored(savedInstanceState)
        binding.editHabitName.setText(savedInstanceState?.getString("habitName"))
        savedInstanceState?.getInt("habitCategoryPositionSelected")?.let { position ->
            binding.editHabitCategorySpinner.post {
                binding.editHabitCategorySpinner.setSelection(position)
                println("setting spinner. position : $position")
            }
        }
    }
}
