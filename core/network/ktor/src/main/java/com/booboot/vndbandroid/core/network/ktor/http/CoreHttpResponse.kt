package com.booboot.vndbandroid.core.network.ktor.http

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

class CoreHttpResponse(
    val httpResponse: HttpResponse
) {
    suspend inline fun <reified T> body() = httpResponse.body<T>()
}

fun HttpResponse.toCoreHttpResponse() = CoreHttpResponse(this)
