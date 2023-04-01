package com.booboot.vndbandroid.core.network.ktor

import com.booboot.vndbandroid.core.network.ktor.di.networkModule
import com.booboot.vndbandroid.core.network.ktor.http.CoreHttpMethod
import com.booboot.vndbandroid.core.network.ktor.http.ktorRequestConfig
import com.booboot.vndbandroid.core.test.kotlin.KotlinUnitTest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import org.junit.Test
import kotlin.test.assertEquals


class CoreHttpClientTest : KotlinUnitTest() {
    override val modules = listOf(networkModule)

    @Test
    fun `ktor http request is properly built`() {
        val httpRequestBuilder = HttpRequestBuilder()

        httpRequestBuilder.ktorRequestConfig(
            httpMethod = CoreHttpMethod.POST,
            baseUrl = "https://fake.url",
            urlPath = "/path",
            query = mapOf(
                "foo" to "bar"
            ),
            body = mapOf(
                "foo2" to "bar2"
            ),
            headers = mapOf(
                "foo3" to "bar3"
            )
        )

        assertEquals(HttpMethod.Post, httpRequestBuilder.method)
        assertEquals("https://fake.url/path?foo=bar", httpRequestBuilder.url.buildString())
        assertEquals(mapOf("foo2" to "bar2"), httpRequestBuilder.body)
        assertEquals(ContentType.Application.Json, httpRequestBuilder.contentType())
        assertEquals("bar3", httpRequestBuilder.headers.build()["foo3"])
    }
}