package ru.practicum.android.diploma.search.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.detail.presentation.detail.DetailFragment
import ru.practicum.android.diploma.search.presentation.models.SearchStates

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()
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

    override fun onResume() {
        super.onResume()
        viewModel.hasFilter()
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
                    setDefaultScreen()
                }

                is SearchStates.ServerError -> {
                    setServerErrorScreen()
                }

                is SearchStates.ConnectionError -> {
                    setConnectionErrorScreen()
                }

                is SearchStates.InvalidRequest -> {
                    setInvalidRequestScreen()
                }

                is SearchStates.Success -> {
                    setSuccessScreen(state, adapter)
                }

                is SearchStates.Loading -> {
                    setLoadingScreen()
                }

                is SearchStates.HasFilter -> {
                    setHasFilterScreen(state)
                }
            }
        }
        initListeners()
    }

    private fun setSuccessScreen(
        state: SearchStates.Success,
        adapter: SearchAdapter
    ) {
        setSuccessScreen(state.found) // Передать общее кол-во найденных вакансий
        adapter.vacancyList = state.vacancyList.toMutableList()
        adapter.notifyDataSetChanged()
    }

    private fun setHasFilterScreen(state: SearchStates.HasFilter) {
        if (state.hasFilter) {
            binding.ivFilter.setImageResource(R.drawable.filter_blue)
        } else {
            binding.ivFilter.setImageResource(R.drawable.ic_filter)
        }
    }

    private fun setLoadingScreen() {
        binding.apply {
            rvSearch.visibility = GONE
            placeholderMessage.visibility = GONE
            progressBar.visibility = VISIBLE
            placeholderMessage.visibility = GONE
            tvRvHeader.visibility = GONE
        }
    }

    private fun setInvalidRequestScreen() {
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

    private fun setConnectionErrorScreen() {
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

    private fun setServerErrorScreen() {
        binding.apply {
            rvSearch.visibility = GONE
            placeholderImage.setImageResource(R.drawable.image_error_server_2)
            progressBar.visibility = GONE
            placeholderMessage.visibility = VISIBLE
            placeholderMessage.setText(R.string.server_error)
            tvRvHeader.visibility = GONE
        }
    }

    private fun setDefaultScreen() {
        binding.apply {
            rvSearch.visibility = GONE
            placeholderImage.setImageResource(R.drawable.image_binoculars)
            progressBar.visibility = GONE
            placeholderMessage.visibility = GONE
            tvRvHeader.visibility = GONE
        }
        viewModel.hasFilter()
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
            if (!binding.etSearch.text.toString().isNullOrEmpty()) {
                binding.container.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                binding.container.endIconDrawable = requireContext().getDrawable(R.drawable.ic_clear)
                if (start != before || count > 0) {
                    searchJob?.cancel()
                    searchJob = viewLifecycleOwner.lifecycleScope.launch {
                        delay(SEARCH_DEBOUNCE_DELAY_MILS)

                        binding.placeholderImage.visibility = GONE
                        viewModel.loadVacancy(binding.etSearch.text.toString())
                    }
                }
            } else {
                binding.container.endIconMode = TextInputLayout.END_ICON_CUSTOM
                binding.container.endIconDrawable = requireContext().getDrawable(R.drawable.ic_search)
                val inputMethodManager = requireContext().getSystemService(
                    AppCompatActivity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                binding.placeholderImage.setImageResource(R.drawable.image_binoculars)
                binding.placeholderImage.visibility = VISIBLE
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
        viewModel.clearAll()
    }

    private fun initListeners() {
        binding.etSearch.addTextChangedListener(tWCreator())

        binding.btClear.setOnClickListener {
            clearText()
        }

        binding.buttonFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }

        binding.rvSearch.addOnScrollListener(initScrlLsnr())
    }

    private fun initScrlLsnr() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val pos =
                (binding.rvSearch.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            val itemsCount = recyclerView.adapter?.itemCount ?: 0
            if (pos >= itemsCount - 1) {
                viewModel.getNewPage()
            }
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
