package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.impl.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.presentation.states.FilterRegionStates
import ru.practicum.android.diploma.search.domain.api.DtoConsumer

class FilterRegionViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FilterRegionStates>()
    fun getState(): LiveData<FilterRegionStates> = stateLiveData
    fun saveRegionFilter(region: Region) {
        viewModelScope.launch {
            filterInteractor.saveRegionFilter(region)
        }
    }

    fun getRegions() {
        stateLiveData.postValue(FilterRegionStates.Loading)
        viewModelScope.launch {
            filterInteractor.getRegions().collect { dto ->
                postRegion(dto)
            }
        }
    }

    private fun postRegion(dto: DtoConsumer<List<Region>>) {
        when (dto) {
            is DtoConsumer.Error -> {
                stateLiveData.postValue(FilterRegionStates.ServerError)
            }
            is DtoConsumer.NoInternet -> {
                stateLiveData.postValue(FilterRegionStates.ConnectionError)
            }
            is DtoConsumer.Success -> {
                if (dto.data.size > 0) {
                    stateLiveData.postValue(FilterRegionStates.Success(dto.data))
                } else {
                    stateLiveData.postValue(FilterRegionStates.Empty)
                }
            }
        }
    }

    fun getRegionsByName(name: String) {
        stateLiveData.postValue(FilterRegionStates.Loading)
        viewModelScope.launch {
            filterInteractor.getRegionsByName(name).collect { dto ->
                postRegion(dto)
            }
        }
    }
}
