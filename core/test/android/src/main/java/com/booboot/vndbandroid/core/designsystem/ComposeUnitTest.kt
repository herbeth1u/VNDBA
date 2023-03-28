package com.booboot.vndbandroid.core.designsystem

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

abstract class ComposeUnitTest : KotlinUnitTest() {

    @get:Rule
    val composeTestRule = createComposeRule()
}
