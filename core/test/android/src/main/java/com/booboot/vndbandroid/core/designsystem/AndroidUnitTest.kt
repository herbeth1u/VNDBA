package com.booboot.vndbandroid.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule

abstract class AndroidUnitTest : KotlinUnitTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
}
