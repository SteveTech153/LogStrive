package com.example.logstrive.presentation.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logstrive.R
import com.example.logstrive.data.entity.DailyLog
import com.example.logstrive.data.entity.Emoji
import com.example.logstrive.databinding.DialogAddSummaryBinding
import com.example.logstrive.presentation.adapter.EmojiAdapter
import com.example.logstrive.util.Helper
import com.example.logstrive.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import java.util.Date

class AddSummaryDialog : DialogFragment() {

    private var listener: AddSummaryListener? = null
    private var adapter: EmojiAdapter? = null
    private lateinit var binding: DialogAddSummaryBinding
    private var selectedPosition: Int = 3

    interface AddSummaryListener {
        fun onSummaryAdd(dailyLog: DailyLog)
    }

    companion object {
        fun newInstance(listener: AddSummaryListener): AddSummaryDialog {
            val dialog = AddSummaryDialog()
            dialog.listener = listener
            return dialog
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = parentFragment as? AddSummaryListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddSummaryBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())

        savedInstanceState?.getString("summary")?.let { binding.etvSummary.setText(it) }
        selectedPosition = savedInstanceState?.getInt("emojiSelectedPosition") ?: 3

        binding.rvEmojis.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = EmojiAdapter(Emoji.entries) { emojiPosition ->
            selectedPosition = emojiPosition
        }.apply {
            selectedPosition = this@AddSummaryDialog.selectedPosition
        }
        binding.rvEmojis.adapter = adapter

        val etvSummary = binding.etvSummary
        binding.addBtn.setOnClickListener {
            if (etvSummary.text?.isNotBlank() == true) {
                listener?.onSummaryAdd(
                    DailyLog(
                        userId = SessionManager.getId(requireContext()),
                        date = Helper.convertDateToLong(Date()),
                        summary = etvSummary.text.toString(),
                        overallMood = selectedPosition
                    )
                )
                dismiss()
            } else {
                etvSummary.hint = getString(R.string.please_enter_summary)
                etvSummary.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.error_color))
            }
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        builder.setView(binding.root)
        return builder.create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("summary", binding.etvSummary.text.toString())
        outState.putInt("emojiSelectedPosition", selectedPosition)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getString("summary")?.let {
            binding.etvSummary.setText(it)
        }
        savedInstanceState?.getInt("emojiSelectedPosition")?.let { restoredPosition ->
            selectedPosition = restoredPosition
            adapter?.selectedPosition = restoredPosition
            adapter?.notifyDataSetChanged()
        }
    }
}
