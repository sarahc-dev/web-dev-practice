package org.example

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ServerTest {
    @Test
    fun `if client visits hello endpoint it responds Hello`() {
        assertEquals(Response(OK).body("Hello"), app(Request(Method.GET, "/hello")))
    }

    @Test
    fun `if client visits hello endpoint and includes name param it responds Hello and name`() {
        assertEquals(Response(OK).body("Hello Sarah"), app(Request(Method.GET, "/hello").query("name", "Sarah")))
    }

    @Test
    fun `if client visits hello endpoint with language specified, it responds Hello in their language`() {
        assertEquals(Response(OK).body("Hello"), app(Request(Method.GET, "/en-US/hello")))
        assertEquals(Response(OK).body("Bonjour"), app(Request(Method.GET, "/fr-FR/hello")))
        assertEquals(Response(OK).body("G'day"), app(Request(Method.GET, "/en-AU/hello")))
        assertEquals(Response(OK).body("Salve"), app(Request(Method.GET, "/it-IT/hello")))
        assertEquals(Response(OK).body("Alright"), app(Request(Method.GET, "/en-GB/hello")))
    }

    @Test
    fun `if client visits hello endpoint with language specified and name param, it responds Hello and name in their language`() {
        assertEquals(Response(OK).body("Hello Sarah"), app(Request(Method.GET, "/en-US/hello").query("name", "Sarah")))
        assertEquals(Response(OK).body("Bonjour Leah"), app(Request(Method.GET, "/fr-FR/hello").query("name", "Leah")))
        assertEquals(Response(OK).body("G'day Ellie"), app(Request(Method.GET, "/en-AU/hello").query("name", "Ellie")))
        assertEquals(Response(OK).body("Salve Alice"), app(Request(Method.GET, "/it-IT/hello").query("name", "Alice")))
        assertEquals(Response(OK).body("Alright, Akash?"), app(Request(Method.GET, "/en-GB/hello").query("name", "Akash")))
    }

    @Test
    fun `if client visits echo_headers sending a custom header, it responds with the header`() {
        assertEquals(Response(OK).body("Accept: text/html"), app(Request(Method.GET, "/echo_headers").header("Accept", "text/html")))
    }

    @Test
    fun `if client visits echo_headers sending multiple custom headers, it responds with a list of headers`() {
        val request = app(Request(Method.GET, "/echo_headers")
            .header("Accept", "text/html")
            .header("Connection", "keep-alive")
            .header("Custom", "header"))

        assertEquals(Response(OK).body("Accept: text/html\nConnection: keep-alive\nCustom: header"), request)
    }

    @Test
    fun `if client supports json responses, it responds with headers as key-value pairs`() {
        val request = app(Request(Method.GET, "/echo_headers")
            .header("Accept", "application/json")
            .header("Connection", "keep-alive")
            .header("Custom", "header"))
        assertEquals(Response(OK).header("content-type", "application/json; charset=utf-8").body("{\"headers\":{\"Accept\":\"application/json\",\"Connection\":\"keep-alive\",\"Custom\":\"header\"}}"), request)
    }

    @Test
    fun `if client sends custom prefix param, it responds with the headers with the custom prefix`() {
        val request = app(Request(Method.GET, "/echo_headers")
            .header("X-My-Custom-Header", "some value")
            .query("as_response_headers_with_prefix", "X-Echo-"))
        val expectedResponse = Response(OK).header("X-Echo-X-My-Custom-Header", "some value").body("")
        assertEquals(expectedResponse, request)
    }
}