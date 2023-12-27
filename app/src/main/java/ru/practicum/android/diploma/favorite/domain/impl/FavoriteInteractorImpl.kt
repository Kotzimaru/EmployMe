package ru.practicum.android.diploma.favorite.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.detail.domain.models.DetailVacancy
import ru.practicum.android.diploma.favorite.domain.FavoriteInteractor
import ru.practicum.android.diploma.favorite.domain.api.FavoriteRepository
import ru.practicum.android.diploma.favorite.presentation.models.FavoriteStates
import ru.practicum.android.diploma.search.domain.models.Vacancy

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository,
) : FavoriteInteractor {
    override suspend fun addFavorite(favorite: DetailVacancy) {
        favoriteRepository.addFavorite(favorite)
    }

    override suspend fun deleteFavorite(id: String) {
        favoriteRepository.deleteFavorite(id)
    }

    override fun getFavorites(): Flow<Pair<FavoriteStates, MutableList<Vacancy>>> {
        return favoriteRepository.getFavorites()
    }

    override fun getFavorite(id: String): Flow<List<DetailVacancy>> {
        return favoriteRepository.getFavorite(id)
    }
}
