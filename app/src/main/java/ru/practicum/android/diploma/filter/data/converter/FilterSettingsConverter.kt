package ru.practicum.android.diploma.filter.data.converter

import ru.practicum.android.diploma.filter.data.model.FilterSettingsDto
import ru.practicum.android.diploma.filter.domain.models.FilterSettings

class FilterSettingsConverter {
    fun map(filterSettings: FilterSettingsDto): FilterSettings {
        return FilterSettings(
            salary = filterSettings.salary,
            onlyWithSalary = filterSettings.onlyWithSalary,
            industry = IndustryConverter.map(filterSettings.industry),
            country = CountryConverter.map(filterSettings.country),
            region = RegionConverter.map(filterSettings.region)
        )
    }

    fun map(filterSettings: FilterSettings): FilterSettingsDto {
        return FilterSettingsDto(
            salary = filterSettings.salary,
            onlyWithSalary = filterSettings.onlyWithSalary,
            industry = IndustryConverter.map(filterSettings.industry),
            country = CountryConverter.map(filterSettings.country),
            region = RegionConverter.map(filterSettings.region)
        )
    }
}
