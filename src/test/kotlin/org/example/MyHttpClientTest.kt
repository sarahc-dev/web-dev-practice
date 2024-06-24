package org.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MyHttpClientTest {
    @Nested
    @DisplayName("/hello")
    inner class Hello {
        @Test
        fun `if user's name is bruce and language is en-US, returns Hello Bruce`() {
            val client = MyHttpClient(baseUrl = "http://localhost:9000")
            val expected = "Hello Bruce"
            val actual = client.hello("Bruce", "en-US")
            assertEquals(expected, actual)
        }

        @Test
        fun `if user's name is Sarah and language is en-GB, returns Alright, Sarah`() {
            val client = MyHttpClient(baseUrl = "http://localhost:9000")
            val expected = "Alright, Sarah?"
            val actual = client.hello("Sarah", "en-GB")
            assertEquals(expected, actual)
        }

        @Test
        fun `if name is null and language is fr-FR, returns Bonjour`() {
            val client = MyHttpClient(baseUrl = "http://localhost:9000")
            val expected = "Bonjour"
            val actual = client.hello(null, "fr-FR")
            assertEquals(expected, actual)
        }

        @Test
        fun `if name is null and language is es-ES, returns Hello`() {
            val client = MyHttpClient(baseUrl = "http://localhost:9000")
            val expected = "Hello"
            val actual = client.hello(null, "es-ES")
            assertEquals(expected, actual)
        }

        @Test
        fun `if name and language are not entered, returns Hello`() {
            val client = MyHttpClient(baseUrl = "http://localhost:9000")
            val expected = "Hello"
            val actual = client.hello()
            assertEquals(expected, actual)
        }
    }

    @Nested
    @DisplayName("/echo_headers")
    inner class EchoHeaders {
        @Test
        fun `client doesn't accept json, returns list of headers`() {
            val client = MyHttpClient(baseUrl="http://localhost:9000")
            val expected = "Host: localhost:9000\n" +
                    "User-agent: Java-http-client/21.0.3\n" +
                    "Content-length: 0"
            val actual = client.echoHeaders(acceptsJson = false)
            assertEquals(expected, actual)
        }

        @Test
        fun `client accepts json, returns list of headers as json`() {
            val client = MyHttpClient(baseUrl="http://localhost:9000")
            val expected = "{\"headers\":{\"Accept\":\"application/json\",\"Host\":\"localhost:9000\",\"User-agent\":\"Java-http-client/21.0.3\",\"Content-length\":\"0\"}}"
            val actual = client.echoHeaders(acceptsJson = true)
            assertEquals(expected, actual)
        }

        @Test
        fun `client sends prefix query param, returns headers with prefix`() {
            val client = MyHttpClient(baseUrl="http://localhost:9000")
            val expected = "(transfer-encoding, chunked), (x-echo-content-length, 0), (x-echo-host, localhost:9000), (x-echo-user-agent, Java-http-client/21.0.3)"

            val actual = client.echoHeaders(acceptsJson = false, prefix = "X-Echo-").contains(expected)
            assertEquals(true, actual)
        }
    }
}