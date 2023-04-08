/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.booboot.vndbandroid.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.booboot.vndbandroid.core.designsystem.R
import com.booboot.vndbandroid.core.designsystem.icon.Icon.DrawableResourceIcon
import com.booboot.vndbandroid.core.designsystem.icon.Icon.ImageVectorIcon

/**
 * All icons used in the app. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object AppIcons {
    val ArrowBack = ImageVectorIcon(Icons.Rounded.ArrowBack)
    val Check = ImageVectorIcon(Icons.Rounded.Check)
    val MoreVert = ImageVectorIcon(Icons.Default.MoreVert)
    val List = DrawableResourceIcon(R.drawable.patient_list_48dp)
    val ListFilled = DrawableResourceIcon(R.drawable.patient_list_filled_48dp)
    val Search = ImageVectorIcon(Icons.Default.Search)
    val SearchFilled = ImageVectorIcon(Icons.Filled.Search)
    val Explore = DrawableResourceIcon(R.drawable.explore_48dp)
    val ExploreFilled = DrawableResourceIcon(R.drawable.explore_filled_48dp)
}

/**
 * A sealed class to make dealing with [ImageVector] and [DrawableRes] icons easier.
 */
sealed class Icon {
    @Composable
    abstract fun toImageVector(): ImageVector

    data class ImageVectorIcon(private val imageVector: ImageVector) : Icon() {
        @Composable
        override fun toImageVector() = imageVector
    }

    data class DrawableResourceIcon(@DrawableRes private val id: Int) : Icon() {
        @Composable
        override fun toImageVector() = ImageVector.vectorResource(id = id)
    }
}
