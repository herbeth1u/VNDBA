package com.booboot.vndbandroid.core.network.ktor.http

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * This class abstracts Ktor's [HttpResponse] implementation for the other modules.
 */
class CoreHttpResponse(
    val httpResponse: HttpResponse
) {
    /**
     * Delegate for [HttpResponse.body].
     * This method needs to be inlined, because it needs to be reified.
     * This method was not included into [CoreHttpClient.ktorRequest] to let the calling module
     * the possibility to retrieve the [HttpResponse] headers or other information not included in
     * the body.
     */
    suspend inline fun <reified T> body() = httpResponse.body<T>()
}

fun HttpResponse.toCoreHttpResponse() = CoreHttpResponse(this)
