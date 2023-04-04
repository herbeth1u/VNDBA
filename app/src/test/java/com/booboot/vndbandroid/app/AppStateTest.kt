package com.booboot.vndbandroid.app

import android.app.Application
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
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
                currentDestination = state.currentDestination?.route

                // Navigate to destination b once
                LaunchedEffect(Unit) {
                    navController.setCurrentDestination("b")
                }
            }

            assertEquals("b", currentDestination)
        }

    private fun getCompactWindowClass() = WindowSizeClass.calculateFromSize(DpSize(500.dp, 300.dp))
}

@Composable
private fun rememberTestNavController(): TestNavHostController {
    val context = LocalContext.current
    val navController = remember {
        TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            graph = createGraph(startDestination = "a") {
                composable("a") { }
                composable("b") { }
                composable("c") { }
            }
        }
    }
    return navController
}