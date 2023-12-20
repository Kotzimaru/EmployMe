package ru.practicum.android.diploma.filter.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filter.data.converter.CountryConverter
import ru.practicum.android.diploma.filter.data.converter.FilterSettingsConverter
import ru.practicum.android.diploma.filter.data.converter.IndustryConverter
import ru.practicum.android.diploma.filter.data.converter.RegionConverter
import ru.practicum.android.diploma.filter.data.model.CountriesResponse
import ru.practicum.android.diploma.filter.data.model.FilterRequest
import ru.practicum.android.diploma.filter.data.model.IndustriesResponse
import ru.practicum.android.diploma.filter.data.model.IndustryDto
import ru.practicum.android.diploma.filter.data.model.RegionsResponse
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.FilterSettings
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.data.models.ResponseCodes
import ru.practicum.android.diploma.search.data.models.dto.Industry
import ru.practicum.android.diploma.search.domain.api.DtoConsumer
import ru.practicum.android.diploma.util.network.NetworkClient
import ru.practicum.android.diploma.util.storage.sharedpreference.SharedPrefStorageClient

class FilterRepositoryImpl(
    private val storageClient: SharedPrefStorageClient,
    private val networkClient: NetworkClient,
) : FilterRepository {
    override suspend fun saveCountryFilter(country: String) {
        storageClient.saveCountry(country)
    }

    override suspend fun deleteCountryFilter() {
        storageClient.deleteCountry()
    }

    override suspend fun saveAreaFilter(area: Region) {
        storageClient.saveArea(RegionConverter.map(area))
    }

    override suspend fun deleteAreaFilter() {
        storageClient.deleteArea()
    }

    override suspend fun saveIndustryFilter(industry: Industry) {
        storageClient.saveIndustry(IndustryConverter.map(industry))
    }

    override suspend fun deleteIndustryFilter() {
        storageClient.deleteIndustry()
    }

    override suspend fun setFilter(salary: String?, onlyWithSalary: Boolean) {
        storageClient.setFilter(salary, onlyWithSalary)
    }

    override suspend fun clearFilter() {
        storageClient.clearFilter()
    }

    override suspend fun getFilter(): FilterSettings {
        return FilterSettingsConverter().map(storageClient.getFilter())
    }

    override suspend fun getIndustries(): Flow<DtoConsumer<List<IndustryDto>>> = flow {
        val response = networkClient.doRequest(FilterRequest.Industries)
        when (response.resultCode) {
            ResponseCodes.SUCCESS -> {
                val industries = response as IndustriesResponse
                emit(DtoConsumer.Success(industries.items))
            }
            ResponseCodes.NO_NET_CONNECTION -> {
                emit(DtoConsumer.NoInternet(response.resultCode.code))
            }
            ResponseCodes.ERROR -> {
                emit(DtoConsumer.Error(response.resultCode.code))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCountries(): Flow<DtoConsumer<List<Region>>> = flow<DtoConsumer<List<Region>>> {
        val response = networkClient.doRequest(FilterRequest.Countries)
        when (response.resultCode) {
            ResponseCodes.SUCCESS -> emit(
                DtoConsumer.Success(
                    (response as CountriesResponse).items.map {
                        CountryConverter.map(it)
                    }
                )
            )
            ResponseCodes.NO_NET_CONNECTION -> {
                emit(DtoConsumer.NoInternet(response.resultCode.code))
            }
            ResponseCodes.ERROR -> {
                emit(DtoConsumer.Error(response.resultCode.code))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getRegions(): Flow<DtoConsumer<List<Region>>> = flow<DtoConsumer<List<Region>>> {
        val response = networkClient.doRequest(FilterRequest.Regions)
        when (response.resultCode) {
            ResponseCodes.SUCCESS -> emit(
                DtoConsumer.Success(
                    (response as RegionsResponse).items.map {
                        RegionConverter.map(it)
                    }
                )
            )
            ResponseCodes.NO_NET_CONNECTION -> {
                emit(DtoConsumer.NoInternet(response.resultCode.code))
            }
            ResponseCodes.ERROR -> {
                emit(DtoConsumer.Error(response.resultCode.code))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getRegionsByCountry(countryId: String): Flow<DtoConsumer<List<Region>>> = flow<DtoConsumer<List<Region>>> {
        val response = networkClient.doRequest(FilterRequest.RegionsByCountry(countryId))
        when (response.resultCode) {
            ResponseCodes.SUCCESS -> emit(
                DtoConsumer.Success(
                    (response as RegionsResponse).items.map {
                        RegionConverter.map(it)
                    }
                )
            )
            ResponseCodes.NO_NET_CONNECTION -> {
                emit(DtoConsumer.NoInternet(response.resultCode.code))
            }
            ResponseCodes.ERROR -> {
                emit(DtoConsumer.Error(response.resultCode.code))
            }
        }
    }.flowOn(Dispatchers.IO)
}
