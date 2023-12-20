package ru.practicum.android.diploma.util.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.detail.data.dto.DetailVacancyDto
import ru.practicum.android.diploma.detail.data.dto.SimilarVacanciesDto
import ru.practicum.android.diploma.filter.data.model.CountryDto
import ru.practicum.android.diploma.filter.data.model.IndustryDto
import ru.practicum.android.diploma.filter.data.model.RegionDto
import ru.practicum.android.diploma.search.data.models.SearchResponse

interface HhApiVacancy {

    // Запрос списка вакансий
    @Headers(
        HEADER_AUTH,
        HH_USER
    )
    @GET("vacancies")
    suspend fun getVacancyList(
        @QueryMap options: HashMap<String, String>
    ): SearchResponse

    // Запрос дет. информации по вакансии
    @Headers(
        HEADER_AUTH,
        HH_USER
    )
    @GET("vacancies/{vacancy_id}")
    suspend fun getDetail(
        @Path("vacancy_id") vacancy: String
    ): DetailVacancyDto

    @Headers(
        HEADER_AUTH,
        HH_USER
    )
    @GET("vacancies/{vacancy_id}/similar_vacancies")
    suspend fun getSimilarVacancies(
        @Path("vacancy_id") vacancy: String
    ): SimilarVacanciesDto

    // Запрос списка отраслей
    @Headers(
        HEADER_AUTH,
        HH_USER
    )
    @GET("industries")
    suspend fun getIndustries(): List<IndustryDto>

    // Запрос списка стран
    @Headers(
        HEADER_AUTH,
        HH_USER
    )
    @GET("areas/countries")
    suspend fun getCountries(): List<CountryDto>

    // Запрос списка регионов
    @Headers(
        HEADER_AUTH,
        HH_USER
    )
    @GET("areas")
    suspend fun getRegions(): List<RegionDto>

    // Запрос списка регионов по стране
    @Headers(
        HEADER_AUTH,
        HH_USER
    )
    @GET("areas/{area_id}")
    suspend fun getRegionsByCountry(@Path("area_id") id: String): RegionDto

    companion object {
        const val HEADER_AUTH = "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}"
        const val HH_USER = "HH-User-Agent: EmployMe (asa-tek68@mail.ru)"
    }
}
