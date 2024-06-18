package org.example

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes

val app: HttpHandler = routes(
    "/hello" bind Method.GET to {
        Response(OK).body("Hello")
    }
)

fun main() {
    println("Hello World!")
}