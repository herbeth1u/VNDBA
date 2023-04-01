package com.booboot.vndbandroid.core.network.ktor.http

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom

class CoreHttpClient internal constructor(
    private val httpClient: HttpClient,
) {

    suspend fun ktorRequest(
        httpMethod: CoreHttpMethod,
        baseUrl: String,
        urlPath: String,
        query: Map<String, String?>,
        body: Map<String, Any?>?,
        headers: Map<String, String?>,
    ) = httpClient.request {
        ktorRequestConfig(
            httpMethod = httpMethod,
            baseUrl = baseUrl,
            urlPath = urlPath,
            query = query,
            body = body,
            headers = headers
        )
    }.toCoreHttpResponse()
}

internal fun HttpRequestBuilder.ktorRequestConfig(
    httpMethod: CoreHttpMethod,
    baseUrl: String,
    urlPath: String,
    query: Map<String, String?>,
    body: Map<String, Any?>?,
    headers: Map<String, String?>,
) {
    method = httpMethod.toHttpMethod()
    url {
        takeFrom(baseUrl)
        encodedPath = urlPath
    }

    url {
        query.forEach { (name, value) ->
            value?.let {
                parameters.append(name, value)
            }
        }
    }
    headers {
        headers.forEach { (name, value) ->
            value?.let {
                append(name, value)
            }
        }
    }
    if (body != null) {
        contentType(ContentType.Application.Json)
        setBody(body)
    }
}