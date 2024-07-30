package com.example.logstrive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logstrive.databinding.HabitCardWithTimeLayoutBinding

class HabitCardAdapter(private var habitCardItems: List<HabitCardItem>) : RecyclerView.Adapter<HabitCardAdapter.HabtiCardViewHolder>() {

    // Update the list and notify changes
    fun setList(list: List<HabitCardItem>) {
        this.habitCardItems = list
        notifyDataSetChanged()
    }

    // ViewHolder class to hold the view bindings
    class HabtiCardViewHolder(val binding: HabitCardWithTimeLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    // Create ViewHolder and inflate the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabtiCardViewHolder {
        val binding = HabitCardWithTimeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabtiCardViewHolder(binding)
    }

    // Bind data to the ViewHolder
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

    // Return the size of the list
    override fun getItemCount(): Int {
        println("cards count : ${habitCardItems.size}")
        return habitCardItems.size
    }
}
