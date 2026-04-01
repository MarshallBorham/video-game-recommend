package com.example.videogame.ui.compare

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.videogame.R
import com.example.videogame.data.model.Game
import com.example.videogame.databinding.FragmentCompareBinding
import com.example.videogame.viewmodel.GameViewModel
import java.util.Calendar

class CompareFragment : Fragment(R.layout.fragment_compare) {
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentCompareBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCompareBinding.bind(view)

        viewModel.currentPair.observe(viewLifecycleOwner) { (gameA, gameB) ->
            animateNewPair(gameA, gameB)

            binding.gameACover.setOnClickListener { viewModel.choose(gameA, gameB) }
            binding.gameBCover.setOnClickListener { viewModel.choose(gameB, gameA) }
            binding.skipButton.setOnClickListener { viewModel.skip() }
        }

        viewModel.recommendation.observe(viewLifecycleOwner) { game ->
            game?.let { findNavController().navigate(R.id.action_compare_to_result) }
        }
    }

    private fun animateNewPair(gameA: Game, gameB: Game) {
        val duration = 250L
        val offset = 80f

        listOf(binding.gameAContainer, binding.gameBContainer).forEach { container ->
            container.animate()
                .alpha(0f)
                .translationX(-offset)
                .setDuration(duration)
                .withEndAction {
                    bindGame(gameA, isTop = true)
                    bindGame(gameB, isTop = false)
                    container.translationX = offset
                    container.animate()
                        .alpha(1f)
                        .translationX(0f)
                        .setDuration(duration)
                        .start()
                }.start()
        }
    }

    private fun bindGame(game: Game, isTop: Boolean) {
        val coverView = if (isTop) binding.gameACover else binding.gameBCover
        val nameView = if (isTop) binding.gameAName else binding.gameBName
        val genreView = if (isTop) binding.gameAGenre else binding.gameBGenre
        val yearView = if (isTop) binding.gameAYear else binding.gameBYear

        coverView.load(game.cover?.url?.toFullCoverUrl())
        nameView.text = game.name ?: "Unknown"
        genreView.text = game.genres?.joinToString(", ") { it.name ?: "" } ?: ""
        yearView.text = game.first_release_date?.let {
            Calendar.getInstance().apply { timeInMillis = it * 1000 }
                .get(Calendar.YEAR).toString()
        } ?: ""
    }

    private fun String.toFullCoverUrl(): String =
        "https:${this.replace("t_thumb", "t_cover_big")}"
}