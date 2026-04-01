package com.example.videogame.ui.compare

class CompareFragment : Fragment(R.layout.fragment_compare) {
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var binding: FragmentCompareBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCompareBinding.bind(view)

        viewModel.currentPair.observe(viewLifecycleOwner) { (gameA, gameB) ->
            binding.gameAName.text = gameA.name
            binding.gameBName.text = gameB.name
            binding.gameACover.load(gameA.cover?.url?.toFullCoverUrl())
            binding.gameBCover.load(gameB.cover?.url?.toFullCoverUrl())

            binding.gameAButton.setOnClickListener { viewModel.choose(gameA, gameB) }
            binding.gameBButton.setOnClickListener { viewModel.choose(gameB, gameA) }
        }

        viewModel.progress.observe(viewLifecycleOwner) { round ->
            binding.progressBar.progress = round
        }

        viewModel.recommendation.observe(viewLifecycleOwner) { game ->
            game?.let { findNavController().navigate(R.id.action_compare_to_result) }
        }
    }

    private fun String.toFullCoverUrl(): String =
        "https:${this.replace("t_thumb", "t_cover_big")}"
}