package com.example.logstrive.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.logstrive.data.entity.Habit
import com.example.logstrive.databinding.ItemLayoutBinding
import com.example.logstrive.presentation.viewModel.HabitViewModel

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
            val resId = context.resources.getIdentifier("category_${habit.categoryId}", "drawable", context.packageName)
            binding.habitImage.setImageResource(resId)

            binding.root.setOnClickListener {
                listener.onItemClick(habit)
            }
        }
    }
}
