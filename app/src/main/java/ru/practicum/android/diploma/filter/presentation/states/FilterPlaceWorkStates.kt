package ru.practicum.android.diploma.filter.presentation.states

import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Region

sealed class FilterPlaceWorkStates {
    data class HasCountry(val country: Country) : FilterPlaceWorkStates()
    data class HasRegion(val region: Region) : FilterPlaceWorkStates()
    object ClearedCountry : FilterPlaceWorkStates()
    object ClearedRegion : FilterPlaceWorkStates()
}
