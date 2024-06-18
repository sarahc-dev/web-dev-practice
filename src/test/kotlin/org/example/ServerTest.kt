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
}