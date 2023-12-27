package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface SearchStates {
    object Default : SearchStates
    object Loading : SearchStates
    object ServerError : SearchStates
    object ConnectionError : SearchStates
    object InvalidRequest : SearchStates
    data class Success(val vacancyList: List<Vacancy>, val found: Int) : SearchStates
    data class HasFilter(val hasFilter: Boolean) : SearchStates
}
