package com.booboot.vndbandroid.core.network.ktor

import com.booboot.vndbandroid.core.network.ktor.di.networkModule
import com.booboot.vndbandroid.core.test.kotlin.KotlinUnitTest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.pluginOrNull
import io.ktor.serialization.kotlinx.json.json
import io.mockk.justRun
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.core.component.get
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class HttpClientTest : KotlinUnitTest() {
    override val modules = listOf(networkModule)

    @Test
    fun `json parser should ignore unknown keys`() = runTest(mainDispatcherRule.testDispatcher) {
        val jsonSlot = slot<Json>()
        mockkStatic(ContentNegotiation.Config::json)
        justRun { any<ContentNegotiation.Config>().json(capture(jsonSlot)) }

        val httpClient = get<HttpClient>()
        val json = jsonSlot.captured
        assertTrue(json.configuration.ignoreUnknownKeys)

        val contentNegotiation = httpClient.pluginOrNull(ContentNegotiation)
        assertNotNull(contentNegotiation)
    }
}