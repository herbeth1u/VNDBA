package com.booboot.vndbandroid.app

import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.graphics.toColor
import androidx.core.view.WindowCompat
import com.booboot.vndbandroid.core.designsystem.theme.AppTheme
import com.booboot.vndbandroid.core.test.android.AndroidUnitTest
import org.junit.Test
import org.koin.core.module.Module
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AppThemeTest : AndroidUnitTest() {

    override val modules = listOf<Module>()

    @Test
    fun `system bars are styled properly in light theme`() {
        var view: View? = null

        composeTestRule.setContent {
            AppTheme(darkTheme = false) {
                view = LocalView.current
            }
        }

        val window = composeTestRule.activity.window
        println(window.statusBarColor.toColor())
        assertEquals(Color.Transparent.value.toInt(), window.statusBarColor)

        assertNotNull(view)
        val windowInsetsController = WindowCompat.getInsetsController(window, view!!)
        assertTrue(windowInsetsController.isAppearanceLightStatusBars)
        assertTrue(windowInsetsController.isAppearanceLightNavigationBars)
    }

    @Test
    fun `system bars are styled properly in dark theme`() {
        var view: View? = null

        composeTestRule.setContent {
            AppTheme(darkTheme = true) {
                view = LocalView.current
            }
        }

        val window = composeTestRule.activity.window
        println(window.statusBarColor.toColor())
        assertEquals(Color.Transparent.value.toInt(), window.statusBarColor)

        assertNotNull(view)
        val windowInsetsController = WindowCompat.getInsetsController(window, view!!)
        assertFalse(windowInsetsController.isAppearanceLightStatusBars)
        assertFalse(windowInsetsController.isAppearanceLightNavigationBars)
    }
}