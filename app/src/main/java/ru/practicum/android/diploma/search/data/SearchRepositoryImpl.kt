package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.search.data.models.ResponseCodes
import ru.practicum.android.diploma.search.data.models.SearchResponse
import ru.practicum.android.diploma.search.domain.api.DtoConsumer
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Filter
import ru.practicum.android.diploma.search.domain.models.VacancyInfo
import ru.practicum.android.diploma.util.network.NetworkClient

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val adapter: AdapterSearch
) : SearchRepository {

    override suspend fun doRequest(
        filter: Filter
    ): Flow<DtoConsumer<VacancyInfo>> =
        flow {
            val response = networkClient.doRequest(adapter.filterToVacancyReq(filter))
            when (response.resultCode) {
                ResponseCodes.SUCCESS -> emit(
                    DtoConsumer.Success(
                        adapter
                            .vacancyInfoDtoToVacancyInfo(
                                code = response.resultCode.code,
                                response = (response as SearchResponse)
                            )
                    )
                )

                ResponseCodes.NO_NET_CONNECTION -> emit(DtoConsumer.NoInternet(response.resultCode.code))
                ResponseCodes.ERROR -> emit(DtoConsumer.Error(response.resultCode.code))
            }
        }.flowOn(Dispatchers.IO)
}

