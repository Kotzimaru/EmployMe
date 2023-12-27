package ru.practicum.android.diploma.detail.data

import ru.practicum.android.diploma.detail.data.dto.SimilarVacanciesDto
import ru.practicum.android.diploma.detail.data.mapper.mapToSimillarVacancies
import ru.practicum.android.diploma.detail.data.models.SimilarRequest
import ru.practicum.android.diploma.detail.domain.api.SimilarRepository
import ru.practicum.android.diploma.detail.domain.models.SimilarVacancy
import ru.practicum.android.diploma.search.data.models.ResponseCodes
import ru.practicum.android.diploma.search.domain.api.DtoConsumer
import ru.practicum.android.diploma.util.network.NetworkClient

class SimilarRepositoryImpl(private val networkClient: NetworkClient) : SimilarRepository {

    override suspend fun doRequest(vacancy: String): DtoConsumer<List<SimilarVacancy>> {
        val response = networkClient.doRequest(SimilarRequest(vacancy))
        return when (response.resultCode) {
            ResponseCodes.SUCCESS -> {
                DtoConsumer.Success(
                    (response.data as SimilarVacanciesDto).mapToSimillarVacancies()
                )
            }

            ResponseCodes.NO_NET_CONNECTION -> DtoConsumer.NoInternet(response.resultCode.code)
            ResponseCodes.ERROR -> DtoConsumer.Error(response.resultCode.code)
        }
    }
}
