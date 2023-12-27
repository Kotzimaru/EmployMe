package ru.practicum.android.diploma.filter.presentation.states

import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Region

sealed class FilterStates {
    data class HasFilters(
        val salary: String,
        val onlyWithSalary: Boolean,
        val industry: Industry,
        val country: Country,
        val region: Region
    ) : FilterStates()
    object SaveSettings : FilterStates()
    object ClearSettings : FilterStates()
    object DeleteCountryAndRegion : FilterStates()
    object DeleteIndustry : FilterStates()
}
