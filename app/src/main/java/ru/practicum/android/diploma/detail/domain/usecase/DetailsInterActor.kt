package ru.practicum.android.diploma.detail.domain.usecase

import ru.practicum.android.diploma.detail.domain.models.ProfessionDetail
import ru.practicum.android.diploma.util.Resource

interface DetailsInterActor {
    suspend fun getDetails(
        id: String
    ): Resource<ProfessionDetail>
}
