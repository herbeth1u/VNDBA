package com.booboot.vndbandroid.app.navigation

import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val chars: Int?,
    val producers: Int?,
    val releases: Int?,
    val staff: Int?,
    val tags: Int?,
    val traits: Int?,
    val vn: Int?
)