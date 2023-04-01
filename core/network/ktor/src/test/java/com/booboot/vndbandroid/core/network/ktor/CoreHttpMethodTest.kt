package com.booboot.vndbandroid.core.network.ktor

import com.booboot.vndbandroid.core.network.ktor.http.CoreHttpMethod
import com.booboot.vndbandroid.core.network.ktor.http.toHttpMethod
import com.booboot.vndbandroid.core.test.kotlin.KotlinUnitTest
import io.ktor.http.HttpMethod
import org.junit.Test
import org.koin.core.module.Module
import kotlin.test.assertEquals


class CoreHttpMethodTest : KotlinUnitTest() {

    override val modules = listOf<Module>()

    @Test
    fun `test toHttpMethod`() {
        assertEquals(HttpMethod.Get, CoreHttpMethod.GET.toHttpMethod())
        assertEquals(HttpMethod.Post, CoreHttpMethod.POST.toHttpMethod())
        assertEquals(HttpMethod.Put, CoreHttpMethod.PUT.toHttpMethod())
        assertEquals(HttpMethod.Delete, CoreHttpMethod.DELETE.toHttpMethod())
    }
}