package com.example.videogame.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videogame.data.engine.PreferenceEngine
import com.example.videogame.data.model.Game
import com.example.videogame.data.network.RetrofitClient
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private lateinit var engine: PreferenceEngine

    val currentPair = MutableLiveData<Pair<Game, Game>>()
    val recommendation = MutableLiveData<Game?>()
    val progress = MutableLiveData(0)

    private val query = """
        fields name, summary, cover.url, genres.name, rating, first_release_date;
        where rating > 75 & cover != null & genres != null & first_release_date != null;
        sort rating desc;
        limit 50;
    """.trimIndent()

    fun loadGames() {
        viewModelScope.launch {
            try {
                val games = RetrofitClient.instance.getGames(query)
                android.util.Log.d("GameViewModel", "Loaded ${games.size} games")
                if (games.isNotEmpty()) {
                    android.util.Log.d("GameViewModel", "First game: ${games[0]}")
                }
                engine = PreferenceEngine(games)
                showNextPair()
            } catch (e: Exception) {
                android.util.Log.e("GameViewModel", "Failed to load games", e)
            }
        }
    }

    fun choose(chosen: Game, rejected: Game) {
        engine.recordChoice(chosen, rejected)
        progress.value = engine.roundsPlayed
        if (engine.isDone()) {
            recommendation.value = engine.getRecommendation()
        } else {
            showNextPair()
        }
    }

    fun skip() {
        showNextPair()
    }

    private fun showNextPair() {
        currentPair.value = engine.nextPair()
    }
}