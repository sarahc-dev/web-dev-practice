package org.example

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ServerTest {
    @Test
    fun `server test`() {
        assertEquals(Response(OK).body("Hello"), app(Request(Method.GET, "/hello")))
    }
}