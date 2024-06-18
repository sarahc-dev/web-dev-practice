package org.example

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ServerTest {
    @Test
    fun `if user visits hello endpoint it responds Hello`() {
        assertEquals(Response(OK).body("Hello"), app(Request(Method.GET, "/hello")))
    }

    @Test
    fun `if user visits hello endpoint and includes name param it responds Hello and name`() {
        assertEquals(Response(OK).body("Hello Sarah"), app(Request(Method.GET, "/hello").query("name", "Sarah")))
    }

    @Test
    fun `if user visits hello endpoint with language specified, it responds Hello in their language`() {
        assertEquals(Response(OK).body("Hello"), app(Request(Method.GET, "/en-US/hello")))
        assertEquals(Response(OK).body("Bonjour"), app(Request(Method.GET, "/fr-FR/hello")))
        assertEquals(Response(OK).body("G'day"), app(Request(Method.GET, "/en-AU/hello")))
        assertEquals(Response(OK).body("Salve"), app(Request(Method.GET, "/it-IT/hello")))
        assertEquals(Response(OK).body("Alright"), app(Request(Method.GET, "/en-GB/hello")))
    }

    @Test
    fun `if user visits hello endpoint with language specified and name param, it responds Hello and name in their language`() {
        assertEquals(Response(OK).body("Hello Sarah"), app(Request(Method.GET, "/en-US/hello").query("name", "Sarah")))
        assertEquals(Response(OK).body("Bonjour Leah"), app(Request(Method.GET, "/fr-FR/hello").query("name", "Leah")))
        assertEquals(Response(OK).body("G'day Ellie"), app(Request(Method.GET, "/en-AU/hello").query("name", "Ellie")))
        assertEquals(Response(OK).body("Salve Alice"), app(Request(Method.GET, "/it-IT/hello").query("name", "Alice")))
        assertEquals(Response(OK).body("Alright, Akash?"), app(Request(Method.GET, "/en-GB/hello").query("name", "Akash")))
    }
}