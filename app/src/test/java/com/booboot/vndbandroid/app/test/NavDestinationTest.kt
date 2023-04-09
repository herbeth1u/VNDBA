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
import com.booboot.vndbandroid.app.navigation.currentRoute
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
    fun `current top level destination is equal the graph's start destination when the app starts`() {
        var currentDestination: NavDestination? = null

        composeTestRule.setContent {
            currentDestination = setupTestRule()
        }

        assertEquals(currentDestination?.route, navController.graph.startDestinationRoute)
        assertEquals(TopLevelDestination.VN_LIST, appState.currentTopLevelDestination)
    }

    @Test
    fun `current top level destination stays the same after navigating to another non-top level destination`() {
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

    @Test
    fun `clicking on a BottomBar item restores its back stack`() {
        composeTestRule.setContent {
            setupTestRule()
        }

        // GIVEN we are on the VN_LIST top level destination
        assertEquals(TopLevelDestination.VN_LIST.route, navController.currentRoute)

        // AND we navigate to the FOO screen
        navController.navigate(DESTINATION_FOO)
        assertEquals(DESTINATION_FOO, navController.currentRoute)

        // AND we change the top level destination
        appState.navigateToTopLevelDestination(TopLevelDestination.EXPLORE)
        assertEquals(TopLevelDestination.EXPLORE.route, navController.currentRoute)

        // AND navigate again in that new top level destination through FOO
        navController.navigate(DESTINATION_FOO)
        navController.navigate(DESTINATION_BAR)
        assertEquals(DESTINATION_BAR, navController.currentRoute)

        // WHEN we go back to the VN_LIST top level destination
        appState.navigateToTopLevelDestination(TopLevelDestination.VN_LIST)

        // THEN the FOO screen should be restored
        assertEquals(DESTINATION_FOO, navController.currentRoute)

        // AND WHEN we pop back stack
        navController.popBackStack()

        // THEN we should retrieve the original VN_LIST screen
        assertEquals(TopLevelDestination.VN_LIST.route, navController.currentRoute)
    }

    @Test
    fun `reselecting a BottomBar item goes back to the top level destination's route`() {
        composeTestRule.setContent {
            setupTestRule()
        }

        // GIVEN we are on the VN_LIST top level destination
        assertEquals(TopLevelDestination.VN_LIST.route, navController.currentRoute)

        // AND we navigate to the FOO screen
        navController.navigate(DESTINATION_FOO)
        assertEquals(DESTINATION_FOO, navController.currentRoute)
        assertEquals(TopLevelDestination.VN_LIST, appState.currentTopLevelDestination)

        // WHEN we reselect the VN_LIST item
        appState.navigateToTopLevelDestination(TopLevelDestination.VN_LIST)

        // THEN we should go back to this top level destination's route
        assertEquals(TopLevelDestination.VN_LIST.route, navController.currentRoute)
    }

    @Test
    fun `start destination back quits app`() {
        composeTestRule.setContent {
            setupTestRule()
        }

        // GIVEN we navigate to another top level destination
        appState.navigateToTopLevelDestination(TopLevelDestination.EXPLORE)

        // AND navigate back to the VN_LIST destination
        appState.navigateToTopLevelDestination(TopLevelDestination.VN_LIST)

        // WHEN the user uses the system button/gesture to go back
        navController.popBackStack()

        // THEN the back stack is empty (so the app quits)
        assertEquals(0, navController.backQueue.size)
    }

    @Test
    fun `back from any top level destination returns to start destination`() {
        composeTestRule.setContent {
            setupTestRule()
        }

        // GIVEN we navigate to another top level destination
        appState.navigateToTopLevelDestination(TopLevelDestination.SEARCH)

        // AND a second top level destination
        appState.navigateToTopLevelDestination(TopLevelDestination.EXPLORE)

        // WHEN the user uses the system button/gesture to go back
        navController.popBackStack()

        // THEN we return to the start destination with one destination in the back stack
        assertEquals(TopLevelDestination.VN_LIST.route, navController.currentRoute)
        assertEquals(1, navController.backQueue.count { it.destination.route != null })
    }
}