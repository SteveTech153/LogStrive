package com.example.logstrive.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logstrive.data.entity.HabitCardItem
import com.example.logstrive.databinding.HabitCardWithTimeLayoutBinding
import com.example.logstrive.databinding.YesterdayHabitCardWithTimeLayoutBinding

class HabitCardAdapter(private var habitCardItems: List<HabitCardItem>) : RecyclerView.Adapter<HabitCardAdapter.HabtiCardViewHolder>() {

    fun setList(list: List<HabitCardItem>) {
        this.habitCardItems = list
        notifyDataSetChanged()
    }

    class HabtiCardViewHolder(val binding: HabitCardWithTimeLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabtiCardViewHolder {
        val binding = HabitCardWithTimeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabtiCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabtiCardViewHolder, position: Int) {
        val habitCardItem = habitCardItems[position]
        holder.binding.habitCardTitle.text = habitCardItem.name
        holder.binding.habitTime.text = habitCardItem.duration
        holder.binding.habitCardImage.setImageResource(
            holder.itemView.context.resources.getIdentifier(
                habitCardItem.imageName, "drawable", holder.itemView.context.packageName
            )
        )
    }

    override fun getItemCount(): Int {
        println("cards count : ${habitCardItems.size}")
        return habitCardItems.size
    }
}
