package ru.practicum.android.diploma.detail.data

import ru.practicum.android.diploma.detail.data.dto.DetailVacancyDto
import ru.practicum.android.diploma.detail.data.mapper.mapToDetailVacancy
import ru.practicum.android.diploma.detail.data.models.DetailRequest
import ru.practicum.android.diploma.detail.domain.api.VacancyDetailRepository
import ru.practicum.android.diploma.detail.domain.models.DetailVacancy
import ru.practicum.android.diploma.search.data.models.ResponseCodes
import ru.practicum.android.diploma.search.domain.api.DtoConsumer
import ru.practicum.android.diploma.util.network.NetworkClient

class VacancyDetailRepositoryImpl(private val networkClient: NetworkClient) : VacancyDetailRepository {

    override suspend fun doRequest(idVacancy: String): DtoConsumer<DetailVacancy> {
        val response = networkClient.doRequest(DetailRequest(idVacancy))
        return when (response.resultCode) {
            ResponseCodes.SUCCESS -> {
                DtoConsumer.Success(
                    (response as DetailVacancyDto).mapToDetailVacancy()
                )
            }

            ResponseCodes.NO_NET_CONNECTION -> DtoConsumer.NoInternet(
                response.resultCode.code
            )

            ResponseCodes.ERROR -> DtoConsumer.Error(response.resultCode.code)
        }
    }
}
