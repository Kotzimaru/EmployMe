package ru.practicum.android.diploma.detail.data

import ru.practicum.android.diploma.detail.data.remote.DetailDto
import ru.practicum.android.diploma.detail.domain.models.ProfessionDetail

fun DetailDto.mapToProfessionDetail(): ProfessionDetail =
    ProfessionDetail(
        id = this.id,
        name = this.name,
        employmentId = this.employment.id,
        employmentName = this.employment.name,
        employerId = this.employer.id,
        employerName = this.employer.name,
        employerLogo = this.employer.logoUrls.original,
        experienceId = this.experience?.id,
        experienceName =this.experience?.name,
        salaryCurrency = this.salary?.currency,
        salaryFrom = this.salary?.from,
        salaryTo = this.salary?.to,
        employerCity = this.area.name
    )
