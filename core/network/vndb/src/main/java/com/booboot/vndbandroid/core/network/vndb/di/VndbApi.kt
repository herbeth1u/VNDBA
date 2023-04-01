package com.booboot.vndbandroid.core.network.vndb.di

import com.booboot.vndbandroid.core.network.ktor.http.CoreHttpClient
import com.booboot.vndbandroid.core.network.ktor.http.CoreHttpMethod

/**
 * Interface through which to call the VNDB.org API.
 *
 * @property coreHttpClient the HTTP client used to send the requests.
 */
class VndbApi internal constructor(
    @PublishedApi internal val coreHttpClient: CoreHttpClient
) {

    /**
     * Sends a GET request with the input parameters.
     *
     * @param path the URL path to call (without the host, scheme, and without the leading "/") (e.g. "stats").
     * @param query the URL queries (e.g. "?foo=bar")
     * @param headers headers of the request
     * @param authorization true if the request should be authentified for the current user and the
     *   Authorization header should be included (it is automatically retrieved)
     */
    suspend inline fun <reified T> get(
        path: String,
        query: Map<String, String?> = mapOf(),
        headers: Map<String, String?> = mapOf(),
        authorization: Boolean = false,
    ) = buildRequest<T>(
        httpMethod = CoreHttpMethod.GET,
        path = path,
        query = query,
        headers = headers,
        authorization = authorization
    )

    /**
     * Sends a POST request with the input parameters.
     *
     * @param path the URL path to call (without the host, scheme, and without the leading "/") (e.g. "stats").
     * @param body body of the request
     * @param headers headers of the request
     * @param authorization true if the request should be authentified for the current user and the
     *   Authorization header should be included (it is automatically retrieved)
     */
    suspend inline fun <reified T> post(
        path: String,
        body: Map<String, Any?> = mapOf(),
        headers: Map<String, String?> = mapOf(),
        authorization: Boolean = false,
    ) = buildRequest<T>(
        httpMethod = CoreHttpMethod.POST,
        path = path,
        body = body,
        headers = headers,
        authorization = authorization
    )

    @PublishedApi
    internal suspend inline fun <reified T> buildRequest(
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