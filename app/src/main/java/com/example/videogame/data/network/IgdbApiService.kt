package com.example.videogame.data.network

interface IgdbApiService {
    @POST("games")
    @Headers("Content-Type: text/plain")
    suspend fun getGames(@Body query: String): List<Game>
}