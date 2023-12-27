package ru.practicum.android.diploma.detail.presentation.semilar

import ru.practicum.android.diploma.detail.domain.models.SimilarVacancy

interface SimilarState {
    data object Loading : SimilarState
    object Empty : SimilarState
    data class Error(
        val message: String
    ) : SimilarState

    data class NoConnect(
        val message: String
    ) : SimilarState

    data class Success(val data: List<SimilarVacancy>) : SimilarState
}
