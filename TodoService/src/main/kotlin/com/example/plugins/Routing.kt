package com.example.plugins

import com.example.routes.taskDataRouting
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*


fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("welcome to TODO api service!")
        }
        taskDataRouting()
    }
}
