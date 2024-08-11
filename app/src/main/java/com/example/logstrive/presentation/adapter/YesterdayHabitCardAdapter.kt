package com.example.logstrive.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logstrive.data.entity.HabitCardItem
import com.example.logstrive.databinding.HabitCardWithTimeLayoutBinding
import com.example.logstrive.databinding.YesterdayHabitCardWithTimeLayoutBinding

class YesterdayHabitCardAdapter(private var habitCardItems: List<HabitCardItem>) : RecyclerView.Adapter<YesterdayHabitCardAdapter.YesterdayHabitCardViewHolder>() {

    fun setList(list: List<HabitCardItem>) {
        this.habitCardItems = list
        notifyDataSetChanged()
    }

    class YesterdayHabitCardViewHolder(val binding: YesterdayHabitCardWithTimeLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YesterdayHabitCardViewHolder {
        val binding = YesterdayHabitCardWithTimeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YesterdayHabitCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YesterdayHabitCardViewHolder, position: Int) {
        val habitCardItem = habitCardItems[position]
        holder.binding.habitCardTitle.text = habitCardItem.name
        holder.binding.habitTime.text = habitCardItem.duration
        holder.binding.habitCardImage.setImageResource(
            holder.itemView.context.resources.getIdentifier( //TODO
                habitCardItem.imageName, "drawable", holder.itemView.context.packageName,
            )
        )
    }

    override fun getItemCount(): Int {
        return habitCardItems.size
    }
}
