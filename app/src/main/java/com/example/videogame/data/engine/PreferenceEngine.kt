package com.example.videogame.data.engine

class PreferenceEngine(private val gamePool: List<Game>) {
    private val scores = mutableMapOf<Int, Double>()
    private val usedPairs = mutableSetOf<Pair<Int, Int>>()
    var roundsPlayed = 0
    val totalRounds = 10

    init { gamePool.forEach { scores[it.id] = 0.0 } }

    fun isDone() = roundsPlayed >= totalRounds

    fun nextPair(): Pair<Game, Game> {
        val shuffled = gamePool.shuffled()
        for (i in shuffled.indices) {
            for (j in i + 1 until shuffled.size) {
                val key = Pair(shuffled[i].id, shuffled[j].id)
                if (key !in usedPairs) {
                    usedPairs.add(key)
                    return Pair(shuffled[i], shuffled[j])
                }
            }
        }
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