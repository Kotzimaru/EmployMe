package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.models.Filter
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyInfo

interface SearchRepository {

    suspend fun doRequest(filter: Filter): Flow<DtoConsumer<VacancyInfo>>
}
