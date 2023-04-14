package com.booboot.vndbandroid.core.test.kotlin

import io.mockk.mockkClass
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Rule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule

abstract class KotlinUnitTest : KoinTest {

    abstract val modules: List<Module>

    /**
     * Coroutine dispatcher rule
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    /**
     * Rule to define Koin modules
     */
    @get:Rule(order = 1)
    val koinTestRule = KoinTestRule.create {
        modules(modules + module {
            factory<CoroutineDispatcher> { mainDispatcherRule.testDispatcher }
        })
    }

    /**
     * Rule to define a mock provider
     */
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz, relaxed = true)
    }
}
