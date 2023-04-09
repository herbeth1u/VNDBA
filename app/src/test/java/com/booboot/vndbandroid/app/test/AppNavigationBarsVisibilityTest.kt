/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.booboot.vndbandroid.app.test;


import android.app.Application
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.booboot.vndbandroid.app.ui.App
import com.booboot.vndbandroid.core.test.android.ComposeUnitTest
import com.google.accompanist.testharness.TestHarness
import org.junit.Test
import org.koin.core.module.Module
import org.robolectric.annotation.Config

/**
 * Tests that the navigation UI is rendered correctly on different screen sizes.
 */
@Config(application = Application::class)
class NavigationUiTest : ComposeUnitTest() {

    override val modules = listOf<Module>()

    @Test
    fun compactWidth_compactHeight_showsNavigationBar() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(400.dp, 400.dp)) {
                BoxWithConstraints {
                    App(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("AppBottomBar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppNavRail").assertDoesNotExist()
    }

    @Test
    fun mediumWidth_compactHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(610.dp, 400.dp)) {
                BoxWithConstraints {
                    App(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("AppNavRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppBottomBar").assertDoesNotExist()
    }

    @Test
    fun expandedWidth_compactHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(900.dp, 400.dp)) {
                BoxWithConstraints {
                    App(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("AppNavRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppBottomBar").assertDoesNotExist()
    }

    @Test
    fun compactWidth_mediumHeight_showsNavigationBar() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(400.dp, 500.dp)) {
                BoxWithConstraints {
                    App(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("AppBottomBar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppNavRail").assertDoesNotExist()
    }

    @Test
    fun mediumWidth_mediumHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(610.dp, 500.dp)) {
                BoxWithConstraints {
                    App(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("AppNavRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppBottomBar").assertDoesNotExist()
    }

    @Test
    fun expandedWidth_mediumHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(900.dp, 500.dp)) {
                BoxWithConstraints {
                    App(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("AppNavRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppBottomBar").assertDoesNotExist()
    }

    @Test
    fun compactWidth_expandedHeight_showsNavigationBar() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(400.dp, 1000.dp)) {
                BoxWithConstraints {
                    App(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("AppBottomBar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppNavRail").assertDoesNotExist()
    }

    @Test
    fun mediumWidth_expandedHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(610.dp, 1000.dp)) {
                BoxWithConstraints {
                    App(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("AppNavRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppBottomBar").assertDoesNotExist()
    }

    @Test
    fun expandedWidth_expandedHeight_showsNavigationRail() {
        composeTestRule.setContent {
            TestHarness(size = DpSize(900.dp, 1000.dp)) {
                BoxWithConstraints {
                    App(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            DpSize(maxWidth, maxHeight),
                        ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("AppNavRail").assertIsDisplayed()
        composeTestRule.onNodeWithTag("AppBottomBar").assertDoesNotExist()
    }
}
