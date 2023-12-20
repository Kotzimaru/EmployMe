package ru.practicum.android.diploma.detail.presentation.semilar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.detail.domain.SimilarInteractor
import ru.practicum.android.diploma.detail.presentation.similar.SimilarState
import ru.practicum.android.diploma.detail.presentation.similar.SimilarState.Loading
import ru.practicum.android.diploma.detail.presentation.similar.SimilarState.NoConnect
import ru.practicum.android.diploma.search.domain.models.ResponseCodes

const val VACANCY = "vacancy"

class SimilarViewModel(
    private val similarInteractor: SimilarInteractor,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableLiveData<SimilarState>(Loading)
    val state = _state

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>(VACANCY) ?: return@launch
            val resultData = similarInteractor.execute(vacancy = id)
            when (resultData.responseCodes) {
                ResponseCodes.ERROR -> {
                    _state.value = SimilarState.Error(resultData.responseCodes.name)
                }

                ResponseCodes.SUCCESS -> {
                    Log.d("TAG result", "result - ${resultData.vacancies}")
                    if (resultData.vacancies?.isEmpty() == true) {
                        _state.value = SimilarState.Empty
                    } else {
                        _state.value = SimilarState.Success(resultData.vacancies ?: emptyList())
                    }
                }

                ResponseCodes.NO_NET_CONNECTION -> {
                    _state.value = NoConnect(resultData.responseCodes.name)
                }

                else -> {}
            }

        }
    }
}
