package ru.practicum.android.diploma.filter.presentation.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.presentation.states.FilterStates
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel

class FilterFragment : Fragment(R.layout.fragment_filter) {
    private lateinit var binding: FragmentFilterBinding
    private val viewModel: FilterViewModel by viewModel()

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is FilterStates.HasFilters -> {
                    setHasFilterScreen(it)
                }

                FilterStates.ClearSettings -> {
                    setClearSettingsScreen()
                }

                FilterStates.SaveSettings -> {
                    findNavController().popBackStack()
                }

                FilterStates.DeleteCountryAndRegion -> {
                    setDeleteCountryAndRegionScreen()
                }

                FilterStates.DeleteIndustry -> {
                    setDeleteIndustryScreen()
                }
            }
        }

        initListeners()

        viewModel.getFilters()
    }

    private fun setDeleteIndustryScreen() {
        binding.btnChoose.visibility = GONE
        binding.btnRemove.visibility = GONE
        binding.industryEditText.setText("")
        binding.industryClear.visibility = GONE
        binding.industryButton.visibility = VISIBLE
        viewModel.getFilters()
    }

    private fun setDeleteCountryAndRegionScreen() {
        binding.btnChoose.visibility = GONE
        binding.btnRemove.visibility = GONE
        binding.placeOfWorkEditText.setText("")
        binding.placeOfWorkClear.visibility = GONE
        binding.placeOfWorkButton.visibility = VISIBLE
        viewModel.getFilters()
    }

    private fun setClearSettingsScreen() {
        binding.btnChoose.visibility = GONE
        binding.btnRemove.visibility = GONE
        binding.placeOfWorkEditText.setText("")
        binding.industryEditText.setText("")
        binding.placeOfWorkClear.visibility = GONE
        binding.placeOfWorkButton.visibility = VISIBLE
        binding.industryClear.visibility = GONE
        binding.industryButton.visibility = VISIBLE
        binding.salaryEditText.setText("")
        binding.filterCheckbox.isChecked = false
    }

    private fun setHasFilterScreen(it: FilterStates.HasFilters) {
        binding.btnChoose.visibility = VISIBLE
        binding.btnRemove.visibility = VISIBLE
        binding.salaryEditText.setText(it.salary)
        binding.filterCheckbox.isChecked = it.onlyWithSalary

        if (it.country.name.isNotEmpty()) {
            binding.placeOfWorkEditText.setText(it.country.name)
            binding.placeOfWorkButton.visibility = GONE
            binding.placeOfWorkClear.visibility = VISIBLE
        } else {
            binding.placeOfWorkEditText.setText("")
            binding.placeOfWorkButton.visibility = VISIBLE
            binding.placeOfWorkClear.visibility = GONE
        }

        if (it.country.name.isNotEmpty() && it.region.name.isNotEmpty()) {
            binding.placeOfWorkEditText.setText("${it.country.name}, ${it.region.name}")
            binding.placeOfWorkButton.visibility = GONE
            binding.placeOfWorkClear.visibility = VISIBLE
        }

        if (it.industry.name.isNotEmpty()) {
            binding.industryEditText.setText(it.industry.name)
            binding.industryButton.visibility = GONE
            binding.industryClear.visibility = VISIBLE
        } else {
            binding.industryEditText.setText("")
            binding.industryButton.visibility = VISIBLE
            binding.industryClear.visibility = GONE
        }
    }

    private fun initListeners() {
        binding.placeOfWorkButton.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_filterPlaceWorkFragment)
        }

        binding.industryButton.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_filterIndustryFragment)
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnChoose.setOnClickListener {
            viewModel.setFilterSettings(binding.salaryEditText.text.toString(), binding.filterCheckbox.isChecked)
        }

        binding.btnRemove.setOnClickListener {
            viewModel.clearFilterSettings()
        }

        binding.placeOfWorkClear.setOnClickListener {
            viewModel.deleteCountryAndRegionFilter()
        }

        binding.industryClear.setOnClickListener {
            viewModel.deleteIndustryFilter()
        }
    }

    companion object {
        const val VISIBLE = View.VISIBLE
        const val GONE = View.GONE
    }
}
