package ru.practicum.android.diploma.detail.domain.models

import ru.practicum.android.diploma.search.domain.models.ResponseCodes

data class SimilarVacanciesInfo(
    val responseCodes: ResponseCodes,
    val vacancies: List<SimilarVacancy>?
)
