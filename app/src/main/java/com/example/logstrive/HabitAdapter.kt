package com.example.logstrive

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.PrimaryKey
import com.example.logstrive.databinding.ItemLayoutBinding
import kotlinx.coroutines.launch

class HabitAdapter(
    private val activity: FragmentActivity,
    private val habitViewModel: HabitViewModel,
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
        return HabitViewHolder(binding, activity, habitViewModel, listener)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    class HabitViewHolder(
        private val binding: ItemLayoutBinding,
        private val activity: FragmentActivity,
        private val habitViewModel: HabitViewModel,
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
