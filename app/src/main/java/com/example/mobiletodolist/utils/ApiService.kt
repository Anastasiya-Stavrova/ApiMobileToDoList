package com.example.mobiletodolist.utils

import android.net.http.UrlRequest
import com.example.mobiletodolist.TaskItem
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @GET("/show/todos")
    fun showTodos(): Call<MutableList<TaskItem>>

    @POST("/create/todos")
    fun createTodo(): Call<TaskItem>

    @DELETE("/delete/todos/{id}")
    fun deleteTodo(@Path("id") id : Int) : Call<TaskItem>

    @PUT("/update/todos/{id}")
    fun updateTodo(@Path("id") id: Int) : Call<TaskItem>

    @DELETE("/delete/todos")
    fun deleteTodos() : Call<UrlRequest.Status?>?

    @PUT("/upload/todos")
    fun uploadTodos(): Call<MutableList<TaskItem>>
}



