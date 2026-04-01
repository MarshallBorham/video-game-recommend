package com.example.videogame.data.model

import com.google.gson.annotations.SerializedName

data class Game(
    val id: Int = 0,
    val name: String? = null,
    val summary: String? = null,
    val cover: Cover? = null,
    val genres: List<Genre>? = null,
    val rating: Double? = null,
    @SerializedName("first_release_date")
    val first_release_date: Long? = null
)

data class Cover(
    val id: Int = 0,
    val url: String? = null
)

data class Genre(
    val id: Int = 0,
    val name: String? = null
)