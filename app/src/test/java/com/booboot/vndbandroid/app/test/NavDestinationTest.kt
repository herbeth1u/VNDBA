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
import com.booboot.vndbandroid.app.navigation.isTopLevelDestinationInHierarchy
import com.booboot.vndbandroid.app.ui.AppState
import com.booboot.vndbandroid.core.test.android.ComposeUnitTest
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.module.Module
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Config(application = Application::class)
class NavDestinationTest : ComposeUnitTest() {

    override val modules = listOf<Module>()

    private lateinit var state: AppState
    private lateinit var navController: NavHostController

    @Composable
    private fun TestScope.setupTestRule(): NavDestination? {
        navController = rememberTestNavController()
        state = remember(navController) {
            AppState(
                windowSizeClass = getCompactWindowClass(),
                navController = navController,
                coroutineScope = backgroundScope,
            )
        }

        // Update currentDestination whenever it changes
        return state.currentDestinationState
    }

    @Test
    fun `isTopLevelDestinationInHierarchy returns true with the start destination`() =
        runTest(mainDispatcherRule.testDispatcher) {
            var currentDestination: NavDestination? = null

            composeTestRule.setContent {
                currentDestination = setupTestRule()
            }

            assertEquals(currentDestination?.route, navController.graph.startDestinationRoute)
            assertTrue(currentDestination.isTopLevelDestinationInHierarchy(TopLevelDestination.VN_LIST))
        }

    @Test
    fun `isTopLevelDestinationInHierarchy returns true after navigation to another screen`() =
        runTest(mainDispatcherRule.testDispatcher) {
            var currentDestination: NavDestination? = null

            composeTestRule.setContent {
                currentDestination = setupTestRule()

                LaunchedEffect(currentDestination) {
                    navController.navigate(DESTINATION_FOO)
                    navController.navigate(DESTINATION_BAR)

//                    navController.setCurrentDestination(TopLevelDestination.SEARCH.route)
                }
            }

            assertEquals(currentDestination?.route, DESTINATION_BAR)
            assertTrue(currentDestination.isTopLevelDestinationInHierarchy(TopLevelDestination.VN_LIST))
        }
}