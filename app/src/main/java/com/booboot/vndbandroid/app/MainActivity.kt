package com.booboot.vndbandroid.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.booboot.vndbandroid.app.ui.App
import com.booboot.vndbandroid.core.designsystem.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(
                enableDynamicTheming = true,
            ) {
                App(
                    windowSizeClass = calculateWindowSizeClass(this),
                )
            }
        }
    }
}