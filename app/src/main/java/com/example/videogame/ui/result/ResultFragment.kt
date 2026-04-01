package com.example.videogame.ui.result

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