package ru.practicum.android.diploma.favorite.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorite.domain.FavoriteInteractor
import ru.practicum.android.diploma.favorite.presentation.models.FavoriteStates
import ru.practicum.android.diploma.search.domain.models.Vacancy

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<Pair<FavoriteStates, MutableList<Vacancy>>>()

    fun getState(): LiveData<Pair<FavoriteStates, MutableList<Vacancy>>> = stateLiveData

    fun loadFavorites() {
        viewModelScope.launch {
            favoriteInteractor.getFavorites().collect {
                stateLiveData.value = it
            }
        }
    }
}
