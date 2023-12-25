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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.detail.presentation.detail.DetailFragment
import ru.practicum.android.diploma.search.presentation.models.SearchStates
import ru.practicum.android.diploma.util.TextUtils

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var adapter: SearchAdapter? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
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
                is SearchStates.Start -> {
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
                    setSuccessScreen(state.found)
                    adapter.vacancyList = state.vacancyList.toMutableList()
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

        binding.etSearch.addTextChangedListener(tWCreator())

        binding.btClear.setOnClickListener {
            clearText()
        }
        binding.rvSearch.addOnScrollListener(initScrlLsnr())
    }

    private fun setSuccessScreen(amount: Int) {
        binding.rvSearch.visibility = VISIBLE
        binding.placeholderImage.visibility = GONE
        binding.progressBar.visibility = GONE
        binding.placeholderMessage.visibility = GONE
        binding.tvRvHeader.visibility = VISIBLE
        binding.tvRvHeader.text = getString(R.string.founded, TextUtils.addSeparator(amount))
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
                viewModel.loadVacancy(s?.toString() ?: "")
            }
        }

        override fun afterTextChanged(s: Editable?) {
            changeVisBottomNav(VISIBLE)
        }
    }

    private fun initScrlLsnr() = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val pos =
                (binding.rvSearch.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            val itemsCount = adapter?.itemCount
            if (itemsCount != null) {
                if (pos >= itemsCount - 1) {
                    viewModel.getNewPage()
                }
            }
        }
    }

    private fun changeVisBottomNav(state: Int) {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.visibility = state
    }

    private fun clearText() {
        binding.etSearch.setText("")
        viewModel.clearAll()
        val endDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_search)
        binding.etSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            endDrawable,
            null
        )
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
    }
}
