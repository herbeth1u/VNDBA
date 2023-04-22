package com.booboot.vndbandroid.core.designsystem.component

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import com.booboot.vndbandroid.core.designsystem.theme.LocalTintTheme
import com.booboot.vndbandroid.core.media.AppImage

/**
 * A wrapper around [AppImage] which determines the colorFilter based on the theme
 */
@Composable
fun TintedIcon(
    data: Any?,
    modifier: Modifier = Modifier,
    crossfade: Boolean = true,
    zoomable: Boolean = false,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    errorContentScale: ContentScale = contentScale,
    placeholderContentScale: ContentScale = contentScale,
    alpha: Float = DefaultAlpha,
    onSuccess: (Drawable) -> Unit = {},
    onError: (Throwable) -> Unit = {}
) {
    val iconTint = LocalTintTheme.current.iconTint

    AppImage(
        data = data,
        modifier = modifier,
        crossfade = crossfade,
        zoomable = zoomable,
        placeholder = placeholder,
        error = error,
        contentScale = contentScale,
        errorContentScale = errorContentScale,
        placeholderContentScale = placeholderContentScale,
        alpha = alpha,
        tint = iconTint,
        onSuccess = onSuccess,
        onError = onError,
    )
}
