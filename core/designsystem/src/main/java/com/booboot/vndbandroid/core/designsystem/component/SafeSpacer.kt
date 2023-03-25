package com.booboot.vndbandroid.core.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * When using [WindowCompat.setDecorFitsSystemWindows]=false, use this composable to add an extra
 * padding and draw below the top inset.
 */
@Composable
fun TopSafeSpacer() {
    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
}

/**
 * When using [WindowCompat.setDecorFitsSystemWindows]=false, use this composable to add an extra
 * padding and draw above the bottom inset.
 */
@Composable
fun BottomSafeSpacer() {
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
}
