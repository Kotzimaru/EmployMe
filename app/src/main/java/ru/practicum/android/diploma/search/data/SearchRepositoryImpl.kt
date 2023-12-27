package ru.practicum.android.diploma.search.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.search.data.models.ResponseCodes
import ru.practicum.android.diploma.search.data.models.SearchRequest
import ru.practicum.android.diploma.search.data.models.SearchResponse
import ru.practicum.android.diploma.search.data.models.dto.Salary
import ru.practicum.android.diploma.search.domain.api.DtoConsumer
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.Filter
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyInfo
import ru.practicum.android.diploma.util.network.NetworkClient

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {

    override suspend fun doRequest(
        filter: Filter
    ): Flow<DtoConsumer<VacancyInfo>> =
        flow {
            val response = networkClient.doRequest(AdapterSearch.filterToVacancyReq(filter))
            when (response.resultCode) {
                ResponseCodes.SUCCESS -> emit(
                    DtoConsumer.Success(
                        AdapterSearch
                            .searchResponseToVacancyInfo(
                                response = (response as SearchResponse)
                            )
                    )
                )

                ResponseCodes.NO_NET_CONNECTION -> emit(DtoConsumer.NoInternet(response.resultCode.code))
                ResponseCodes.ERROR -> emit(DtoConsumer.Error(response.resultCode.code))
            }
        }.flowOn(Dispatchers.IO)
}

object AdapterSearch {

/*    fun vacancyInfoDtoToVacancyInfo(response: List<VacancyDto>): List<Vacancy> = response.map {
        Vacancy(
            id = it.id,
            area = it.area.name,
            // department = it.department.name,
            employerImgUrl = it.employer.logoUrls?.original?: "",
            employer = it.employer.name,
            name = it.name,
            salary = formSalaryString(it.salary),
            type = it.type.name
        )
    }*/

    fun searchResponseToVacancyInfo(response: SearchResponse): VacancyInfo = VacancyInfo(
        responseCodes = when (response.resultCode.code) {
            200 -> { ru.practicum.android.diploma.search.domain.models.ResponseCodes.SUCCESS }
            500 -> { ru.practicum.android.diploma.search.domain.models.ResponseCodes.SUCCESS }
            else -> { ru.practicum.android.diploma.search.domain.models.ResponseCodes.SUCCESS }
        },
        vacancy = response.items.map {
            Vacancy(
                id = it.id,
                area = it.area.name,
                // department = it.department.name,
                employerImgUrl = it.employer.logoUrls?.original ?: "",
                employer = it.employer.name,
                name = it.name,
                salary = formSalaryString(it.salary)
            )
        },
        found = response.found,
        page = response.page,
        pages = response.pages
    )

    fun filterToVacancyReq(filter: Filter) = SearchRequest(
        makeHasMap(filter)
    )

    private fun formSalaryString(salary: Salary?): String {
        if (salary == null) return " "
        if (salary.from == null && salary.to != null) {
            return "до  ${salary.to} ${salary.currency}"
        }
        if (salary.from != null && salary.to == null) {
            return "от  ${salary.from}  ${salary.currency}"
        }
        return "от  ${salary.from}  до  ${salary.to} ${salary.currency}"
    }

    private fun makeHasMap(filter: Filter): HashMap<String, String> {
        val request = HashMap<String, String>()
        request["text"] = filter.request
        request["page"] = filter.page.toString()
        if (!filter.area.isNullOrBlank()) request["area"] = filter.area
        if (!filter.industry.isNullOrBlank()) request["industry"] = filter.industry
        if (filter.onlyWithSalary == true) request["only_with_salary"] = filter.onlyWithSalary.toString()

        if (!filter.salary.toString().isNullOrBlank() &&
            Integer.parseInt(filter.salary.toString()) > 0
        ) {
            request["salary"] = filter.salary.toString()
        }

        return request
    }
}
