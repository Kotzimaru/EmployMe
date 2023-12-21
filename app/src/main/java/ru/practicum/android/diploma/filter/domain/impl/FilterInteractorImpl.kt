package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.FilterSettings
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.data.models.Industry
import ru.practicum.android.diploma.search.domain.api.DtoConsumer

class FilterInteractorImpl(private val repository: FilterRepository) : FilterInteractor {
    override suspend fun saveCountryFilter(country: String) {
        repository.saveCountryFilter(country)
    }

    override suspend fun deleteCountryFilter() {
        repository.deleteCountryFilter()
    }

    override suspend fun saveAreaFilter(area: Region) {
        repository.saveAreaFilter(area)
    }

    override suspend fun deleteAreaFilter() {
        repository.deleteAreaFilter()
    }

    override suspend fun saveIndustryFilter(industry: Industry) {
        repository.saveIndustryFilter(industry)
    }

    override suspend fun deleteIndustryFilter() {
        repository.deleteIndustryFilter()
    }

    override suspend fun setFilter(salary: String?, onlyWithSalary: Boolean) {
        repository.setFilter(salary, onlyWithSalary)
    }

    override suspend fun clearFilter() {
        repository.clearFilter()
    }

    override suspend fun getFilter(): FilterSettings {
        return repository.getFilter()
    }

    override suspend fun getIndustries(): Flow<DtoConsumer<List<Industry>>> {
        return repository.getIndustries()
    }

    override suspend fun getCountries(): Flow<DtoConsumer<List<Region>>> {
        return repository.getCountries()
    }

    override suspend fun getRegions(): Flow<DtoConsumer<List<Region>>> {
        return repository.getRegions()
    }

    override suspend fun getRegionsByCountry(countryId: String): Flow<DtoConsumer<List<Region>>> {
        return  repository.getRegionsByCountry(countryId)
    }

}