package ru.practicum.android.diploma.detail.domain.usecase

import ru.practicum.android.diploma.detail.domain.impl.DetailRepository
import ru.practicum.android.diploma.detail.domain.models.ProfessionDetail
import ru.practicum.android.diploma.util.Resource

class DetailsInterActorImpl(
    private val repo: DetailRepository
) : DetailsInterActor {
    override suspend fun getDetails(
        id: String
    ): Resource<ProfessionDetail> {
        return repo.getVacancyDetail(id = id)
    }
}
