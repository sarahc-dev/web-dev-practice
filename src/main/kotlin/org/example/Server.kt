package org.example

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

val app: HttpHandler = routes(
    "/hello" bind Method.GET to {
        request: Request ->
        val name = request.query("name")?.let { " $it" } ?: ""
        Response(OK).body("Hello$name")
    },
    "/{language}/hello" bind Method.GET to {
        request: Request ->
        var name = request.query("name")?.let { " $it" } ?: ""
        val greeting = when (request.path("language")) {
            "fr-FR" -> "Bonjour"
            "en-AU" -> "G'day"
            "it-IT" -> "Salve"
            "en-GB" -> {
                if (name != "") name += "?"
                if (name == "") "Alright" else "Alright,"
            }
            else -> "Hello"
        }

        Response(OK).body("$greeting$name")
    }

)

fun main() {
    val server = app.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())
}