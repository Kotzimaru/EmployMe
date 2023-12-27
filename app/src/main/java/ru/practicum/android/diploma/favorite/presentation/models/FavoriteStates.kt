package ru.practicum.android.diploma.favorite.presentation.models

sealed interface FavoriteStates {
    object Empty : FavoriteStates
    object Error : FavoriteStates
    object Success : FavoriteStates
}
