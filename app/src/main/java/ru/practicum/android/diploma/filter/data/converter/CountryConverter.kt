package ru.practicum.android.diploma.filter.data.converter

import ru.practicum.android.diploma.filter.data.model.CountryDto
import ru.practicum.android.diploma.filter.domain.models.Country

object CountryConverter {
    fun map(country: CountryDto): Country {
        return Country(
            id = country.id,
            name = country.name
        )
    }

    fun map(country: Country): CountryDto {
        return CountryDto(
            id = country.id,
            name = country.name,
            areas = listOf()
        )
    }
}
