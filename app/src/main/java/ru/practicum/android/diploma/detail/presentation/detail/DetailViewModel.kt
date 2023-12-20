package ru.practicum.android.diploma.detail.presentation.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.detail.domain.DetailVacancyInteractor
import ru.practicum.android.diploma.detail.domain.models.DetailVacancy
import ru.practicum.android.diploma.detail.presentation.detail.DetailState.Error
import ru.practicum.android.diploma.detail.presentation.detail.DetailState.Loading
import ru.practicum.android.diploma.detail.presentation.detail.DetailState.NoConnect
import ru.practicum.android.diploma.detail.presentation.detail.DetailState.Success
import ru.practicum.android.diploma.favorite.domain.FavoriteInteractor
import ru.practicum.android.diploma.search.domain.models.ResponseCodes

const val VACANCY_ID = "id"

class DetailViewModel(
    private val id: String,
    private val detailsInterActor: DetailVacancyInteractor,
    private val favoriteInterActor: FavoriteInteractor,
) : ViewModel() {
    private val _state = MutableLiveData<DetailState>(Loading)
    val state = _state

    init {
        getData()
    }

    fun onFavoriteClick(vacancy: DetailVacancy, setFavorite: Boolean){
        viewModelScope.launch {
            if(setFavorite){
                favoriteInterActor.addFavorite(vacancy)
                _state.value = DetailState.IsFavorite(true)
            } else {
                favoriteInterActor.deleteFavorite(id)
                _state.value = DetailState.IsFavorite(false)
            }
        }
    }

    fun isFavorite(id: String){
        viewModelScope.launch {
            favoriteInterActor.getFavorite(id).collect{
                if (it.isEmpty()){
                    _state.value = DetailState.IsFavorite(false)
                } else {
                    _state.value = DetailState.IsFavorite(true)
                }
            }
        }
    }

    private fun getVacancyFromDB(id: String){
        _state.value = Loading
        viewModelScope.launch {
            favoriteInterActor.getFavorite(id).collect{
                if (it.isEmpty()){
                    _state.value = NoConnect(ResponseCodes.NO_NET_CONNECTION.name)
                } else {
                    _state.value = Success(it[0])
                }
            }
        }
    }

    private fun getData() {
        viewModelScope.launch {
            //val id = savedStateHandle.get<String>(VACANCY_ID) ?: return@launch
            val resultData = detailsInterActor.execute(id)
            when (resultData.responseCodes) {
                ResponseCodes.ERROR -> {
                    _state.value = Error(resultData.responseCodes.name)
                }

                ResponseCodes.SUCCESS -> {
                    Log.d("TAG result", "result - ${resultData.detailVacancy}")
                    _state.value = resultData.detailVacancy?.let { Success(it) }
                }

                ResponseCodes.NO_NET_CONNECTION -> {
                    getVacancyFromDB(id)
                    //_state.value = NoConnect(resultData.responseCodes.name)
                }

                else -> {}
            }

        }
    }


}
