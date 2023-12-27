package ru.practicum.android.diploma.util.storage.sharedpreference

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filter.data.model.CountryDto
import ru.practicum.android.diploma.filter.data.model.FilterSettingsDto
import ru.practicum.android.diploma.filter.data.model.IndustryDto
import ru.practicum.android.diploma.filter.data.model.RegionDto

class SharedPrefStorageClient(
    private val sharedPref: SharedPreferences,
) : StorageClient {
    override suspend fun saveCountry(country: CountryDto) {
        val json = Gson().toJson(country)
        sharedPref.edit()
            .putString(FILTER_COUNTRY, json)
            .apply()
    }

    override suspend fun getCountry(): CountryDto {
        var country = CountryDto(
            id = "",
            name = "",
            areas = listOf()
        )
        val json = sharedPref.getString(FILTER_COUNTRY, null)
        if (json !== null) {
            val countryFromJson = Gson().fromJson(json, CountryDto::class.java)

            country = countryFromJson
        }

        return country
    }

    override suspend fun deleteCountry() {
        sharedPref.edit()
            .remove(FILTER_COUNTRY)
            .apply()
    }

    override suspend fun saveRegion(area: RegionDto) {
        val json = Gson().toJson(area)
        sharedPref.edit()
            .putString(FILTER_AREA, json)
            .apply()
    }

    override suspend fun getRegion(): RegionDto {
        var region = RegionDto(
            id = "",
            name = "",
            areas = listOf()
        )
        val json = sharedPref.getString(FILTER_AREA, null)
        if (json !== null) {
            val areaFromJson = Gson().fromJson(json, RegionDto::class.java)

            region = areaFromJson
        }

        return region
    }

    override suspend fun deleteRegion() {
        sharedPref.edit()
            .remove(FILTER_AREA)
            .apply()
    }

    override suspend fun saveIndustry(industry: IndustryDto) {
        val json = Gson().toJson(industry)
        sharedPref.edit()
            .putString(FILTER_INDUSTRY, json)
            .apply()
    }

    override suspend fun deleteIndustry() {
        sharedPref.edit()
            .remove(FILTER_INDUSTRY)
            .apply()
    }

    override suspend fun getIndustry(): IndustryDto {
        var industry = IndustryDto(
            id = "",
            name = "",
            industries = listOf()
        )
        val json = sharedPref.getString(FILTER_INDUSTRY, null)
        if (json !== null) {
            val industryFromJson = Gson().fromJson(json, IndustryDto::class.java)

            industry = industryFromJson
        }

        return industry
    }

    override suspend fun setFilter(salary: String, onlyWithSalary: Boolean) {
        val filter = FilterSettingsDto(
            salary,
            onlyWithSalary,
            getIndustry(),
            getCountry(),
            getRegion()
        )

        val json = Gson().toJson(filter)

        sharedPref.edit()
            .putString(FILTER_SETTINGS, json)
            .apply()
    }

    override suspend fun clearFilter() {
        sharedPref.edit()
            .remove(FILTER_SETTINGS)
            .apply()

        deleteCountry()
        deleteIndustry()
        deleteRegion()
    }

    override suspend fun getFilter(): FilterSettingsDto {
        val country = getCountry()
        val region = getRegion()
        val industry = getIndustry()
        var filter = FilterSettingsDto(
            salary = "",
            country = country,
            onlyWithSalary = false,
            region = region,
            industry = industry
        )
        val json = sharedPref.getString(FILTER_SETTINGS, null)
        if (json !== null) {
            val filterFromJson = Gson().fromJson(json, FilterSettingsDto::class.java)

            filter = filterFromJson
            if (country.id.isNotEmpty()) {
                filter.country = country
            }
            if (region.id.toString().isNotEmpty()) {
                filter.region = region
            }
            if (industry.id.toString().isNotEmpty()) {
                filter.industry = industry
            }
        }

        return filter
    }

    override suspend fun getFilterSettings(): FilterSettingsDto {
        var filter = FilterSettingsDto(
            salary = "",
            country = CountryDto(
                "",
                "",
                listOf()
            ),
            onlyWithSalary = false,
            region = RegionDto(
                "",
                "",
                listOf()
            ),
            industry = IndustryDto(
                "",
                listOf(),
                ""
            )
        )
        val json = sharedPref.getString(FILTER_SETTINGS, null)
        if (json !== null) {
            val filterFromJson = Gson().fromJson(json, FilterSettingsDto::class.java)

            filter = filterFromJson
        }

        return filter
    }

    companion object {
        const val FILTER_AREA = "key_for_area"
        const val FILTER_COUNTRY = "key_for_country"
        const val FILTER_INDUSTRY = "key_for_industry"
        const val FILTER_SETTINGS = "key_for_filter_settings"
    }
}
