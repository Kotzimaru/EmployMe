package ru.practicum.android.diploma.detail.presentation.detail

import ru.practicum.android.diploma.detail.domain.models.DetailVacancy

sealed interface DetailState {
    data object Loading : DetailState
    data class Error(
        val message: String
    ) : DetailState

    data class NoConnect(
        val message: String
    ) : DetailState

    data class IsFavorite(
        val isFavorite: Boolean
    ) : DetailState

    data class Success(val data: DetailVacancy) : DetailState
}
