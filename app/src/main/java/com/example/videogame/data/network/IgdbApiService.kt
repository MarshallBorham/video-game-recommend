package com.example.videogame.data.network

import com.example.videogame.data.model.Game
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IgdbApiService {
    @POST("games")
    @Headers("Content-Type: text/plain")
    suspend fun getGames(@Body query: String): List<Game>
}