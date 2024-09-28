package com.example.mobiletodolist.utils

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletodolist.TaskItem
import com.example.mobiletodolist.databinding.TaskItemCellBinding

class TaskItemsViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemsClickListener
): RecyclerView.ViewHolder(binding.root) {

    fun bindTaskItem(taskItem: TaskItem){
        binding.taskName.text = taskItem.Description

        if(taskItem.IsCompleted == 1){
            binding.taskName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.completeButton.setImageResource(taskItem.imageResource())
        binding.completeButton.setColorFilter(taskItem.imageColor(context))

        /*binding.completeButton.setOnClickListener {
            clickListener.changeCheckedTaskItem(taskItem)
        }*/

        /*binding.taskCellContainer.setOnClickListener {
            clickListener.editTaskItem(taskItem)
        }*/

        binding.deleteButton.setOnClickListener {
            clickListener.deleteTaskItem(taskItem)
        }
    }
}