package com.booboot.vndbandroid.core.test.android

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.booboot.vndbandroid.core.test.kotlin.KotlinUnitTest
import org.junit.Rule

abstract class AndroidUnitTest : KotlinUnitTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
}
