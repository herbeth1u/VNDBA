package com.booboot.vndbandroid.core.media

import androidx.annotation.FloatRange
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Displays Lottie animations based on a raw resource id.
 *
 * @param resourceId Lottie JSON file raw resource id.
 * @param modifier Modifier applied on the image.
 * @param repeatForever true to repeat the animation indefinitely.
 * @param reverseOnRepeat true to play the animation backwards when it reaches the end (instead of starting again from the start).
 * @param speed The speed the animation should play at. Numbers larger than one will speed it up. Numbers between 0 and 1 will slow it down. Numbers less than 0 will play it backwards.
 * @param minProgress minimum progress from which the animation should play. Between 0 and 1. Frames lower than this value will never play.
 * @param maxProgress maximum progress to which the animation should play. Between 0 and 1. Frames higher than this value will never play.
 */
@Composable
fun AppAnimatedImage(
    @RawRes resourceId: Int,
    modifier: Modifier = Modifier,
    repeatForever: Boolean = true,
    reverseOnRepeat: Boolean = false,
    speed: Float = 1f,
    @FloatRange(from = 0.0, to = 1.0) minProgress: Float = 0f,
    @FloatRange(from = 0.0, to = 1.0) maxProgress: Float = 1f,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resourceId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = if (repeatForever) LottieConstants.IterateForever else 1,
        clipSpec = LottieClipSpec.Progress(minProgress, maxProgress)
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}