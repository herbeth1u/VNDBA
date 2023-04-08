package com.booboot.vndbandroid.feature.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.booboot.vndbandroid.core.designsystem.component.BottomSafeSpacer
import com.booboot.vndbandroid.core.designsystem.component.TopSafeSpacer

@Composable
fun SearchRoute() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopSafeSpacer()
        Text(text = "SEARCH")
        BottomSafeSpacer()
    }
}