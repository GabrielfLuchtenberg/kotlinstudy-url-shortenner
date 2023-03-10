package com.shortener.plugins

import com.shortener.url.CreateURL
import com.shortener.url.UrlCommands
import com.shortener.url.UrlQueries
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(AutoHeadResponse)

    val urlCommands by inject<UrlCommands>()
    val urlQueries by inject<UrlQueries>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/short") {
            val url = call.receive<CreateURL>()
            val storedUrl = urlCommands.create(url)

            call.respondNullable(status = HttpStatusCode.OK, message = storedUrl)
        }

        get("/all") {
            call.respond(listOf(urlQueries.getAll()))
        }

        get("/{shortId}") {
            val shortId = call.parameters["shortId"]

            if(shortId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val url = urlQueries.get(shortId)
            if (url == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respondRedirect(url.originalURL)
        }

    }
}
