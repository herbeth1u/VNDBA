package com.booboot.vndbandroid.app.test

import android.app.Application
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.booboot.vndbandroid.app.data.getCompactWindowClass
import com.booboot.vndbandroid.app.data.rememberTestNavController
import com.booboot.vndbandroid.app.navigation.TopLevelDestination
import com.booboot.vndbandroid.app.ui.AppState
import com.booboot.vndbandroid.core.test.android.ComposeUnitTest
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.module.Module
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Config(application = Application::class)
class AppStateTest : ComposeUnitTest() {

    override val modules = listOf<Module>()

    // Subject under test.
    private lateinit var state: AppState

    @Test
    fun `AppState currentDestination should update automatically`() =
        runTest(mainDispatcherRule.testDispatcher) {
            var currentDestination: String? = null

            composeTestRule.setContent {
                val navController = rememberTestNavController()
                state = remember(navController) {
                    AppState(
                        windowSizeClass = getCompactWindowClass(),
                        navController = navController,
                        coroutineScope = backgroundScope,
                    )
                }

                // Update currentDestination whenever it changes
                currentDestination = state.currentDestinationState?.route

                // Navigate to destination once
                LaunchedEffect(Unit) {
                    navController.setCurrentDestination(TopLevelDestination.SEARCH.route)
                }
            }

            assertEquals(TopLevelDestination.SEARCH.route, currentDestination)
        }
}