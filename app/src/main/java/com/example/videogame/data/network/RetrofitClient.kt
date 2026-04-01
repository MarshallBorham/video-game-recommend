package com.example.videogame.data.network

import com.example.videogame.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.igdb.com/v4/"

    val instance: IgdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        chain.proceed(
                            chain.request().newBuilder()
                                .addHeader("Client-ID", BuildConfig.IGDB_CLIENT_ID)
                                .addHeader("Authorization", "Bearer ${BuildConfig.IGDB_TOKEN}")
                                .build()
                        )
                    }.build()
            )
            .build()
            .create(IgdbApiService::class.java)
    }
}