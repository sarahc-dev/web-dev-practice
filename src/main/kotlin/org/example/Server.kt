package org.example

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

val app: HttpHandler = routes(
    "/hello" bind Method.GET to {
        Response(OK).body("Hello")
    }
)

fun main() {
    val server = app.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())
}