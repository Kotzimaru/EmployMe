package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterSettings
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.domain.api.DtoConsumer

class FilterInteractorImpl(private val repository: FilterRepository) : FilterInteractor {
    override suspend fun saveCountryFilter(country: Country) {
        repository.saveCountryFilter(country)
    }

    override suspend fun deleteCountryFilter() {
        repository.deleteCountryFilter()
    }

    override suspend fun getCountryFilter(): Country {
        return repository.getCountryFilter()
    }

    override suspend fun saveRegionFilter(region: Region) {
        repository.saveRegionFilter(region)
    }

    override suspend fun deleteRegionFilter() {
        repository.deleteRegionFilter()
    }

    override suspend fun getRegionFilter(): Region {
        return repository.getRegionFilter()
    }

    override suspend fun saveIndustryFilter(industry: Industry) {
        repository.saveIndustryFilter(industry)
    }

    override suspend fun deleteIndustryFilter() {
        repository.deleteIndustryFilter()
    }

    override suspend fun getIndustryFilter(): Industry {
        return repository.getIndustryFilter()
    }

    override suspend fun setFilterSettings(salary: String, onlyWithSalary: Boolean) {
        repository.setFilterSettings(salary, onlyWithSalary)
    }

    override suspend fun clearFilterSettings() {
        repository.clearFilterSettings()
    }

    override suspend fun getFilter(): FilterSettings? {
        return repository.getFilter()
    }

    override suspend fun getFilterSettings(): FilterSettings? {
        return repository.getFilterSettings()
    }

    override suspend fun getIndustries(): Flow<DtoConsumer<List<Industry>>> {
        return repository.getIndustries()
    }

    override suspend fun getCountries(): Flow<DtoConsumer<List<Country>>> {
        return repository.getCountries()
    }

    override suspend fun getRegions(): Flow<DtoConsumer<List<Region>>> {
        return repository.getRegions()
    }

    override suspend fun getIndustriesByName(industry: String): Flow<DtoConsumer<List<Industry>>> {
        return repository.getIndustriesByName(industry)
    }

    override suspend fun getRegionsByName(name: String): Flow<DtoConsumer<List<Region>>> {
        return repository.getRegionsByName(name)
    }
}
