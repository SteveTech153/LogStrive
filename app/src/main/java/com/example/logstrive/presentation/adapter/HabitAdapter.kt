package com.example.logstrive.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.logstrive.R
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.databinding.ItemLayoutBinding

class HabitAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(habit: Habit)
    }

    private var habits: List<Habit> = emptyList()

    fun setHabits(habits: List<Habit>) {
        this.habits = habits
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    class HabitViewHolder(
        private val binding: ItemLayoutBinding,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(habit: Habit) {
            binding.habitName.text = habit.habitName
            val context = binding.root.context
            val drawableImg =
                when(habit.categoryId){
                    1 -> AppCompatResources.getDrawable(context, R.drawable.category_1)
                    2 -> AppCompatResources.getDrawable(context, R.drawable.category_2)
                    3 -> AppCompatResources.getDrawable(context, R.drawable.category_3)
                    4 -> AppCompatResources.getDrawable(context, R.drawable.category_4)
                    5 -> AppCompatResources.getDrawable(context, R.drawable.category_5)
                    6 -> AppCompatResources.getDrawable(context, R.drawable.category_6)
                    else -> AppCompatResources.getDrawable(context, R.drawable.category_6)
                }

            binding.habitImage.setImageDrawable(drawableImg)

            binding.root.setOnClickListener {
                listener.onItemClick(habit)
            }
        }
    }
}
