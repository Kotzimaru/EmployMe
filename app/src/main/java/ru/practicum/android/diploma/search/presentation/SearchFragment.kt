package ru.practicum.android.diploma.search.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.detail.presentation.detail.DetailFragment
import ru.practicum.android.diploma.search.domain.models.Filter
import ru.practicum.android.diploma.search.presentation.models.SearchStates

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private var filter: Filter = Filter(
        page = 0,
        request = "android",
        area = "",
        industry = "",
        salary = 1000,
        onlyWithSalary = false
    )
    private val viewModel: SearchViewModel by viewModel {
        parametersOf(
            filter
        )
    }
    private var searchJob: Job? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.rvSearch
        val adapter = SearchAdapter(clickOnVacancy())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchStates.Default -> {
                    binding.apply {
                        rvSearch.visibility = GONE
                        placeholderImage.setImageResource(R.drawable.image_binoculars)
                        progressBar.visibility = GONE
                        placeholderMessage.visibility = GONE
                        tvRvHeader.visibility = GONE
                    }
                }

                is SearchStates.ServerError -> {
                    binding.apply {
                        rvSearch.visibility = GONE
                        placeholderImage.setImageResource(R.drawable.image_error_server_2)
                        progressBar.visibility = GONE
                        placeholderMessage.visibility = VISIBLE
                        placeholderMessage.setText(R.string.server_error)
                        tvRvHeader.visibility = GONE
                    }
                }

                is SearchStates.ConnectionError -> {
                    binding.apply {
                        rvSearch.visibility = GONE
                        placeholderImage.visibility = VISIBLE
                        placeholderImage.setImageResource(R.drawable.image_scull)
                        progressBar.visibility = GONE
                        placeholderMessage.visibility = VISIBLE
                        placeholderMessage.setText(R.string.internet_connection_issue)
                        tvRvHeader.visibility = GONE
                    }
                }

                is SearchStates.InvalidRequest -> {
                    binding.apply {
                        rvSearch.visibility = GONE
                        placeholderImage.setImageResource(R.drawable.image_error_favorite)
                        progressBar.visibility = GONE
                        placeholderMessage.visibility = VISIBLE
                        placeholderMessage.setText(R.string.internet_connection_issue)
                        tvRvHeader.visibility = VISIBLE
                        tvRvHeader.setText(R.string.vacancy_mismatch)
                    }
                }

                is SearchStates.Success -> {
                    setSuccessScreen(state.trackList.count()) // Передать общее кол-во найденных вакансий
                    adapter.vacancyList = state.trackList.toMutableList()
                    adapter.notifyDataSetChanged()
                }

                is SearchStates.Loading -> {
                    binding.apply {
                        rvSearch.visibility = GONE
                        placeholderMessage.visibility = GONE
                        progressBar.visibility = VISIBLE
                        placeholderMessage.visibility = GONE
                        tvRvHeader.visibility = GONE
                    }
                }
            }
        }

        initListeners()
    }

    private fun setSuccessScreen(amount: Int) {
        binding.rvSearch.visibility = VISIBLE
        binding.placeholderImage.visibility = GONE
        binding.progressBar.visibility = GONE
        binding.placeholderMessage.visibility = GONE
        binding.tvRvHeader.visibility = VISIBLE
        binding.tvRvHeader.text = amount.toString()
    }

    private fun tWCreator() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            changeVisBottomNav(GONE)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val endDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)
            binding.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                endDrawable,
                null
            )

            if (start != before) {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(SEARCH_DEBOUNCE_DELAY_MILS)
                    filter.request = s?.toString() ?: ""
                    binding.placeholderImage.visibility = View.GONE
                    viewModel.loadVacancy()
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            changeVisBottomNav(VISIBLE)
        }
    }

    private fun changeVisBottomNav(state: Int) {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.visibility = state
    }

    private fun clearText() {
        binding.etSearch.setText("")
        val endDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
        binding.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            endDrawable,
            null
        )
    }

    private fun initListeners(){

        binding.etSearch.addTextChangedListener(tWCreator())

        binding.btClear.setOnClickListener {
            clearText()
        }

        binding.buttonFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }
    }

    private fun clickOnVacancy(): (String) -> Unit = { id ->

        findNavController().navigate(
            R.id.action_searchFragment_to_detailFragment,
            DetailFragment.createArgs(id)
        )
    }

    companion object {
        const val VISIBLE = View.VISIBLE
        const val GONE = View.GONE
        const val SEARCH_DEBOUNCE_DELAY_MILS = 2000L
    }
}
