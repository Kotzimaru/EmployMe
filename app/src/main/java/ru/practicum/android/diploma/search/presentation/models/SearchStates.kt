package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface SearchStates {
    object Default : SearchStates
    object Loading : SearchStates
    object ServerError : SearchStates
    object ConnectionError : SearchStates
    object InvalidRequest : SearchStates
    data class Success(val trackList: List<Vacancy>) : SearchStates
}
