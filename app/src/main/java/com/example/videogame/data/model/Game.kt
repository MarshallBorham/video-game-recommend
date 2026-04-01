package com.example.videogame.data.model

import com.google.gson.annotations.SerializedName

data class Game(
    val id: Int,
    val name: String?,
    val summary: String?,
    val cover: Cover?,
    val genres: List<Genre>?,
    val rating: Double?,
    val first_release_date: Long?
)

data class Cover(
    val id: Int,
    @SerializedName("url") val url: String?
)

data class Genre(
    val id: Int,
    val name: String?
)