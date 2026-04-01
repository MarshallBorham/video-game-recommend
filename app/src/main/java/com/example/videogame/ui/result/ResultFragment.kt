package com.example.videogame.ui.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.videogame.R
import com.example.videogame.databinding.FragmentResultBinding
import com.example.videogame.viewmodel.GameViewModel

class ResultFragment : Fragment(R.layout.fragment_result) {
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentResultBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResultBinding.bind(view)

        viewModel.recommendation.value?.let { game ->
            binding.resultName.text = game.name
            binding.resultSummary.text = game.summary
            binding.resultCover.load("https:${game.cover?.url?.replace("t_thumb", "t_cover_big")}")
        }

        binding.restartButton.setOnClickListener {
            viewModel.loadGames()
            findNavController().navigate(R.id.action_result_to_compare)
        }
    }
}