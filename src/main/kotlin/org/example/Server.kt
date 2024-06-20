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
import org.http4k.lens.Query

val queryName = Query.optional("name")
data class RequestHeaders(val headers: Map<String, String?>)

// BiDi allows for outgoing + incoming
val jsonLens: BiDiBodyLens<RequestHeaders> = Body.auto<RequestHeaders>().toLens()

val app: HttpHandler = routes(
    "/hello" bind Method.GET to {
        request: Request ->
        var name = queryName(request)
        val acceptedLanguage = request.headers.find { it.first == "Accept-language" }?.second

        val greeting = when {
            acceptedLanguage == null -> "Hello"
            acceptedLanguage.contains("fr-FR") -> "Bonjour"
            acceptedLanguage.contains("en-AU") -> "G'day"
            acceptedLanguage.contains("it-IT") -> "Salve"
            acceptedLanguage.contains("en-GB") -> {
                if (name != null) name += "?"
                if (name == null) "Alright" else "Alright,"
            }
            else -> "Hello"
        }
        Response(OK).body("$greeting${if (name != null) " $name" else ""}")
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
        val responseHeadersPrefix = request.query("as_response_headers_with_prefix")

        if (responseHeadersPrefix == null) {
            val requestHeaders = RequestHeaders(headers.toMap())

            val acceptsJson = headers.any { it.first == "Accept" && it.second?.contains("json") ?: false }
            if (acceptsJson) jsonLens.inject(requestHeaders, Response(OK)) else Response(OK).body(headers.joinToString("\n") { "${it.first}: ${it.second}" })
        } else {
            val headersWithPrefix = headers.map { Pair(responseHeadersPrefix + it.first, it.second) }
            Response(OK).headers(headersWithPrefix)
        }
    }
)

fun main() {
    val server = app.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())
}