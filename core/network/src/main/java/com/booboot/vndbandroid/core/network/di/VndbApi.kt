package com.booboot.vndbandroid.core.network.di

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom

class VndbApi internal constructor(
    @PublishedApi internal val httpClient: HttpClient,
) {
    suspend inline fun <reified T> get(
        path: String,
        query: Map<String, String?> = mapOf(),
        headers: Map<String, String?> = mapOf(),
        authorization: Boolean = false,
    ) = ktorRequest<T>(
        path = path, query = query, headers = headers, authorization = authorization
    )

    suspend inline fun <reified T> post(
        path: String,
        body: Map<String, Any?> = mapOf(),
        headers: Map<String, String?> = mapOf(),
        authorization: Boolean = false,
    ) = ktorRequest<T>(
        path = path, body = body, headers = headers, authorization = authorization
    )

    @PublishedApi
    internal suspend inline fun <reified T> ktorRequest(
        path: String,
        query: Map<String, String?> = mapOf(),
        body: Map<String, Any?>? = null,
        headers: Map<String, String?> = mapOf(),
        authorization: Boolean = false,
    ) = httpClient.request {
        url {
            takeFrom("https://api.vndb.org")
            encodedPath = "/kana/$path"
        }
        headers {
            if (authorization) {
                // TODO
            }
        }

        ktorRequest(query = query, body = body, headers = headers)
    }.body<T>()
}