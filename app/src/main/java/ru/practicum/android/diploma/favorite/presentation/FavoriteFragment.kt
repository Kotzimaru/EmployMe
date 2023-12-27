package ru.practicum.android.diploma.favorite.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding
import ru.practicum.android.diploma.detail.presentation.detail.DetailFragment
import ru.practicum.android.diploma.favorite.presentation.models.FavoriteStates

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        val adapter = FavoriteAdapter(clickOnVacancy())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state.first) {
                FavoriteStates.Empty -> {
                    binding.apply {
                        placeholderEmpty.visibility = View.VISIBLE
                        placeholderError.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                    }
                }
                FavoriteStates.Error -> {
                    binding.apply {
                        placeholderEmpty.visibility = View.GONE
                        placeholderError.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
                is FavoriteStates.Success -> {
                    binding.apply {
                        placeholderEmpty.visibility = View.GONE
                        placeholderError.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adapter.vacancyList = state.second
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        viewModel.loadFavorites()
    }

    private fun clickOnVacancy(): (String) -> Unit = { id ->

        findNavController().navigate(
            R.id.action_favoriteFragment_to_detailFragment,
            DetailFragment.createArgs(id)
        )
    }
}
