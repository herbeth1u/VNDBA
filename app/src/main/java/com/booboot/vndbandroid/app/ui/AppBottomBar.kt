package com.booboot.vndbandroid.app.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.booboot.vndbandroid.app.navigation.TopLevelDestination
import com.booboot.vndbandroid.core.designsystem.component.AppNavigationBar
import com.booboot.vndbandroid.core.designsystem.component.AppNavigationBarItem
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AppBottomBar(
    destinations: ImmutableList<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    isItemSelected: @Composable (TopLevelDestination) -> Boolean,
    modifier: Modifier = Modifier,
) {
    AppNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val selected = isItemSelected(destination)

            AppNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    Icon(
                        imageVector = icon.toImageVector(),
                        modifier = Modifier.size(24.dp),
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
            )
        }
    }
}
