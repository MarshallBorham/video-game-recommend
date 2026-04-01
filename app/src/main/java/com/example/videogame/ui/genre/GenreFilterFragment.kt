package com.example.videogame.ui.genre

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videogame.R
import com.example.videogame.databinding.FragmentGenreFilterBinding
import com.example.videogame.viewmodel.GameViewModel

class GenreFilterFragment : Fragment(R.layout.fragment_genre_filter) {
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentGenreFilterBinding
    private var selectedGenres = emptySet<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGenreFilterBinding.bind(view)

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.genreProgress.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.availableGenres.observe(viewLifecycleOwner) { genres ->
            if (genres.isNotEmpty()) {
                binding.genreRecycler.visibility = View.VISIBLE
                binding.startButton.isEnabled = true
                binding.genreRecycler.layoutManager = LinearLayoutManager(requireContext())
                binding.genreRecycler.adapter = GenreAdapter(genres) { selected ->
                    selectedGenres = selected
                }
            }
        }

        binding.startButton.setOnClickListener {
            viewModel.startWithGenres(selectedGenres)
            findNavController().navigate(R.id.action_genre_to_compare)
        }

        viewModel.loadGames()
    }
}