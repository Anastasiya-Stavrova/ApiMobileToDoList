package com.example.mobiletodolist.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletodolist.TaskItem
import com.example.mobiletodolist.databinding.TaskItemCellBinding

class TaskItemsAdapter(
    private val taskItems: List<TaskItem>,
    private val clickListener: TaskItemsClickListener
): RecyclerView.Adapter<TaskItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemsViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TaskItemCellBinding.inflate(from, parent, false)
        return TaskItemsViewHolder(parent.context, binding, clickListener)
    }

    override fun getItemCount(): Int {
        return taskItems.size
    }

    override fun onBindViewHolder(holder: TaskItemsViewHolder, position: Int) {
        holder.bindTaskItem(taskItems[position])
    }
}