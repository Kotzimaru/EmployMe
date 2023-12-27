package ru.practicum.android.diploma.detail.domain.impl

import android.util.Log
import ru.practicum.android.diploma.detail.domain.DetailVacancyInteractor
import ru.practicum.android.diploma.detail.domain.api.VacancyDetailRepository
import ru.practicum.android.diploma.detail.domain.models.DetailVacancyInfo
import ru.practicum.android.diploma.search.domain.api.DtoConsumer
import ru.practicum.android.diploma.search.domain.models.ResponseCodes

class DetailVacancyInteractorImpl(private val repository: VacancyDetailRepository) : DetailVacancyInteractor {
    override suspend fun execute(idVacancy: String): DetailVacancyInfo {
        return when (val result = repository.doRequest(idVacancy = idVacancy)) {
            is DtoConsumer.Success -> {
                Log.d("TAG result", "result - ${result.data}")
                DetailVacancyInfo(
                    responseCodes = ResponseCodes.SUCCESS,
                    detailVacancy = result.data
                )
            }

            is DtoConsumer.Error -> {
                Log.d("TAG result", "result - ${result.errorCode}")
                DetailVacancyInfo(
                    responseCodes = ResponseCodes.ERROR,
                    detailVacancy = null
                )
            }

            is DtoConsumer.NoInternet -> {
                Log.d("TAG result", "result - ${result.errorCode}")
                DetailVacancyInfo(
                    responseCodes = ResponseCodes.NO_NET_CONNECTION,
                    detailVacancy = null
                )
            }
        }
    }
}
