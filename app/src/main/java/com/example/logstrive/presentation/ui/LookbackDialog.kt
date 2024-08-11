package com.example.logstrive.presentation.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logstrive.data.entity.DailyLog
import com.example.logstrive.data.entity.Emoji
import com.example.logstrive.data.entity.HabitCardItem
import com.example.logstrive.databinding.DialogLookbackBinding
import com.example.logstrive.presentation.adapter.YesterdayHabitCardAdapter
import com.example.logstrive.util.Helper
import kotlinx.coroutines.launch
import java.util.Date

class LookbackDialog(
    val date: Date,
    val log: DailyLog?,
    val habitCards: List<HabitCardItem>?,
): DialogFragment() {
    private lateinit var binding: DialogLookbackBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        binding = DialogLookbackBinding.inflate(layoutInflater)

        binding.tvDateSummary.text = Helper.formatDate(date)

        lifecycleScope.launch {
            binding.tvSummary.text = log?.summary.toString()
            log?.overallMood?.let {  binding.tvEmoji.text = Emoji.entries[log.overallMood].emojiString  }
        }

        binding.rvHabitRecord.adapter = habitCards?.let { YesterdayHabitCardAdapter(it) }
        binding.rvHabitRecord.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        builder.setView(binding.root)
        return builder.create()
    }
}