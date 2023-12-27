package ru.practicum.android.diploma.util.storage.sharedpreference

import ru.practicum.android.diploma.filter.data.model.CountryDto
import ru.practicum.android.diploma.filter.data.model.FilterSettingsDto
import ru.practicum.android.diploma.filter.data.model.IndustryDto
import ru.practicum.android.diploma.filter.data.model.RegionDto

interface StorageClient {
    suspend fun saveCountry(country: CountryDto)
    suspend fun deleteCountry()
    suspend fun getCountry(): CountryDto
    suspend fun saveRegion(area: RegionDto)
    suspend fun getRegion(): RegionDto
    suspend fun deleteRegion()
    suspend fun saveIndustry(industry: IndustryDto)
    suspend fun getIndustry(): IndustryDto
    suspend fun deleteIndustry()
    suspend fun setFilter(salary: String, onlyWithSalary: Boolean)
    suspend fun clearFilter()
    suspend fun getFilter(): FilterSettingsDto
    suspend fun getFilterSettings(): FilterSettingsDto
}
