package ru.practicum.android.diploma.favorite.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.detail.domain.models.DetailVacancy
import ru.practicum.android.diploma.favorite.domain.api.FavoriteRepository
import ru.practicum.android.diploma.favorite.presentation.models.FavoriteStates
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.storage.db.AppDatabase
import ru.practicum.android.diploma.util.storage.db.converter.FavoriteDbConverter

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteDbConverter: FavoriteDbConverter,
) : FavoriteRepository {
    override suspend fun addFavorite(favorite: DetailVacancy) {
        appDatabase.favoriteDao().addFavorite(favoriteDbConverter.map(favorite))
    }

    override suspend fun deleteFavorite(id: String) {
        appDatabase.favoriteDao().deleteFavorite(id)
    }

    override fun getFavorites(): Flow<Pair<FavoriteStates, MutableList<Vacancy>>> = flow {
        try {
            val favorites = appDatabase.favoriteDao().getFavorites()
            if (favorites.isEmpty()) {
                emit(Pair(FavoriteStates.Empty, mutableListOf()))
            } else {
                val mappedFavorites = ArrayList<Vacancy>()
                favorites.forEach {
                    mappedFavorites.add(favoriteDbConverter.map2(it))
                }
                emit(Pair(FavoriteStates.Success, mappedFavorites.toMutableList()))
            }
        } catch (e: Exception) {
            emit(Pair(FavoriteStates.Error, mutableListOf()))
        }
    }

    override fun getFavorite(id: String): Flow<List<DetailVacancy>> = flow {
        val favorites = appDatabase.favoriteDao().getFavorite(id)
        emit(
            favorites.map {
                    favorite ->
                favoriteDbConverter.map(favorite)
            }
        )
    }
}
