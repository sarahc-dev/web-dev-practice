package org.example

import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import org.http4k.format.Jackson.auto
import org.http4k.lens.BiDiBodyLens

data class RequestHeaders(val headers: Map<String, String?>)

// BiDi allows for outgoing + incoming
val jsonLens: BiDiBodyLens<RequestHeaders> = Body.auto<RequestHeaders>().toLens()

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
    },
    "/echo_headers" bind Method.GET to {
        request: Request ->
        val headers = request.headers
        val requestHeaders = RequestHeaders(headers.toMap())

        val acceptsJson = headers.any { it.first == "Accept" && it.second?.contains("json") ?: false }
        if (acceptsJson) jsonLens.inject(requestHeaders, Response(OK)) else Response(OK).body(headers.joinToString("\n") { "${it.first}: ${it.second}" })
    }
)

fun main() {
    val server = app.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())
}