package com.booboot.vndbandroid.core.test.android

import androidx.compose.ui.test.junit4.createComposeRule
import com.booboot.vndbandroid.core.test.kotlin.KotlinUnitTest
import org.junit.Rule

abstract class ComposeUnitTest : KotlinUnitTest() {

    @get:Rule
    val composeTestRule = createComposeRule()
}
