package com.example.videogame.data.model

data class Game(
    val id: Int,
    val name: String,
    val summary: String?,
    val cover: Cover?,
    val genres: List<Genre>?,
    val rating: Double?,
    val first_release_date: Long?
)

data class Cover(val id: Int, val url: String?)
data class Genre(val id: Int, val name: String)