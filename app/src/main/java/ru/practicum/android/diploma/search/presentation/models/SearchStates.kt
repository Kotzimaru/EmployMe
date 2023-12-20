package ru.practicum.android.diploma.search.presentation.models

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface SearchStates {
    data object Start : SearchStates
    data object Loading : SearchStates
    data object ServerError : SearchStates
    data object ConnectionError : SearchStates
    data object InvalidRequest : SearchStates
    data class Success(
        val vacancyList: List<Vacancy>,
        val page: Int,
        val found: Int,
    ) : SearchStates
}
