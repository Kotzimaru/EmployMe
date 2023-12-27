package ru.practicum.android.diploma.detail.presentation.semilar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarBinding
import ru.practicum.android.diploma.detail.domain.models.SimilarVacancy
import ru.practicum.android.diploma.detail.presentation.detail.VACANCY_ID

class SimilarFragment : Fragment() {
    private var _binding: FragmentSimilarBinding? = null
    private val viewModel: SimilarViewModel by viewModel()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        binding.btTopBarBack.setOnClickListener {
            view.findNavController().navigateUp()
        }
    }

    private fun renderState(state: SimilarState) {
        when (state) {
            is SimilarState.Success -> {
                setData(state.data)
            }

            is SimilarState.Error -> {
                setError()
            }

            is SimilarState.Loading -> {
                setLoad()
            }

            is SimilarState.Empty -> {
                setEmpty()
            }
        }
    }

    private fun setEmpty() {
        with(binding.errorPlaceholder) {
            imError.setImageResource(R.drawable.error_show_cat)
            tvError.text = getString(R.string.vacancy_mismatch)
        }
        binding.errorPlaceholder.root.isVisible = true
        binding.rvSimilar.isVisible = false
        binding.progress.isVisible = false
    }

    private fun setError() {
        binding.errorPlaceholder.root.isVisible = true
        binding.rvSimilar.isVisible = false
        binding.progress.isVisible = false
    }

    private fun setData(data: List<SimilarVacancy>) {
        binding.rvSimilar.isVisible = true
        binding.rvSimilar.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        val stateClickListener: SimilarAdapter.OnStateClickListener =
            object : SimilarAdapter.OnStateClickListener {
                override fun onStateClick(item: SimilarVacancy, position: Int) {
                    val bundle = bundleOf(VACANCY_ID to item.id)
                    view?.findNavController()
                        ?.navigate(R.id.action_similarFragment_to_detailFragment, bundle)
                }
            }
        val adapter = SimilarAdapter(data, stateClickListener)
        binding.rvSimilar.adapter = adapter
        binding.progress.isVisible = false
        binding.errorPlaceholder.root.isVisible = false
    }

    private fun setLoad() {
        binding.rvSimilar.isVisible = false
        binding.progress.isVisible = true
        binding.errorPlaceholder.root.isVisible = false
    }
}
