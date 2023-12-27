package ru.practicum.android.diploma.filter.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterRegionBinding
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.presentation.adapter.FilterRegionAdapter
import ru.practicum.android.diploma.filter.presentation.states.FilterRegionStates
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterRegionViewModel

class FilterRegionFragment : Fragment(R.layout.fragment_filter_region) {
    private lateinit var binding: FragmentFilterRegionBinding
    private val viewModel: FilterRegionViewModel by viewModel()
    private val adapter = FilterRegionAdapter {
        chooseRegion(it)
    }

    private fun chooseRegion(region: Region) {
        viewModel.saveRegionFilter(region)
/*        setFragmentResult(
            "region",
            bundleOf("id" to region.id,
            "name" to region.name
            ))*/
        findNavController().popBackStack()
    }

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerFilterRegion.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFilterRegion.adapter = adapter

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                FilterRegionStates.ConnectionError -> {
                    binding.recyclerFilterRegion.visibility = GONE
                    binding.pbLoading.visibility = GONE
                    binding.tvError.visibility = VISIBLE
                    binding.ivError.visibility = VISIBLE
                    binding.tvError.setText(R.string.internet_connection_issue)
                    binding.ivError.setImageResource(R.drawable.error_connection)
                }
                FilterRegionStates.Empty -> {
                    binding.recyclerFilterRegion.visibility = GONE
                    binding.pbLoading.visibility = GONE
                    binding.tvError.visibility = VISIBLE
                    binding.ivError.visibility = VISIBLE
                    binding.tvError.setText(R.string.there_is_no_such_region)
                    binding.ivError.setImageResource(R.drawable.image_error_favorite)
                }
                FilterRegionStates.Loading -> {
                    binding.pbLoading.visibility = VISIBLE
                    binding.tvError.visibility = GONE
                    binding.ivError.visibility = GONE
                }
                FilterRegionStates.ServerError -> {
                    binding.recyclerFilterRegion.visibility = GONE
                    binding.pbLoading.visibility = GONE
                    binding.tvError.visibility = VISIBLE
                    binding.ivError.visibility = VISIBLE
                    binding.tvError.setText(R.string.server_error)
                    binding.ivError.setImageResource(R.drawable.image_error_server_2)
                }
                is FilterRegionStates.Success -> {
                    binding.recyclerFilterRegion.visibility = VISIBLE
                    binding.pbLoading.visibility = GONE
                    adapter.regions.clear()
                    adapter.regions = it.regions.toMutableList()
                    adapter.notifyDataSetChanged()
                    binding.tvError.visibility = GONE
                    binding.ivError.visibility = GONE
                }
            }
        }

        viewModel.getRegions()

        initListeners()
    }

    private fun initListeners() {
        binding.etSearch.addTextChangedListener(textWatcherListener())

        binding.etSearch.setOnEditorActionListener { textView, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(SEARCH_DEBOUNCE_DELAY_MILS)

                    viewModel.getRegionsByName(binding.etSearch.text.toString())
                }
                true
            }
            false
        }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun textWatcherListener() = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!binding.etSearch.text.toString().isNullOrEmpty()) {
                binding.container.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                binding.container.endIconDrawable = requireContext().getDrawable(R.drawable.ic_clear)
                if (start != before) {
                    searchJob?.cancel()
                    searchJob = viewLifecycleOwner.lifecycleScope.launch {
                        delay(SEARCH_DEBOUNCE_DELAY_MILS)

                        viewModel.getRegionsByName(binding.etSearch.text.toString())
                    }
                }
            } else {
                binding.container.endIconMode = TextInputLayout.END_ICON_CUSTOM
                binding.container.endIconDrawable = requireContext().getDrawable(R.drawable.ic_search)
                val inputMethodManager = requireContext().getSystemService(
                    AppCompatActivity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                viewModel.getRegions()
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            //
        }
    }

    companion object {
        const val VISIBLE = View.VISIBLE
        const val GONE = View.GONE
        const val SEARCH_DEBOUNCE_DELAY_MILS = 2000L
    }
}
