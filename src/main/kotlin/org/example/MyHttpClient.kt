package org.example

import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response

class MyHttpClient (private val baseUrl: String) {
    val client: HttpHandler = JavaHttpClient()

    fun hello(name: String? = null, language: String = "en-US"): String {
        val headers = listOf(Pair("Accept-language", language))
        val request = Request(Method.GET, "$baseUrl/hello")
            .let { if (name != null) it.query("name", name) else it }
            .headers(headers)

        val response = client(request)
        return response.bodyString()
    }

    fun echoHeaders(acceptsJson: Boolean, prefix: String? = null): String {
        val headers: MutableList<Pair<String, String>> = mutableListOf()
        if (acceptsJson) headers.add(Pair("Accept", "application/json"))

        val request = Request(Method.GET, "$baseUrl/echo_headers")
            .let { if (prefix != null) it.query("as_response_headers_with_prefix", prefix) else it }
            .headers(headers)
        val response = client(request)
        return if (response.bodyString() == "") response.headers.joinToString() else response.bodyString()
    }
}
