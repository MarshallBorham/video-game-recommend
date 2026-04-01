package com.example.videogame.data.engine

import com.example.videogame.data.model.Game

class PreferenceEngine(private val gamePool: List<Game>) {
    private val scores = mutableMapOf<Int, Double>()
    private val usedPairs = mutableSetOf<Pair<Int, Int>>()
    private var currentPairIds: Pair<Int, Int>? = null
    var roundsPlayed = 0
    val totalRounds = 10

    init { gamePool.forEach { scores[it.id] = 0.0 } }

    fun isDone() = roundsPlayed >= totalRounds

    fun nextPair(): Pair<Game, Game> {
        val excludeIds = currentPairIds?.let { setOf(it.first, it.second) } ?: emptySet()
        val available = gamePool.filter { it.id !in excludeIds }.shuffled()

        if (available.size >= 2) {
            for (i in available.indices) {
                for (j in i + 1 until available.size) {
                    val key = Pair(available[i].id, available[j].id)
                    if (key !in usedPairs) {
                        usedPairs.add(key)
                        currentPairIds = key
                        return Pair(available[i], available[j])
                    }
                }
            }
        }

        // Fallback if all non-current pairs exhausted
        val shuffled = gamePool.shuffled()
        val key = Pair(shuffled[0].id, shuffled[1].id)
        currentPairIds = key
        return Pair(shuffled[0], shuffled[1])
    }

    fun recordChoice(chosen: Game, rejected: Game) {
        scores[chosen.id] = (scores[chosen.id] ?: 0.0) + 1.0
        chosen.genres?.forEach { genre ->
            gamePool
                .filter { it.id != chosen.id && it.genres?.any { g -> g.id == genre.id } == true }
                .forEach { scores[it.id] = (scores[it.id] ?: 0.0) + 0.3 }
        }
        roundsPlayed++
    }

    fun getRecommendation(): Game {
        return gamePool.maxByOrNull { scores[it.id] ?: 0.0 }!!
    }
}