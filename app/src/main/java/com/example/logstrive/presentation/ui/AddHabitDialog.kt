package com.example.logstrive.presentation.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.logstrive.MyApp
import com.example.logstrive.R
import com.example.logstrive.data.entity.Category
import com.example.logstrive.databinding.DialogAddHabitBinding
import com.example.logstrive.presentation.viewModel.HabitViewModel
import com.example.logstrive.presentation.viewModel.HabitViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable

class AddHabitDialog : DialogFragment() {

    private lateinit var binding: DialogAddHabitBinding
    private var habitNameEntered: String? = null
    private var habitCategoryPositionSelected: Int? = null
    private var listener: AddHabitListener? = null

    interface AddHabitListener {
        fun onHabitAdded(habitName: String, categoryId: Int, callback: (Boolean) -> Unit)
    }

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory(requireActivity().application, (requireActivity().application as MyApp).habitRepository)
    }

    companion object {
        fun newInstance(listener: AddHabitListener): AddHabitDialog {
            val dialog = AddHabitDialog()
            dialog.listener = listener
            return dialog
        }
    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        listener = parentFragment as? AddHabitListener
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        listener = parentFragment as? AddHabitListener
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = requireParentFragment() as? AddHabitListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        listener = requireParentFragment() as? AddHabitListener
        binding = DialogAddHabitBinding.inflate(LayoutInflater.from(context), null, false)
        val builder = AlertDialog.Builder(requireContext())

        // Restore state
        habitNameEntered = savedInstanceState?.getString("habitName")
        habitCategoryPositionSelected = savedInstanceState?.getInt("habitCategoryPositionSelected")

        binding.etvHabitName.setText(habitNameEntered)
        binding.habitCategorySpinner.post {
            habitCategoryPositionSelected?.let { position ->
                binding.habitCategorySpinner.setSelection(position)
            }
        }


        val habitNameEditText = binding.etvHabitName
        val categorySpinner = binding.habitCategorySpinner

        setupCategorySpinner(categorySpinner)

        binding.addBtn.setOnClickListener {
            val habitName = habitNameEditText.text.toString()
            var selectedCategory: Category? = null
            categorySpinner.selectedItem?.let { selectedCategory = categorySpinner.selectedItem as Category }
            if (habitName.isNotEmpty() && selectedCategory != null) {
                listener!!.onHabitAdded(habitName, selectedCategory!!.categoryId) { success ->
                    if (success) {
                        dismiss()
                    } else {
                        binding.tvHabitErrorMsg.text = getString(R.string.habit_unique_error_msg)
                        binding.tvHabitErrorMsg.visibility = View.VISIBLE
                    }
                }
            } else {
                binding.tvHabitErrorMsg.text = getString(R.string.habit_error_msg)
                binding.tvHabitErrorMsg.visibility = View.VISIBLE
            }
        }
        binding.canceBtn.setOnClickListener {
            dialog?.cancel()
        }

        builder.setView(binding.root)
        return builder.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        habitNameEntered = binding.etvHabitName.text.toString()
        habitCategoryPositionSelected = binding.habitCategorySpinner.selectedItemPosition
        outState.putString("habitName", habitNameEntered)
        outState.putInt("habitCategoryPositionSelected", habitCategoryPositionSelected!!)
        println("onSaveInstance spinner. position : $habitCategoryPositionSelected")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.etvHabitName.setText(savedInstanceState?.getString("habitName"))
        savedInstanceState?.getInt("habitCategoryPositionSelected")?.let { position ->
            binding.habitCategorySpinner.post {
                binding.habitCategorySpinner.setSelection(position)
                println("setting spinner. position : $position")
            }
        }
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
            habitCategoryPositionSelected?.let { position ->
                spinner.setSelection(position)
            }
        }
    }
}

