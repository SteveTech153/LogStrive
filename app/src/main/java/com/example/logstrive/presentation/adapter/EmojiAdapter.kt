package com.example.logstrive.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.logstrive.R
import com.example.logstrive.data.entity.Emoji
import kotlin.enums.EnumEntries

class EmojiAdapter(private val emojis: EnumEntries<Emoji>, private val onEmojiClick: (Int) -> Unit) :
    RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

        var selectedPosition = 3 //smile emoji

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emoji, parent, false)
        return EmojiViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.bind(emojis[position], position == selectedPosition)
    }

    override fun getItemCount() = emojis.size

    inner class EmojiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val emojiTextView: TextView = itemView.findViewById(R.id.emoji)

        init {
            itemView.setOnClickListener { //changed here
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onEmojiClick(bindingAdapterPosition)
            }
        }

        fun bind(emoji: Emoji, isSelected: Boolean) {
            emojiTextView.text = emoji.emojiString
            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.TRANSPARENT)
        }
    }
}