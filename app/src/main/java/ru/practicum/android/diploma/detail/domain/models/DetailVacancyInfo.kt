package ru.practicum.android.diploma.detail.domain.models

import ru.practicum.android.diploma.search.domain.models.ResponseCodes

data class DetailVacancyInfo(
    val responseCodes: ResponseCodes,
    val detailVacancy: DetailVacancy?
)
