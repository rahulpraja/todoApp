package com.example.models
import kotlinx.serialization.Serializable

val taskList = mutableListOf<Task>()

@Serializable
data class Task( var taskId: Int,var taskName: String,var  isTaskCompleted: Boolean)
