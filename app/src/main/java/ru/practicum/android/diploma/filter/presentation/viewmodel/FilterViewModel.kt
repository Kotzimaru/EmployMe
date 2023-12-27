package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.impl.FilterInteractor
import ru.practicum.android.diploma.filter.presentation.states.FilterStates

class FilterViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FilterStates>()
    fun getState(): LiveData<FilterStates> = stateLiveData

    fun getFilters() {
        viewModelScope.launch {
            val filter = filterInteractor.getFilter()
            if (filter != null) {
                stateLiveData.postValue(
                    FilterStates.HasFilters(
                        filter.salary,
                        filter.onlyWithSalary,
                        filter.industry,
                        filter.country,
                        filter.region
                    )
                )
            }
        }
    }

    fun setFilterSettings(salary: String, onlyWithSalary: Boolean) {
        viewModelScope.launch {
            filterInteractor.setFilterSettings(salary, onlyWithSalary)
            stateLiveData.postValue(FilterStates.SaveSettings)
        }
    }

    fun clearFilterSettings() {
        viewModelScope.launch {
            filterInteractor.clearFilterSettings()
            stateLiveData.postValue(FilterStates.ClearSettings)
        }
    }

    fun deleteCountryAndRegionFilter() {
        viewModelScope.launch {
            filterInteractor.deleteRegionFilter()
            filterInteractor.deleteCountryFilter()
            stateLiveData.postValue(FilterStates.DeleteCountryAndRegion)
        }
    }

    fun deleteIndustryFilter() {
        viewModelScope.launch {
            filterInteractor.deleteIndustryFilter()
            stateLiveData.postValue(FilterStates.DeleteIndustry)
        }
    }
}
