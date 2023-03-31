package com.booboot.vndbandroid.core.network.vndb.di

import com.booboot.vndbandroid.core.network.ktor.http.CoreHttpClient
import com.booboot.vndbandroid.core.network.ktor.http.CoreHttpMethod


class VndbApi internal constructor(
    @PublishedApi internal val coreHttpClient: CoreHttpClient
) {

    suspend inline fun <reified T> get(
        path: String,
        query: Map<String, String?> = mapOf(),
        headers: Map<String, String?> = mapOf(),
        authorization: Boolean = false,
    ) = ktorRequest<T>(
        httpMethod = CoreHttpMethod.GET,
        path = path,
        query = query,
        headers = headers,
        authorization = authorization
    )

    suspend inline fun <reified T> post(
        path: String,
        body: Map<String, Any?> = mapOf(),
        headers: Map<String, String?> = mapOf(),
        authorization: Boolean = false,
    ) = ktorRequest<T>(
        httpMethod = CoreHttpMethod.POST,
        path = path,
        body = body,
        headers = headers,
        authorization = authorization
    )

    @PublishedApi
    internal suspend inline fun <reified T> ktorRequest(
        httpMethod: CoreHttpMethod,
        path: String,
        query: Map<String, String?> = mapOf(),
        body: Map<String, Any?>? = null,
        headers: Map<String, String?> = mapOf(),
        authorization: Boolean = false,
    ) = run {
        val allHeaders = headers.run {
            if (authorization) {
                // TODO
            }
            this
        }

        coreHttpClient.ktorRequest(
            httpMethod = httpMethod,
            baseUrl = "https://api.vndb.org",
            urlPath = "/kana/$path",
            query = query,
            body = body,
            headers = allHeaders
        ).body<T>()
    }
}