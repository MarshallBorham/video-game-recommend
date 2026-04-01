package com.example.videogame.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommendations")
data class RecommendationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameId: Int,
    val gameName: String,
    val gameSummary: String?,
    val coverUrl: String?,
    val timestamp: Long = System.currentTimeMillis()
)