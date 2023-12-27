package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.impl.FilterInteractor
import ru.practicum.android.diploma.filter.presentation.states.FilterPlaceWorkStates

class FilterPlaceWorkViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FilterPlaceWorkStates>()
    fun getState(): LiveData<FilterPlaceWorkStates> = stateLiveData

    fun getCountry() {
        viewModelScope.launch {
            val country = filterInteractor.getCountryFilter()
            if (country.id.isNotEmpty()) {
                stateLiveData.postValue(FilterPlaceWorkStates.HasCountry(country))
            }
        }
    }

    fun getRegion() {
        viewModelScope.launch {
            val region = filterInteractor.getRegionFilter()
            if (region.id.isNotEmpty()) {
                stateLiveData.postValue(FilterPlaceWorkStates.HasRegion(region))
            }
        }
    }

    fun clearCountryFilter() {
        viewModelScope.launch {
            filterInteractor.deleteCountryFilter()
            stateLiveData.postValue(FilterPlaceWorkStates.ClearedCountry)
        }
    }

    fun clearRegionFilter() {
        viewModelScope.launch {
            filterInteractor.deleteRegionFilter()
            stateLiveData.postValue(FilterPlaceWorkStates.ClearedRegion)
        }
    }
}
