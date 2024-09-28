package com.example.mobiletodolist.utils


import android.content.Context
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobiletodolist.TaskItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class TaskViewModel: ViewModel() {
    var taskItemsList = MutableLiveData<MutableList<TaskItem>>()

    private lateinit var myContext: Context

    init {
        taskItemsList.value = mutableListOf()
    }

    /*fun addTaskItem(newTask: TaskItem){
        val list = taskItemsList.value
        list!!.add(0, newTask)
        taskItemsList.postValue(list)

        saveDataToJsonFile(myContext)
    }*/

    /*fun updateTaskItem(id: UUID, desc: String){
        val list = taskItemsList.value
        val task = list!!.find{ it.id == id}!!
        task.description = desc
        taskItemsList.postValue(list)

        saveDataToJsonFile(myContext)
    }*/

    fun changeChecked(taskItem: TaskItem){
        var changed = if (taskItem.IsCompleted == 1){ 0 }else{ 1 }

        RetrofitBuilder.api.updateTodo(taskItem.Id).enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (response.isSuccessful) {
                    Log.d("rrrrrrrrrr", "${response.body()}")
                    /*val list = taskItemsList.value
                    val task = list!!.find{ it.Id == taskItem.Id}!!

                    if (task.IsCompleted == 1){
                        task.IsCompleted = 0
                    }else{
                        task.IsCompleted = 1
                    }

                    taskItemsList.postValue(list)*/
                }
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })



        /*val list = taskItemsList.value
        val task = list!!.find{ it.id == taskItem.id}!!
        task.checked = !task.checked
        taskItemsList.postValue(list)

        saveDataToJsonFile(myContext)*/
    }

    fun deleteTaskItem(id: Int){
        RetrofitBuilder.api.deleteTodo(id).enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (response.isSuccessful) {
                    val list = taskItemsList.value
                    val task = list!!.find{ it.Id == id}!!
                    list.remove(task)
                    taskItemsList.postValue(list)
                }
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    /*fun changeTaskItemsList(file: File){
        val json = file.readText()
        val type = object : TypeToken<MutableList<TaskItem>>() {}.type
        taskItemsList.value = Gson().fromJson(json, type)

        saveDataToJsonFile(myContext)
    }*/

    fun loadData(context: Context) {
        RetrofitBuilder.api.showTodos().enqueue(object : Callback<MutableList<TaskItem>> {
            override fun onResponse(call: Call<MutableList<TaskItem>>,
                                    response: Response<MutableList<TaskItem>>) {

                if (response.isSuccessful) {
                    taskItemsList.value = response.body()
                }
            }

            override fun onFailure(call: Call<MutableList<TaskItem>>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })

        myContext = context
    }


    /*val file = File(context.filesDir, "taskItems.json")

    if (file.exists()) {
        val json = file.readText()
        val type = object : TypeToken<MutableList<TaskItem>>() {}.type
        taskItemsList.value = Gson().fromJson(json, type)
    }*/




    /*private fun saveDataToJsonFile(context: Context) {
        val list = taskItemsList.value

        val gson = Gson()
        val jsonString = gson.toJson(list)

        val file = File(context.filesDir, "taskItems.json")
        file.writeText(jsonString)
    }*/

    fun downloadFile(){
        val sdf = SimpleDateFormat("dd.M.yyyy_hh:mm:ss")
        val currentDate = sdf.format(Date())

        val file = File("/storage/emulated/0/Download/", "Tasks_${currentDate}.json")
        file.createNewFile()

        val list = taskItemsList.value

        val gson = Gson()
        val jsonString = gson.toJson(list)

        file.writeText(jsonString)

        Toast.makeText(myContext, "Список дел сохранен!", LENGTH_SHORT).show()
    }
}