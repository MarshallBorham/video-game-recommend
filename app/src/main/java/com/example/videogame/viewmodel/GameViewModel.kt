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
        fields name, summary, cover.url, genres.name, rating;
        where rating > 75 & cover != null & genres != null;
        sort rating desc;
        limit 50;
    """.trimIndent()

    fun loadGames() {
        viewModelScope.launch {
            val games = RetrofitClient.instance.getGames(query)
            engine = PreferenceEngine(games)
            showNextPair()
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

    private fun showNextPair() {
        currentPair.value = engine.nextPair()
    }
}