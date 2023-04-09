package com.booboot.vndbandroid.app.test

import android.app.Application
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.booboot.vndbandroid.app.data.getCompactWindowClass
import com.booboot.vndbandroid.app.data.rememberTestNavController
import com.booboot.vndbandroid.app.navigation.TopLevelDestination
import com.booboot.vndbandroid.app.ui.AppState
import com.booboot.vndbandroid.core.test.android.ComposeUnitTest
import org.junit.Test
import org.koin.core.module.Module
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@Config(application = Application::class)
class AppStateTest : ComposeUnitTest() {

    override val modules = listOf<Module>()

    // Subject under test.
    private lateinit var appState: AppState

    @Test
    fun `AppState currentDestination should update automatically`() {
        var currentDestination: String? = null

        composeTestRule.setContent {
            val navController = rememberTestNavController()
            appState = remember(navController) {
                AppState(
                    navController = navController,
                    windowSizeClass = getCompactWindowClass(),
                )
            }

            // Update currentDestination whenever it changes
            currentDestination = appState.currentDestinationState?.route

            // Navigate to destination once
            LaunchedEffect(Unit) {
                navController.setCurrentDestination(TopLevelDestination.SEARCH.route)
            }
        }

        assertEquals(TopLevelDestination.SEARCH.route, currentDestination)
    }
}