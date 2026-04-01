package com.example.videogame.data.network

import com.example.videogame.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.igdb.com/v4/"

    val instance: IgdbApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
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
                    }
                    .addInterceptor(logging)
                    .build()
            )
            .build()
            .create(IgdbApiService::class.java)
    }
}