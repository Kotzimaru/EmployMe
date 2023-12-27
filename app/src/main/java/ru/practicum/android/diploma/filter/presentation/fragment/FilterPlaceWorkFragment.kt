package ru.practicum.android.diploma.filter.presentation.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterPlaceWorkBinding
import ru.practicum.android.diploma.filter.presentation.states.FilterPlaceWorkStates
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterPlaceWorkViewModel

class FilterPlaceWorkFragment : Fragment(R.layout.fragment_filter_place_work) {
    private lateinit var binding: FragmentFilterPlaceWorkBinding
    private val viewModel: FilterPlaceWorkViewModel by viewModel()

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterPlaceWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is FilterPlaceWorkStates.HasCountry -> {
                    binding.countryEditText.setText(it.country.name)
                    binding.chooseCountryBottom.visibility = GONE
                    binding.clearCountryName.visibility = VISIBLE
                    viewModel.getRegion()
                    setVisibilityForChooseBtn()
                }
                is FilterPlaceWorkStates.HasRegion -> {
                    binding.regionEditText.setText(it.region.name)
                    binding.regionButton.visibility = GONE
                    binding.clearRegion.visibility = VISIBLE
                    setVisibilityForChooseBtn()
                }
                is FilterPlaceWorkStates.ClearedCountry -> {
                    binding.countryEditText.setText("")
                    binding.chooseCountryBottom.visibility = VISIBLE
                    binding.clearCountryName.visibility = GONE
                    viewModel.clearRegionFilter()
                    setVisibilityForChooseBtn()
                }
                is FilterPlaceWorkStates.ClearedRegion -> {
                    binding.regionEditText.setText("")
                    binding.regionButton.visibility = VISIBLE
                    binding.clearRegion.visibility = GONE
                    setVisibilityForChooseBtn()
                }

                else -> {
                }
            }
        }

        initListeners()

        viewModel.getCountry()
    }

    private fun setVisibilityForChooseBtn() {
        if (binding.clearRegion.isVisible || binding.clearCountryName.isVisible) {
            binding.btnChoose.visibility = VISIBLE
        } else {
            binding.btnChoose.visibility = GONE
        }
    }

    private fun initListeners() {
        binding.chooseCountryBottom.setOnClickListener {
            findNavController().navigate(R.id.action_filterPlaceWorkFragment_to_filterCountryFragment)
        }

        binding.regionButton.setOnClickListener {
            findNavController().navigate(R.id.action_filterPlaceWorkFragment_to_filterRegionFragment)
        }

        binding.clearCountryName.setOnClickListener {
            viewModel.clearCountryFilter()
        }

        binding.clearRegion.setOnClickListener {
            viewModel.clearRegionFilter()
        }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnChoose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val VISIBLE = View.VISIBLE
        const val GONE = View.GONE
    }
}
