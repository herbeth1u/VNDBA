package com.booboot.vndbandroid.app.test

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.booboot.vndbandroid.app.data.DESTINATION_BAR
import com.booboot.vndbandroid.app.data.DESTINATION_FOO
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
class NavDestinationTest : ComposeUnitTest() {

    override val modules = listOf<Module>()

    private lateinit var appState: AppState
    private lateinit var navController: NavHostController

    @Composable
    private fun setupTestRule(): NavDestination? {
        navController = rememberTestNavController()
        appState = remember(navController) {
            AppState(
                navController = navController,
                windowSizeClass = getCompactWindowClass(),
            )
        }

        // Update currentDestination whenever it changes
        return appState.currentDestinationState
    }

    @Test
    fun `isTopLevelDestinationInHierarchy returns true with the start destination`() {
        var currentDestination: NavDestination? = null

        composeTestRule.setContent {
            currentDestination = setupTestRule()
        }

        assertEquals(currentDestination?.route, navController.graph.startDestinationRoute)
        assertEquals(TopLevelDestination.VN_LIST, appState.currentTopLevelDestination)
    }

    @Test
    fun `isTopLevelDestinationInHierarchy returns true after navigation to another screen`() {
        var currentDestination: NavDestination? = null

        composeTestRule.setContent {
            currentDestination = setupTestRule()

            LaunchedEffect(Unit) {
                navController.navigate(DESTINATION_FOO)
                navController.navigate(DESTINATION_BAR)
            }
        }

        assertEquals(currentDestination?.route, DESTINATION_BAR)
        assertEquals(TopLevelDestination.VN_LIST, appState.currentTopLevelDestination)
    }
}