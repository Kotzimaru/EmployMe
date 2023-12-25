package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.Filter
import ru.practicum.android.diploma.search.domain.models.ResponseCodes
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyInfo
import ru.practicum.android.diploma.search.presentation.models.SearchStates
import ru.practicum.android.diploma.util.createDebounceFunction

class SearchViewModel(
    private val filter: Filter,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val vacancyList = mutableListOf<Vacancy>()
    private var state: SearchStates = SearchStates.Start
    private val stateLiveData = MutableLiveData(state)
    private var searchJob: Job? = null
    private var page = 0
    private var maxPage = 0
    private var founded = 0
    private val searchDebounce =
        createDebounceFunction<String>(SEARCH_DEBOUNCE_DELAY_MILS, viewModelScope, true) {
            filter.request = it
            search()
        }
    private val pageLoaderDebounce =
        createDebounceFunction<Unit>(PAGE_LOAD_DEBOUNCE_DELAY_MILS, viewModelScope, true) {
            search()
        }

    fun loadVacancy(text: String) {
        if (text.isBlank() || text == filter.request) return
        searchDebounce(text)
    }

    private fun search() {
        stateLiveData.value = SearchStates.Loading
        viewModelScope.launch {
            searchInteractor.execute(filter = filter).collect { jobsInfo ->
                requestHandler(jobsInfo)
            }
        }
    }
    fun getState(): LiveData<SearchStates> = stateLiveData

    fun getNewPage() {
        if (page < maxPage - 1) {
            filter.page = page + 1
            pageLoaderDebounce(Unit)
        }
    }

    fun clearAll() {
        stateLiveData.value = SearchStates.Start
        vacancyList.clear()
        filter.request = ""
        searchJob?.cancel()
        maxPage = 0
        page = 0
    }

    private fun requestHandler(jobsInfo: VacancyInfo) {
        when (jobsInfo.responseCodes) {
            ResponseCodes.ERROR -> {
                stateLiveData.value = SearchStates.ServerError
            }

            ResponseCodes.SUCCESS -> {
                vacancyList.addAll(jobsInfo.vacancy!!)
                page = jobsInfo.page
                maxPage = jobsInfo.pages
                if (page == 0) {
                    founded = jobsInfo.found
                }

                state = jobsInfo.let {
                    SearchStates.Success(
                        vacancyList = vacancyList,
                        page = it.page,
                        found = founded
                    )
                }
                stateLiveData.value = state
            }

            ResponseCodes.NO_NET_CONNECTION -> {
                state = SearchStates.ConnectionError
                stateLiveData.value = state
            }

            ResponseCodes.NO_RESULTS -> {
                state = SearchStates.InvalidRequest
                stateLiveData.value = state
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        searchJob = null
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY_MILS = 2000L
        const val PAGE_LOAD_DEBOUNCE_DELAY_MILS = 100L
    }
}
