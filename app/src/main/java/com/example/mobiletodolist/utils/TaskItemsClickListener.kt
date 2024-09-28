package com.example.mobiletodolist.utils

import com.example.mobiletodolist.TaskItem

interface TaskItemsClickListener {
    fun editTaskItem(taskItem: TaskItem)
    fun changeCheckedTaskItem(taskItem: TaskItem)
    fun deleteTaskItem(taskItem: TaskItem)
}
