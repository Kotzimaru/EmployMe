package ru.practicum.android.diploma.favorite.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.detail.domain.models.DetailVacancy
import ru.practicum.android.diploma.favorite.presentation.models.FavoriteStates
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface FavoriteInteractor {
    suspend fun addFavorite(favorite: DetailVacancy)
    suspend fun deleteFavorite(id: String)
    fun getFavorites(): Flow<Pair<FavoriteStates, MutableList<Vacancy>>>
    fun getFavorite(id: String): Flow<List<DetailVacancy>>
}
