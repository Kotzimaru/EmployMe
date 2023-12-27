package ru.practicum.android.diploma.search.data.models

data class SearchResponse(
    val items: List<VacancyDto>,
    val found: Int,
    val page: Int,
    val pages: Int
) : Response()
