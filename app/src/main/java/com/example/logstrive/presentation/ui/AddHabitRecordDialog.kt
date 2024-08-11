package com.example.logstrive.presentation.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.logstrive.MyApp
import com.example.logstrive.util.SessionManager
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.data.entity.HabitLog
import com.example.logstrive.databinding.DialogAddHabitRecordBinding
import com.example.logstrive.presentation.viewModel.HabitViewModel
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory
import com.example.logstrive.util.Helper.convertDateToLong
import java.util.Calendar
import java.util.Date

class AddHabitRecordDialog: DialogFragment() {

    private var listener: AddHabitRecordListener? = null

    interface AddHabitRecordListener {
        fun onHabitLogAdded(habitLog: HabitLog)
    }

    companion object{
        fun newInstance(listener: AddHabitRecordListener): AddHabitRecordDialog{
            val dialog = AddHabitRecordDialog()
            dialog.listener = listener
            return dialog
        }
    }

    private lateinit var binding: DialogAddHabitRecordBinding

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application, (requireActivity().application as MyApp).habitRepository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = parentFragment as? AddHabitRecordListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddHabitRecordBinding.inflate(layoutInflater, null, false)
        val builder = AlertDialog.Builder(requireContext())
        binding.editHabitSpinner.post {
            binding.editHabitSpinner.setSelection( savedInstanceState?.getInt("habitPosition")?:0 )
        }
        binding.timepicker.setIs24HourView(true)
        binding.timepicker.hour = savedInstanceState?.getInt("selectedHour", 0) ?: 0
        binding.timepicker.minute = savedInstanceState?.getInt("selectedMinute", 10) ?: 10

        habitViewModel.allHabits.observe(this) { habits ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                habits
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.editHabitSpinner.adapter = adapter
        }

        binding.addButton.setOnClickListener {
            val habit = binding.editHabitSpinner.selectedItem as Habit
            val hour = binding.timepicker.hour
            val minutes = binding.timepicker.minute
            if (habit.habitName.isNotBlank() && (hour != 0 || minutes != 0)) {
                listener?.onHabitLogAdded(
                    HabitLog(
                        userId = SessionManager.getId(requireContext()),
                        habitId = habit.habitId,
                        date = convertDateToLong(Date()),
                        duration = hour * 60 + minutes,
                        timestamp = System.currentTimeMillis()
                    )
                )
                dismiss()
            } else {
                binding.tvDurationErrorMsg.visibility = View.VISIBLE
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        builder.setView(binding.root)
        return builder.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val selectedPosition = binding.editHabitSpinner.selectedItemPosition
        outState.putInt("habitPosition", selectedPosition)
        outState.putInt("selectedHour", binding.timepicker.hour)
        outState.putInt("selectedMinute", binding.timepicker.minute)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val habitPosition = savedInstanceState?.getInt("habitPosition") ?: 0
        binding.editHabitSpinner.post {
            binding.editHabitSpinner.setSelection(habitPosition)
        }
        binding.timepicker.hour = savedInstanceState?.getInt("selectedHour", 0) ?: 0
        binding.timepicker.minute = savedInstanceState?.getInt("selectedMinute", 10) ?: 10
    }

}
