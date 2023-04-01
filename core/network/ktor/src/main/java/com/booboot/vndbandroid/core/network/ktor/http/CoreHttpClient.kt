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

/**
 * This class abstracts Ktor's [HttpClient] implementation from the other modules.
 * If Ktor has to be replaced by another HTTP client, only this module should be affected.
 *
 * @property httpClient Ktor's [HttpClient] implementation
 */
class CoreHttpClient internal constructor(
    private val httpClient: HttpClient,
) {

    /**
     * Builds and executes an HTTP request using Ktor.
     *
     * @param httpMethod one of [CoreHttpMethod]
     * @param baseUrl the base URL of the request (WITHOUT THE PATH, only scheme+host)
     * @param urlPath the rest of the path of the URL
     * @param query the URL queries (e.g. "?foo=bar")
     * @param body body of the request
     * @param headers headers of the request
     */
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

/**
 * Builds a Ktor's [HttpRequestBuilder] using the input parameters.
 */
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