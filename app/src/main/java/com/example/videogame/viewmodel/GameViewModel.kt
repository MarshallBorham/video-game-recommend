package com.example.videogame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.videogame.data.engine.PreferenceEngine
import com.example.videogame.data.local.AppDatabase
import com.example.videogame.data.local.RecommendationEntity
import com.example.videogame.data.model.Game
import com.example.videogame.data.network.RetrofitClient
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)

    val isLoading = MutableLiveData(false)
    val availableGenres = MutableLiveData<List<String>>()
    val currentPair = MutableLiveData<Pair<Game, Game>>()
    val recommendation = MutableLiveData<Game?>()
    val progress = MutableLiveData(0)

    private var allGames: List<Game> = emptyList()
    private lateinit var engine: PreferenceEngine

    private val query = """
        fields name, summary, cover.url, genres.name, rating, first_release_date;
        where rating > 75 & cover != null & genres != null & first_release_date != null;
        sort rating desc;
        limit 50;
    """.trimIndent()

    fun loadGames() {
        if (allGames.isNotEmpty()) return
        isLoading.value = true
        viewModelScope.launch {
            try {
                allGames = RetrofitClient.instance.getGames(query)
                val genres = allGames
                    .flatMap { it.genres ?: emptyList() }
                    .mapNotNull { it.name }
                    .distinct()
                    .sorted()
                availableGenres.value = genres
            } catch (e: Exception) {
                android.util.Log.e("GameViewModel", "Failed to load games", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun startWithGenres(selectedGenres: Set<String>) {
        val pool = if (selectedGenres.isEmpty()) allGames
        else allGames.filter { game ->
            game.genres?.any { it.name in selectedGenres } == true
        }.takeIf { it.size >= 4 } ?: allGames

        recommendation.value = null
        progress.value = 0
        engine = PreferenceEngine(pool)
        showNextPair()
    }

    fun choose(chosen: Game, rejected: Game) {
        engine.recordChoice(chosen, rejected)
        progress.value = engine.roundsPlayed
        if (engine.isDone()) {
            val rec = engine.getRecommendation()
            recommendation.value = rec
            viewModelScope.launch {
                rec.name?.let { name ->
                    db.recommendationDao().insert(
                        RecommendationEntity(
                            gameId = rec.id,
                            gameName = name,
                            gameSummary = rec.summary,
                            coverUrl = rec.cover?.url
                        )
                    )
                }
            }
        } else {
            showNextPair()
        }
    }

    fun skip() { showNextPair() }

    private fun showNextPair() {
        currentPair.value = engine.nextPair()
    }
}