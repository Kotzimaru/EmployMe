package ru.practicum.android.diploma.search.data

import ru.practicum.android.diploma.search.data.models.SearchRequest
import ru.practicum.android.diploma.search.data.models.SearchResponse
import ru.practicum.android.diploma.search.data.models.dto.LogoUrls
import ru.practicum.android.diploma.search.domain.models.Filter
import ru.practicum.android.diploma.search.domain.models.ResponseCodes
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.domain.models.VacancyInfo
import ru.practicum.android.diploma.util.TextUtils
import ru.practicum.android.diploma.util.network.ResourceProvider

class AdapterSearch(private val resourceProvider: ResourceProvider) {

    fun vacancyInfoDtoToVacancyInfo(response: SearchResponse, code: Int) = VacancyInfo(
        responseCodes = codeMapper(code, response.found),
        vacancy = response.items.map {
            Vacancy(
                id = it.id,
                area = it.area.name,
                // department = it.department?.name ?: "",
                employerImgUrl = getLogo(it.employer.logoUrls),
                employer = it.employer.name,
                name = it.name,
                salary = TextUtils.getSalaryString(it.salary, resourceProvider),
                type = it.type.name
            )
        },
        found = response.found,
        page = response.page,
        pages = response.pages
    )

    fun filterToVacancyReq(filter: Filter) = SearchRequest(
        makeHasMap(filter)
    )

    private fun codeMapper(code: Int, found: Int) = when (code) {
        200 -> if (found == 0) ResponseCodes.NO_RESULTS else ResponseCodes.SUCCESS
        500 -> ResponseCodes.NO_NET_CONNECTION
        else -> ResponseCodes.ERROR
    }
    private fun getLogo(logoUrls: LogoUrls?): String {
        if (logoUrls?.mediumIcon != null) return logoUrls.mediumIcon.toString()
        if (logoUrls?.original != null) return logoUrls.original.toString()
        return ""
    }

    private fun makeHasMap(filter: Filter): HashMap<String, String> {
        val request = HashMap<String, String>()
        request["text"] = filter.request
        request["page"] = filter.page.toString()
        request["only_with_salary"] = filter.onlyWithSalary.toString()
        if (filter.area != null) request["area"] = filter.area
        if (filter.industry != null) request["industry"] = filter.industry
        if (filter.salary != null) request["salary"] = filter.salary.toString()
        return request
    }
}
