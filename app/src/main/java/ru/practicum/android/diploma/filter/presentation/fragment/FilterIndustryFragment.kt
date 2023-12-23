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
import androidx.core.content.ContextCompat
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
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.filter.domain.models.FilterIndustryStates
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.presentation.adapter.FilterIndustryAdapter
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterIndustryViewModel
import ru.practicum.android.diploma.search.presentation.SearchFragment

class FilterIndustryFragment : Fragment(R.layout.fragment_filter_industry) {
    private lateinit var binding: FragmentFilterIndustryBinding
    private val viewModel: FilterIndustryViewModel by viewModel()
    private val adapter = FilterIndustryAdapter {
        chooseIndustry(it)
    }
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.recyclerFilterIndustry.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFilterIndustry.adapter = adapter

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                FilterIndustryStates.ConnectionError -> {}
                FilterIndustryStates.Loading -> {}
                FilterIndustryStates.ServerError -> {}
                is FilterIndustryStates.Success -> {
                    adapter.industries.clear()
                    adapter.industries = it.industries.toMutableList()
                    adapter.notifyDataSetChanged()
                    viewModel.isChecked()
                }

                FilterIndustryStates.HasSelected -> {
                    binding.btnChoose.visibility = VISIBLE
                }
            }
        }

        viewModel.getIndustries()

        initListeners()
    }

    private fun initListeners() {
        binding.etSearch.addTextChangedListener(textWatcherListener())

        binding.etSearch.setOnEditorActionListener { textView, action, keyEvent  ->
            if(action == EditorInfo.IME_ACTION_DONE){
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(SEARCH_DEBOUNCE_DELAY_MILS)

                    viewModel.getIndustriesByName(binding.etSearch.text.toString())
                }
                true
            }
            false
        }

        binding.btnChoose.setOnClickListener {
            viewModel.saveIndustryFilter()
            findNavController().popBackStack()
        }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun textWatcherListener() = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!binding.etSearch.text.toString().isNullOrEmpty()){
                binding.container.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                binding.container.endIconDrawable = requireContext().getDrawable(R.drawable.ic_clear)
                if (start != before) {
                    searchJob?.cancel()
                    searchJob = viewLifecycleOwner.lifecycleScope.launch {
                        delay(SEARCH_DEBOUNCE_DELAY_MILS)

                        viewModel.getIndustriesByName(binding.etSearch.text.toString())
                    }
                }
            } else {
                binding.container.endIconMode = TextInputLayout.END_ICON_CUSTOM
                binding.container.endIconDrawable = requireContext().getDrawable(R.drawable.ic_search)
                val inputMethodManager = requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                viewModel.getIndustries()
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            //
        }

    }

    private fun chooseIndustry(industry : Industry){
        viewModel.bufferIndustry(industry)
    }

    companion object {
        const val VISIBLE = View.VISIBLE
        const val GONE = View.GONE
        const val SEARCH_DEBOUNCE_DELAY_MILS = 2000L
    }
}
