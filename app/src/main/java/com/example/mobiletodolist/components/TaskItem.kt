package com.example.mobiletodolist

import android.content.Context
import androidx.core.content.ContextCompat
import kotlinx.serialization.Serializable

@Serializable
data class TaskItem(
    var Id : Int,
    var Description: String,
    var IsCompleted: String = "0"
) {

    fun imageResource(): Int
    {
        if(IsCompleted == "1"){
            return R.drawable.radio_checked
        }
        return R.drawable.radio_unchecked
    }

    fun imageColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.main_color)
    }
}