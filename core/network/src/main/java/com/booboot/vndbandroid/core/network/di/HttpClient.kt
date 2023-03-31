package com.booboot.vndbandroid.core.network.di

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

@PublishedApi
internal fun HttpRequestBuilder.ktorRequest(
    query: Map<String, String?>,
    body: Map<String, Any?>?,
    headers: Map<String, String?>,
) {
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