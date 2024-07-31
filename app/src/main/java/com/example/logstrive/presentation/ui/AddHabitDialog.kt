package com.example.logstrive.presentation.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.logstrive.MyApp
import com.example.logstrive.data.entity.Category
import com.example.logstrive.databinding.DialogAddHabitBinding
import com.example.logstrive.presentation.viewModel.HabitViewModel
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory
import com.google.android.material.snackbar.Snackbar

class AddHabitDialog(private val listener: AddHabitListener) : DialogFragment() {

    private lateinit var binding: DialogAddHabitBinding

    interface AddHabitListener {
        fun onHabitAdded(habitName: String, categoryId: Int)
    }

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application, (requireActivity().application as MyApp).habitRepository)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddHabitBinding.inflate(layoutInflater, null, false)
//        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
        val builder = AlertDialog.Builder(requireContext())
        val habitNameEditText = binding.habitNameEditText
        val categorySpinner = binding.habitCategorySpinner

        setupCategorySpinner(categorySpinner)

        builder.setView(binding.root)

        binding.addBtn.setOnClickListener{
            val habitName = habitNameEditText.text.toString()
            var selectedCategory: Category? = null
            categorySpinner.selectedItem?.let { selectedCategory = categorySpinner.selectedItem as Category }
            if (habitName.isNotEmpty() && selectedCategory != null) {
                listener.onHabitAdded(habitName, selectedCategory!!.categoryId)
            } else {
                binding.tvHabitErrorMsg.visibility = View.VISIBLE
            }
        }
        binding.canceBtn.setOnClickListener{
            dialog?.cancel()
        }


        val dialog = builder.create()
        return dialog
    }

    private fun setupCategorySpinner(spinner: Spinner) {
        habitViewModel.getAllCategories().observe(this) { categories ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}
