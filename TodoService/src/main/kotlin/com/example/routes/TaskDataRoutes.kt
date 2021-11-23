package com.example.routes
import com.example.models.Task
import com.example.models.taskList
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.taskDataRouting() {
    route("tasks") {
        get("all") {
            if (taskList.isNotEmpty()) {
                call.respond(taskList)
            } else {
                call.respondText("no tasks yet", status = HttpStatusCode.NotFound)
            }
        }
        get("{taskName}") {
            val taskName = call.parameters["taskName"] ?: return@get call.respondText(
                "task not available",
                status = HttpStatusCode.BadRequest
            )
            taskList.find { it.taskName == taskName } ?: return@get call.respondText(
                "$taskName is not  available",
                status = HttpStatusCode.NotFound
            )
            val tasks = mutableListOf<Task>()
            for (task in taskList) {
                if (task.taskName == taskName) {
                    tasks.add(task)
                }
            }

            call.respond(tasks)
        }
        post("/add") {
            val task = call.receive<Task>()
            task.taskId=taskList.size+1
            taskList.add(task)
            call.respondText("Task added successfully", status = HttpStatusCode.Created)
        }

        post("/edit") {
            val task = call.receive<Task>()
            val taskId = task.taskId
            val taskStatus = task.isTaskCompleted
            taskList.find { it.taskId == taskId } ?: call.respondText(
                "$taskId task is not  available",
                status = HttpStatusCode.NotFound
            )
            for (taskDetail in taskList) {
                if (taskDetail.taskId == taskId) {
                    taskDetail.isTaskCompleted = taskStatus
                }
                call.respondText("Task edited successfully", status = HttpStatusCode.Accepted)
            }
        }

        delete("{taskId}") {
            val taskId = call.parameters["taskId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            try {
                if (taskList.removeIf { it.taskId == Integer.parseInt(taskId) }) {
                    call.respondText("task removed successfully", status = HttpStatusCode.Accepted)
                } else {
                    call.respondText("task $taskId does not exist", status = HttpStatusCode.NotFound)
                }
            }catch(e: Exception){
                call.respondText("task id is not proper", status = HttpStatusCode.BadRequest)
            }
        }

    }
}