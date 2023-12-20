package ru.practicum.android.diploma.filter.data.converter

import ru.practicum.android.diploma.filter.data.model.IndustryDto
import ru.practicum.android.diploma.search.data.models.dto.Industry

object IndustryConverter {
    fun map(industry: IndustryDto): Industry {
        return Industry(
            id = industry.id,
            name = industry.name
        )
    }

    fun map(industry: Industry): IndustryDto {
        return IndustryDto(
            id = industry.id,
            name = industry.name,
            industries = null
        )
    }
}
