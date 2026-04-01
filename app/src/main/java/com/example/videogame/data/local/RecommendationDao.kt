package com.example.videogame.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationDao {
    @Insert
    suspend fun insert(recommendation: RecommendationEntity)

    @Query("SELECT * FROM recommendations ORDER BY timestamp DESC")
    fun getAll(): Flow<List<RecommendationEntity>>
}