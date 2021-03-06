package com.booboot.vndbandroid.model.vndb

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Anime(
    var id: Long = 0,
    var ann_id: Int? = 0,
    var nfo_id: String? = null,
    var title_romaji: String? = null,
    var title_kanji: String? = null,
    var year: Int? = 0,
    var type: String? = null
)