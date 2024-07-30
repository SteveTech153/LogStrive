package com.example.logstrive

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.logstrive.databinding.DialogAddHabitBinding
import com.example.logstrive.databinding.DialogAddHabitRecordBinding
import com.example.logstrive.databinding.DialogEditHabitBinding
import java.util.Calendar
import java.util.Date

class AddHabitRecordDialog(
    private val listener: AddHabitRecordListener
) : DialogFragment() {

    interface AddHabitRecordListener {
        fun onHabitLogAdded(habitLog: HabitLog)
    }

    private lateinit var binding: DialogAddHabitRecordBinding

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application, (requireActivity().application as MyApp).habitRepository)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddHabitRecordBinding.inflate(layoutInflater, null, false)

        val builder = AlertDialog.Builder(requireContext())

        habitViewModel.allHabits.observe(this) { habits ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                habits
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.editHabitSpinner.adapter = adapter
        }

        binding.timepicker.setIs24HourView(true)

        binding.addButton.setOnClickListener {
            val habit = binding.editHabitSpinner.selectedItem as Habit
            val hour = binding.timepicker.hour
            val minutes = binding.timepicker.minute
            if (habit.habitName.isNotBlank() && (hour != 0 || minutes != 0)) {
                listener.onHabitLogAdded(
                    HabitLog(
                        userId = SessionManager.getId(requireContext()),
                        habitId = habit.habitId,
                        date = convertDateToLong(Date()),
                        duration = hour * 60 + minutes
                    )
                )
                dismiss()
            } else {
                //
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        builder.setView(binding.root)
        return builder.create()
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
    } //temp
}
