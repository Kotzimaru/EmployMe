package ru.practicum.android.diploma.search.domain.models

data class VacancyInfo(
    val responseCodes: ResponseCodes,
    val vacancy: List<Vacancy>?,
    val found: Int = 0,
    val page: Int = 0,
    val pages: Int = 0
)
