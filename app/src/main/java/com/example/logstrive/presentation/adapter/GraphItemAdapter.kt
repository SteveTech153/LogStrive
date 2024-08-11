package com.example.logstrive.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.logstrive.data.entity.GraphItem
import com.example.logstrive.databinding.GraphItemBinding
import com.example.logstrive.databinding.HabitCardWithTimeLayoutBinding
import com.example.logstrive.presentation.adapter.HabitCardAdapter.HabtiCardViewHolder

class GraphItemAdapter(private val graphItems: List<GraphItem>): RecyclerView.Adapter<GraphItemAdapter.GraphItemViewHolder>()  {

    class GraphItemViewHolder(val binding: GraphItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GraphItemAdapter.GraphItemViewHolder {
        val binding = GraphItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GraphItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GraphItemViewHolder, position: Int) {
        val graphItem = graphItems[position]
        holder.binding.tvHabitname.text = graphItem.habitName
        if(graphItem.noOfBoxes<60)
            holder.binding.heatmapView.setHeatmapData(graphItem.noOfBoxes, graphItem.activeIndexes, graphItem.startYearMonth, "")
        else
            holder.binding.heatmapView.setHeatmapData(graphItem.noOfBoxes, graphItem.activeIndexes, graphItem.startYearMonth, graphItem.endYearMonth)
    }

    override fun getItemCount(): Int {
        return graphItems.size
    }

}