package com.example.mobiletodolist.utils


import android.content.Context
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobiletodolist.TaskItem
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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

    fun addTaskItem(newTask: String){
        val task = """{ "Id" : "0", "Description": "$newTask" }""".trimIndent()
        val taskJson = Json.decodeFromString<TaskItem>(task);

        RetrofitBuilder.api.createTodo(taskJson).enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (response.isSuccessful) {
                    loadData(myContext)

                    Log.d("Message", "The todo was successfully added")
                }
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    /*fun updateTaskItem(id: UUID, desc: String){
        val list = taskItemsList.value
        val task = list!!.find{ it.id == id}!!
        task.description = desc
        taskItemsList.postValue(list)

        saveDataToJsonFile(myContext)
    }*/

    /*fun changeChecked(taskItem: TaskItem){
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



        val list = taskItemsList.value
        val task = list!!.find{ it.id == taskItem.id}!!
        task.checked = !task.checked
        taskItemsList.postValue(list)

        saveDataToJsonFile(myContext)
    }*/

    fun deleteTaskItem(id: Int){
        RetrofitBuilder.api.deleteTodo(id).enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (response.isSuccessful) {
                    val list = taskItemsList.value
                    val task = list!!.find{ it.Id == id}!!
                    list.remove(task)
                    taskItemsList.postValue(list)

                    Log.d("Message", "The todo was successfully deleted")
                }
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    fun changeTaskItemsList(file: File){
        RetrofitBuilder.api.deleteTodos().enqueue(object : Callback<TaskItem> {
            override fun onResponse(call: Call<TaskItem>, response: Response<TaskItem>) {
                if (response.isSuccessful) {
                    Log.d("Message", "The todo list was successfully deleted")

                    val task = file.readText().trimIndent()
                    val taskJson = Json.decodeFromString<MutableList<TaskItem>>(task);

                    RetrofitBuilder.api.uploadTodos(taskJson).enqueue(object : Callback<MutableList<TaskItem>> {
                        override fun onResponse(call: Call<MutableList<TaskItem>>,
                                                response: Response<MutableList<TaskItem>>) {
                            if (response.isSuccessful) {
                                loadData(myContext)

                                Log.d("Message", "The todo list was successfully upload")
                            } else{
                                Log.d("Message", response.message())
                            }
                        }

                        override fun onFailure(call: Call<MutableList<TaskItem>>, t: Throwable) {
                            if (t.message.toString() == "Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path $") {
                                loadData(myContext)

                                Log.d("Message", "The todo list was successfully upload")
                            }else{
                                Log.d("Error", t.message.toString())
                            }
                        }
                    })
                }
            }

            override fun onFailure(call: Call<TaskItem>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })



        /*val json = file.readText()
        val type = object : TypeToken<MutableList<TaskItem>>() {}.type
        taskItemsList.value = Gson().fromJson(json, type)

        saveDataToJsonFile(myContext)

        Toast.makeText(applicationContext, "Список дел добавлен!", LENGTH_SHORT).show()*/
    }

    fun loadData(context: Context) {
        RetrofitBuilder.api.showTodos().enqueue(object : Callback<MutableList<TaskItem>> {
            override fun onResponse(call: Call<MutableList<TaskItem>>,
                                    response: Response<MutableList<TaskItem>>) {
                if (response.isSuccessful) {
                    taskItemsList.value = response.body()
                    taskItemsList.value?.reverse()

                    Log.d("Message", "The todo list has been uploaded successfully")
                }
            }

            override fun onFailure(call: Call<MutableList<TaskItem>>, t: Throwable) {
                if (t.message.toString() == "Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path $") {
                    taskItemsList.value = mutableListOf()

                    Log.d("Message", "The todo list has been uploaded successfully")
                }else{
                    Log.d("Error", t.message.toString())
                }
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

        Log.d("Message", "The todo list has been successfully saved")
    }
}