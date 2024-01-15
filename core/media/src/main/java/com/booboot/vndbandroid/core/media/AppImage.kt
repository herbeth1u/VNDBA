package com.booboot.vndbandroid.core.media

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

/**
 * The [AppImage] composable can display images with easy customization like zooming, tinting or
 * crossfade. It also abstracts the internal image loader library to the rest of the app.
 *
 * @param modifier The [Modifier] applied to the [AsyncImage].
 * @param data any type of data recognized by Coil.
 *   Supported types: String (URI), File, Int (resource)
 * @param crossfade true to enable the fade-in animation.
 * @param zoomable true to enable zoom
 * @param placeholder drawable resource placeholder
 * @param error error resource placeholder
 * @param contentScale [ContentScale] to use to display the success image
 * @param errorContentScale [ContentScale] to use to display the error image
 * @param placeholderContentScale [ContentScale] to use to display the placeholder
 * @param alpha value between 0 and 1 to control the opacity
 * @param tint [Color] used to tint the image
 * @param onSuccess called when the media has successfully loaded. Provides the [Drawable].
 * @param onError called when the media could not load.
 */
@Composable
fun AppImage(
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
    tint: Color? = null,
    onSuccess: (Drawable) -> Unit = {},
    onError: (Throwable) -> Unit = {}
) {
    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current
    var contentScaleState by remember { mutableStateOf(placeholderContentScale) }

    val zoomState = rememberZoomState()
    val zoomModifier = if (zoomable) {
        Modifier.zoomable(zoomState)
    } else Modifier

    val imageRequest = remember(data) {
        ImageRequest.Builder(context).apply {
            data(data)
            if (placeholder != null && contentScale == ContentScale.Fit) {
                // Bug in Coil: image is not properly scaled when using a placeholder with [ContentScale.Fit].
                // Crossfade is always disabled in this case.
                crossfade(false)
            } else {
                crossfade(crossfade)
            }
            when {
                placeholder != null -> placeholder(placeholder)
                isInPreview && data is Int -> placeholder(data)
            }
            error?.let {
                error(error)
            }
        }.build()
    }

    AsyncImage(
        modifier = modifier
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
            .clipToBounds()
            .then(zoomModifier),
        contentDescription = null,
        model = imageRequest,
        onState = { state ->
            when (state) {
                is AsyncImagePainter.State.Success -> {
                    contentScaleState = contentScale
                    zoomState.setContentSize(state.painter.intrinsicSize)
                    onSuccess(state.result.drawable)
                }

                is AsyncImagePainter.State.Error -> {
                    contentScaleState = errorContentScale
                    onError(state.result.throwable)
                }

                else -> {
                }
            }
        },
        contentScale = contentScaleState,
        alpha = alpha,
        colorFilter = tint?.let { ColorFilter.tint(tint) }
    )
}

@Preview(showBackground = true, backgroundColor = 0x00FF00)
@Composable
fun AppImagePreview() {
    AppImage(
        data = R.drawable.ic_android_black_24dp,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(100.dp),
        tint = Color.White
    )
}