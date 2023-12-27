package ru.practicum.android.diploma.filter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterCountryBinding
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.presentation.adapter.FilterCountryAdapter
import ru.practicum.android.diploma.filter.presentation.states.FilterCountryStates
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterCountryViewModel

class FilterCountryFragment : Fragment(R.layout.fragment_filter_country) {
    private lateinit var binding: FragmentFilterCountryBinding
    private val viewModel: FilterCountryViewModel by viewModel()
    private val adapter = FilterCountryAdapter {
        chooseCountry(it)
    }

    private fun chooseCountry(country: Country) {
        viewModel.saveCountryFilter(country)
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerFilterCountry.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFilterCountry.adapter = adapter

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                FilterCountryStates.ConnectionError -> {
                    setConnectionErrorScreen()
                }
                FilterCountryStates.Empty -> {
                    setEmptyScreen()
                }
                FilterCountryStates.Loading -> {
                    setLoadingScreen()
                }
                FilterCountryStates.ServerError -> {
                    setServerErrorScreen()
                }
                is FilterCountryStates.Success -> {
                    setSuccessScreen(it)
                }
            }
        }

        viewModel.getCountries()

        initListeners()
    }

    private fun setSuccessScreen(it: FilterCountryStates.Success) {
        binding.recyclerFilterCountry.visibility = VISIBLE
        binding.pbLoading.visibility = GONE
        adapter.countries.clear()
        adapter.countries = it.countries.toMutableList()
        adapter.notifyDataSetChanged()
        binding.tvError.visibility = GONE
        binding.ivError.visibility = GONE
    }

    private fun setServerErrorScreen() {
        binding.recyclerFilterCountry.visibility = GONE
        binding.pbLoading.visibility = GONE
        binding.tvError.visibility = VISIBLE
        binding.ivError.visibility = VISIBLE
        binding.tvError.setText(R.string.server_error)
        binding.ivError.setImageResource(R.drawable.image_error_server_2)
    }

    private fun setLoadingScreen() {
        binding.pbLoading.visibility = VISIBLE
        binding.tvError.visibility = GONE
        binding.ivError.visibility = GONE
    }

    private fun setEmptyScreen() {
        binding.recyclerFilterCountry.visibility = GONE
        binding.pbLoading.visibility = GONE
        binding.tvError.visibility = VISIBLE
        binding.ivError.visibility = VISIBLE
        binding.tvError.setText(R.string.there_is_no_such_country)
        binding.ivError.setImageResource(R.drawable.image_error_favorite)
    }

    private fun setConnectionErrorScreen() {
        binding.recyclerFilterCountry.visibility = GONE
        binding.pbLoading.visibility = GONE
        binding.tvError.visibility = VISIBLE
        binding.ivError.visibility = VISIBLE
        binding.tvError.setText(R.string.internet_connection_issue)
        binding.ivError.setImageResource(R.drawable.error_connection)
    }

    private fun initListeners() {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val VISIBLE = View.VISIBLE
        const val GONE = View.GONE
    }
}
