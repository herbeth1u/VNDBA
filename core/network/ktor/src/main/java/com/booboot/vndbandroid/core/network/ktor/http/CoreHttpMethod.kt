package com.booboot.vndbandroid.core.network.ktor.http

import io.ktor.http.HttpMethod

enum class CoreHttpMethod {
    GET, POST, PUT, DELETE
}

fun CoreHttpMethod.toHttpMethod() = when (this) {
    CoreHttpMethod.GET -> HttpMethod.Get
    CoreHttpMethod.POST -> HttpMethod.Post
    CoreHttpMethod.PUT -> HttpMethod.Put
    CoreHttpMethod.DELETE -> HttpMethod.Delete
}