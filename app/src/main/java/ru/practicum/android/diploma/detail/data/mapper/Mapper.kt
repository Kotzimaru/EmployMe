package ru.practicum.android.diploma.detail.data.mapper

import ru.practicum.android.diploma.detail.data.dto.DetailVacancyDto
import ru.practicum.android.diploma.detail.data.dto.KeySkillDetailVacancyDto
import ru.practicum.android.diploma.detail.data.dto.SimilarVacanciesDto
import ru.practicum.android.diploma.detail.domain.models.CurrencyDetailVacancy
import ru.practicum.android.diploma.detail.domain.models.DetailVacancy
import ru.practicum.android.diploma.detail.domain.models.SalaryDetailVacancy
import ru.practicum.android.diploma.detail.domain.models.SimilarVacancy

fun SimilarVacanciesDto.mapToSimillarVacancies(): List<SimilarVacancy> {
    return this.itemSimillarVacancyDtos.map { itemSimillarVacancyDto ->
        val salary = SalaryDetailVacancy(
            from = itemSimillarVacancyDto.salary?.from,
            to = itemSimillarVacancyDto.salary?.to,
            currency = CurrencyDetailVacancy.getCurrency(itemSimillarVacancyDto.salary?.currency.toString())
        )
        SimilarVacancy(
            id = itemSimillarVacancyDto.id,
            name = itemSimillarVacancyDto.name,
            address = itemSimillarVacancyDto.area.name,
            idEmployer = itemSimillarVacancyDto.employer.id,
            nameEmployer = itemSimillarVacancyDto.employer.name,
            logoUrlsEmployerOriginal = itemSimillarVacancyDto.employer.logoUrlsDetailVacancyDto?.original,
            salary = salary
        )
    }
}

fun DetailVacancyDto.mapToDetailVacancy(): DetailVacancy {
    val comment = if (!contactsDetailVacancyDto?.phoneDetailVacancyDtos.isNullOrEmpty()) {
        contactsDetailVacancyDto?.phoneDetailVacancyDtos?.first()?.comment
    } else {
        null
    }

    val salary = SalaryDetailVacancy(
        from = salaryDetailVacancyDto?.from,
        to = salaryDetailVacancyDto?.to,
        currency = CurrencyDetailVacancy.getCurrency(salaryDetailVacancyDto?.currency.toString())
    )

    return DetailVacancy(
        id = id,
        address = areaDetailVacancyDto.name,
        name = name,
        description = description,
        keySkills = keySkillDetailVacancyDtos.mapToString(),
        comment = comment,
        contactName = contactsDetailVacancyDto?.name,
        email = contactsDetailVacancyDto?.email,
        url = url,
        idEmployment = employmentDetailVacancyDto.id,
        nameEmployment = employmentDetailVacancyDto.name,
        idEmployer = employerDetailVacancyDto.id,
        nameEmployer = employerDetailVacancyDto.name,
        logoUrlsEmployerOriginal = employerDetailVacancyDto.logoUrlsDetailVacancyDto?.original,
        alternateUrlEmployer = employerDetailVacancyDto.alternateUrl,
        blacklistedEmployer = employerDetailVacancyDto.blacklisted,
        urlEmployer = employerDetailVacancyDto.url,
        trustedEmployer = employerDetailVacancyDto.trusted,
        idExperience = experienceDetailVacancyDto?.id,
        nameExperience = experienceDetailVacancyDto?.name,
        phone = if (!contactsDetailVacancyDto?.phoneDetailVacancyDtos.isNullOrEmpty()) {
            convertStringToSting(
                prefix = contactsDetailVacancyDto?.phoneDetailVacancyDtos?.first()?.country,
                middle = contactsDetailVacancyDto?.phoneDetailVacancyDtos?.first()?.city,
                postfix = contactsDetailVacancyDto?.phoneDetailVacancyDtos?.first()?.number
            )
        } else {
            null
        },
        salary = salary
    )
}

private fun List<KeySkillDetailVacancyDto>?.mapToString(): String {
    return if (this.isNullOrEmpty()) {
        ""
    } else {
        val marker = "• "
        val newline = "\n"
        this.joinToString("") { marker + it.name + newline }
    }
}

private fun convertStringToSting(
    prefix: String?,
    middle: String?,
    postfix: String?
): String? {
    return if (prefix != null && middle != null && postfix != null) {
        "+$prefix ($middle) $postfix"
    } else {
        null
    }
}
