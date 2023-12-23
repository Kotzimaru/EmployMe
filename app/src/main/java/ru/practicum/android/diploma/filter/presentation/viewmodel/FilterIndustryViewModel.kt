package ru.practicum.android.diploma.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.impl.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.FilterIndustryStates
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.search.domain.api.DtoConsumer

class FilterIndustryViewModel (
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private var selectedIndustry: Industry? = null
    private val stateLiveData = MutableLiveData<FilterIndustryStates>()
    fun getState(): LiveData<FilterIndustryStates> = stateLiveData

    fun getIndustries() {
        viewModelScope.launch {
            filterInteractor.getIndustries().collect{ dto ->
                postIndustry(dto)
            }
        }
    }

    fun getIndustriesByName(industry: String) {
        viewModelScope.launch {
            filterInteractor.getIndustriesByName(industry).collect{ dto ->
                postIndustry(dto)
            }
        }
    }

    fun isChecked(){
        viewModelScope.launch {
            if(filterInteractor.getIndustryFilter().id.isNotEmpty() ){
                stateLiveData.postValue(FilterIndustryStates.HasSelected)
            }
        }
    }

    private fun postIndustry(dto: DtoConsumer<List<Industry>>){
        when (dto) {
            is DtoConsumer.Error -> {
                stateLiveData.postValue(FilterIndustryStates.ServerError)
            }
            is DtoConsumer.NoInternet -> {
                stateLiveData.postValue(FilterIndustryStates.ConnectionError)
            }
            is DtoConsumer.Success -> {
                if(dto.data.any {
                        it.isChecked
                    }){
                    stateLiveData.postValue(FilterIndustryStates.HasSelected)
                }
                stateLiveData.postValue(FilterIndustryStates.Success(dto.data))
            }
        }
    }

    fun bufferIndustry(industry: Industry) {
        selectedIndustry = industry
        stateLiveData.postValue(FilterIndustryStates.HasSelected)
    }

    fun saveIndustryFilter() {
        viewModelScope.launch {
            filterInteractor.saveIndustryFilter(selectedIndustry!!)
        }

    }

}
