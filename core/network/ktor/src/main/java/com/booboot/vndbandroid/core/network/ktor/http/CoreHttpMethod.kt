package com.booboot.vndbandroid.core.network.ktor.http

import io.ktor.http.HttpMethod

/**
 * This class abstracts Ktor's [HttpMethod] implementation for the other modules.
 */
enum class CoreHttpMethod {
    GET, POST, PUT, DELETE
}

/**
 * Direct conversion of a [CoreHttpMethod] to a [HttpMethod].
 */
fun CoreHttpMethod.toHttpMethod() = when (this) {
    CoreHttpMethod.GET -> HttpMethod.Get
    CoreHttpMethod.POST -> HttpMethod.Post
    CoreHttpMethod.PUT -> HttpMethod.Put
    CoreHttpMethod.DELETE -> HttpMethod.Delete
}