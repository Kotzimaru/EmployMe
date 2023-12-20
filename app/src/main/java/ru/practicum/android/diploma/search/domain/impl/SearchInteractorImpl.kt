package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.search.domain.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.DtoConsumer
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Filter
import ru.practicum.android.diploma.search.domain.models.ResponseCodes
import ru.practicum.android.diploma.search.domain.models.VacancyInfo

class SearchInteractorImpl(
    private val repository: SearchRepository
) : SearchInteractor {
    override suspend fun execute(filter: Filter): Flow<VacancyInfo> =
        repository.doRequest(filter = filter).map { result ->
            when (result) {
                is DtoConsumer.Success -> result.data

                is DtoConsumer.Error -> Adapter.codeMapper(ResponseCodes.ERROR)

                is DtoConsumer.NoInternet -> Adapter.codeMapper(ResponseCodes.NO_NET_CONNECTION)
            }
        }
}

object Adapter {
    fun codeMapper(codesError: ResponseCodes) = VacancyInfo(responseCodes = codesError, null)
}
