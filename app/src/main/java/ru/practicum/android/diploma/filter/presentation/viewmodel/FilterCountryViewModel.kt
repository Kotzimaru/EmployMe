package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.impl.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.presentation.states.FilterCountryStates
import ru.practicum.android.diploma.search.domain.api.DtoConsumer

class FilterCountryViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FilterCountryStates>()
    fun getState(): LiveData<FilterCountryStates> = stateLiveData
    fun getCountries() {
        stateLiveData.postValue(FilterCountryStates.Loading)
        viewModelScope.launch {
            filterInteractor.getCountries().collect { dto ->
                when (dto) {
                    is DtoConsumer.Error -> {
                        stateLiveData.postValue(FilterCountryStates.ServerError)
                    }
                    is DtoConsumer.NoInternet -> {
                        stateLiveData.postValue(FilterCountryStates.ConnectionError)
                    }
                    is DtoConsumer.Success -> {
                        if (dto.data.size > 0) {
                            stateLiveData.postValue(FilterCountryStates.Success(dto.data))
                        } else {
                            stateLiveData.postValue(FilterCountryStates.Empty)
                        }
                    }
                }
            }
        }
    }

    fun saveCountryFilter(country: Country) {
        viewModelScope.launch {
            filterInteractor.saveCountryFilter(country)
        }
    }
}
