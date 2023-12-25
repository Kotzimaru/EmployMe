package ru.practicum.android.diploma.filter.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.filter.data.converter.CountryConverter
import ru.practicum.android.diploma.filter.data.converter.RegionConverter
import ru.practicum.android.diploma.filter.data.converter.FilterSettingsConverter
import ru.practicum.android.diploma.filter.data.converter.IndustryConverter
import ru.practicum.android.diploma.filter.data.model.CountriesResponse
import ru.practicum.android.diploma.filter.data.model.FilterRequest
import ru.practicum.android.diploma.filter.data.model.IndustriesResponse
import ru.practicum.android.diploma.filter.data.model.RegionsResponse
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterSettings
import ru.practicum.android.diploma.filter.domain.models.Industry
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.data.models.Response
import ru.practicum.android.diploma.search.data.models.ResponseCodes
import ru.practicum.android.diploma.search.domain.api.DtoConsumer
import ru.practicum.android.diploma.util.network.NetworkClient
import ru.practicum.android.diploma.util.storage.sharedpreference.StorageClient

class FilterRepositoryImpl(
    private val storageClient: StorageClient,
    private val networkClient: NetworkClient,
): FilterRepository {
    override suspend fun saveCountryFilter(country: Country) {
        storageClient.saveCountry(CountryConverter.map(country))
    }

    override suspend fun deleteCountryFilter() {
        storageClient.deleteCountry()
    }

    override suspend fun getCountryFilter(): Country {
        return CountryConverter.map(storageClient.getCountry())
    }

    override suspend fun saveRegionFilter(region: Region) {
        storageClient.saveRegion(RegionConverter.map(region))
    }

    override suspend fun deleteRegionFilter() {
        storageClient.deleteRegion()
    }

    override suspend fun getRegionFilter(): Region {
        return RegionConverter.map(storageClient.getRegion())
    }

    override suspend fun saveIndustryFilter(industry: Industry) {
        storageClient.saveIndustry(IndustryConverter.map(industry))
    }

    override suspend fun deleteIndustryFilter() {
        storageClient.deleteIndustry()
    }

    override suspend fun getIndustryFilter(): Industry {
        return IndustryConverter.map(storageClient.getIndustry())
    }

    override suspend fun setFilter(salary: String, onlyWithSalary: Boolean) {
        storageClient.setFilter(salary, onlyWithSalary)
    }

    override suspend fun clearFilter() {
        storageClient.clearFilter()
    }

    override suspend fun getFilter(): FilterSettings {
        return FilterSettingsConverter().map(storageClient.getFilter())
    }

    override suspend fun getIndustries(): Flow<DtoConsumer<List<Industry>>> = flow {
        val response = networkClient.doRequest(FilterRequest.Industries)
        when (response.resultCode){
            ResponseCodes.SUCCESS -> {
                val industries = mutableListOf<Industry>()
                (response as IndustriesResponse).items.forEach {
                    industries.add(
                        IndustryConverter.map(it)
                    )
                    it.industries?.forEach {
                        industries.add(
                            IndustryConverter.map(it)
                        )
                    }
                }
                industries.sortBy { it.name }
                industries.distinct()

                val industryFilter = getIndustryFilter()
                if (industryFilter.id.isNotEmpty()){
                    industries.forEach {
                        if (it.id.equals(industryFilter.id)){
                            it.isChecked = true
                        }
                    }
                }

                emit(
                    DtoConsumer.Success(
                        industries.toList()
                    )
                )
            }

            ResponseCodes.NO_NET_CONNECTION -> {
                emit(DtoConsumer.NoInternet(response.resultCode.code))
            }
            ResponseCodes.ERROR -> {
                emit(DtoConsumer.Error(response.resultCode.code))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCountries(): Flow<DtoConsumer<List<Country>>> = flow<DtoConsumer<List<Country>>> {
        val response = networkClient.doRequest(FilterRequest.Countries)
        when (response.resultCode){
            ResponseCodes.SUCCESS -> emit(
                DtoConsumer.Success(
                    (response.data as CountriesResponse).items.map {
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
        val countryFilter = CountryConverter.map(storageClient.getCountry())

        val response = if (countryFilter.id.isNotEmpty()){
            networkClient.doRequest(FilterRequest.RegionsByCountry(countryFilter.id))
        } else {
            networkClient.doRequest(FilterRequest.Regions)
        }

        when (response.resultCode){
            ResponseCodes.SUCCESS -> {
                val regions = mutableListOf<Region>()

                (response as RegionsResponse).items.forEach {
                    it.areas?.forEach {
                        regions.addAll(RegionConverter.mapDtoToRegions(it))
                    }
                }

/*                (response as RegionsResponse).items.forEach {
                    it.areas?.forEach {
                        regions.add(
                            RegionConverter.map(it)
                        )
                    }
                }*/

                regions.sortBy { it.name }
                regions.distinct()

                emit(
                    DtoConsumer.Success(
                        regions.toList()
                    )
                )
            }
            ResponseCodes.NO_NET_CONNECTION -> {
                emit(DtoConsumer.NoInternet(response.resultCode.code))
            }
            ResponseCodes.ERROR -> {
                emit(DtoConsumer.Error(response.resultCode.code))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getRegionsByName(name: String): Flow<DtoConsumer<List<Region>>> = flow<DtoConsumer<List<Region>>> {
        val countryFilter = CountryConverter.map(storageClient.getCountry())

        val response = if (countryFilter.id.isNotEmpty()){
            networkClient.doRequest(FilterRequest.RegionsByCountry(countryFilter.id))
        } else {
            networkClient.doRequest(FilterRequest.Regions)
        }

        when (response.resultCode){
            ResponseCodes.SUCCESS -> {
                val regions = mutableListOf<Region>()
                val filteredRegions = mutableListOf<Region>()

                (response as RegionsResponse).items.forEach {
                    it.areas?.forEach {
                        regions.addAll(RegionConverter.mapDtoToRegions(it))
                    }
                }

/*                (response as RegionsResponse).items.forEach {
                    it.areas?.forEach {
                        regions.add(
                            RegionConverter.map(it)
                        )
                    }
                }*/

                filteredRegions.addAll(regions.filter { it.name.contains(name, ignoreCase = true) })
                filteredRegions.sortBy { it.name }
                filteredRegions.distinct()

                emit(
                    DtoConsumer.Success(
                        filteredRegions.toList()
                    )
                )
            }
            ResponseCodes.NO_NET_CONNECTION -> {
                emit(DtoConsumer.NoInternet(response.resultCode.code))
            }
            ResponseCodes.ERROR -> {
                emit(DtoConsumer.Error(response.resultCode.code))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getIndustriesByName(industry: String): Flow<DtoConsumer<List<Industry>>> = flow {
        val response = networkClient.doRequest(FilterRequest.Industries)
        when (response.resultCode){
            ResponseCodes.SUCCESS -> {
                val industries = mutableListOf<Industry>()
                val filteredIndustries = mutableListOf<Industry>()
                (response as IndustriesResponse).items.forEach {
                    industries.add(
                        IndustryConverter.map(it)
                    )
                    it.industries?.forEach {
                        industries.add(
                            IndustryConverter.map(it)
                        )
                    }
                }
                filteredIndustries.addAll(industries.filter { it.name.contains(industry, ignoreCase = true) })
                filteredIndustries.sortBy { it.name }
                filteredIndustries.distinct()

                val industryFilter = getIndustryFilter()
                if (industryFilter.id.isNotEmpty()){
                    filteredIndustries.forEach {
                        if (it.id.equals(industryFilter.id)){
                            it.isChecked = true
                        }
                    }
                }

                emit(
                    DtoConsumer.Success(
                        filteredIndustries.toList()
                    )
                )
            }

            ResponseCodes.NO_NET_CONNECTION -> {
                emit(DtoConsumer.NoInternet(response.resultCode.code))
            }
            ResponseCodes.ERROR -> {
                emit(DtoConsumer.Error(response.resultCode.code))
            }
        }
    }.flowOn(Dispatchers.IO)
}
