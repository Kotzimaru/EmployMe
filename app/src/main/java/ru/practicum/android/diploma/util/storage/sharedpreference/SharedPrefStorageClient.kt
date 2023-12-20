package ru.practicum.android.diploma.util.storage.sharedpreference

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filter.data.model.FilterSettingsDto
import ru.practicum.android.diploma.filter.data.model.IndustryDto
import ru.practicum.android.diploma.filter.data.model.RegionDto

class SharedPrefStorageClient(
    private val sharedPref: SharedPreferences,
) : StorageClient {
    override suspend fun saveCountry(country: String) {
        val json = Gson().toJson(country)
        sharedPref.edit()
            .putString(FILTER_COUNTRY, json)
            .apply()
    }

    private fun getCountry(): String {
        var country = ""
        val json = sharedPref.getString(FILTER_COUNTRY, null)
        if (json !== null) {
            val countryFromJson = Gson().fromJson(json, String::class.java)

            country = countryFromJson
        }

        return country
    }

    override suspend fun deleteCountry() {
        sharedPref.edit()
            .remove(FILTER_COUNTRY)
            .apply()
    }

    override suspend fun saveArea(area: RegionDto) {
        val json = Gson().toJson(area)
        sharedPref.edit()
            .putString(FILTER_AREA, json)
            .apply()
    }

    private fun getArea(): RegionDto {
        var area: RegionDto? = null
        val json = sharedPref.getString(FILTER_AREA, null)
        if (json !== null) {
            val areaFromJson = Gson().fromJson(json, RegionDto::class.java)

            area = areaFromJson
        }

        return area!!
    }

    override suspend fun deleteArea() {
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

    private fun getIndustry(): IndustryDto {
        var industry: IndustryDto? = null
        val json = sharedPref.getString(FILTER_INDUSTRY, null)
        if (json !== null) {
            val industryFromJson = Gson().fromJson(json, IndustryDto::class.java)

            industry = industryFromJson
        }

        return industry!!
    }

    override suspend fun setFilter(salary: String?, onlyWithSalary: Boolean) {
        val filter = FilterSettingsDto(
            salary,
            onlyWithSalary,
            getIndustry(),
            getCountry(),
            getArea()
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
    }

    override suspend fun getFilter(): FilterSettingsDto {
        var filter: FilterSettingsDto? = null
        val json = sharedPref.getString(FILTER_SETTINGS, null)
        if (json !== null) {
            val filterFromJson = Gson().fromJson(json, FilterSettingsDto::class.java)

            filter = filterFromJson
        }

        return filter!!
    }

    companion object {
        const val FILTER_AREA = "key_for_area"
        const val FILTER_COUNTRY = "key_for_country"
        const val FILTER_INDUSTRY = "key_for_industry"
        const val FILTER_SETTINGS = "key_for_filter_settings"
    }
}
