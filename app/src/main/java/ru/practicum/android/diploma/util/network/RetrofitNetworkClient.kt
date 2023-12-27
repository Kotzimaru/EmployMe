package ru.practicum.android.diploma.util.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.detail.data.models.DetailRequest
import ru.practicum.android.diploma.detail.data.models.SimilarRequest
import ru.practicum.android.diploma.filter.data.model.CountriesResponse
import ru.practicum.android.diploma.filter.data.model.FilterRequest
import ru.practicum.android.diploma.filter.data.model.IndustriesResponse
import ru.practicum.android.diploma.filter.data.model.RegionsResponse
import ru.practicum.android.diploma.search.data.models.Response
import ru.practicum.android.diploma.search.data.models.ResponseCodes
import ru.practicum.android.diploma.search.data.models.SearchRequest

class RetrofitNetworkClient(
    private val validator: InternetConnectionValidator
) : NetworkClient {

    override var lock = Any()

    private val retrofitHh =
        Retrofit.Builder()
            .baseUrl(BASE_HH_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val hhService = retrofitHh.create(HhApiVacancy::class.java)

    override suspend fun doRequest(dto: Any): Response {
        if (!validator.isConnected()) return Response()

        return when (dto) {
            is SearchRequest -> getVacancies(dto)

            is DetailRequest -> getVacancy(dto)

            is SimilarRequest -> getSimilarVacancies(dto)

            is FilterRequest.Industries -> getIndustries()

            is FilterRequest.Countries -> getCountries()

            is FilterRequest.Regions -> getRegions()

            is FilterRequest.RegionsByCountry -> getRegionsByCountry(dto)

            else -> {
                Response().apply { resultCode = ResponseCodes.ERROR }
            }
        }
    }

    private suspend fun getCountries() = try {
        val resp = hhService.getCountries()
        val response = CountriesResponse(resp)
        response.apply {
            resultCode = ResponseCodes.SUCCESS
        }
    } catch (e: Exception) {
        Response().apply { resultCode = ResponseCodes.ERROR }
    }

    private suspend fun getRegions() = try {
        val resp = hhService.getRegions()
        val response = RegionsResponse(resp)
        response.apply {
            resultCode = ResponseCodes.SUCCESS
        }
    } catch (e: Exception) {
        Response().apply { resultCode = ResponseCodes.ERROR }
    }

    private suspend fun getIndustries() = try {
        val resp = hhService.getIndustries()
        val response = IndustriesResponse(resp)
        response.apply {
            resultCode = ResponseCodes.SUCCESS
        }
    } catch (e: Exception) {
        Response().apply { resultCode = ResponseCodes.ERROR }
    }

    private suspend fun getRegionsByCountry(dto: FilterRequest.RegionsByCountry) =
        try {
            val resp = hhService.getRegionsByCountry(dto.countryId)
            val response = RegionsResponse(resp.areas!!)
            response.apply {
                resultCode = ResponseCodes.SUCCESS
            }
        } catch (e: Exception) {
            Response().apply { resultCode = ResponseCodes.ERROR }
        }

    private suspend fun getSimilarVacancies(dto: SimilarRequest) = try {
        val resp = hhService.getSimilarVacancies(dto.vacancy)
        Response().apply {
            resultCode = ResponseCodes.SUCCESS
            data = resp
        }
    } catch (e: Exception) {
        Response().apply { resultCode = ResponseCodes.ERROR }
    }

    private suspend fun getVacancy(dto: DetailRequest) = try {
        val resp = hhService.getDetail(dto.id)
        Response().apply {
            resultCode = ResponseCodes.SUCCESS
            data = resp
        }
    } catch (e: Exception) {
        Response().apply { resultCode = ResponseCodes.ERROR }
    }

    private suspend fun getVacancies(dto: SearchRequest) = try {
        val resp = hhService.getVacancyList(
            options = dto.queryMap
        )
        resp.apply { resultCode = ResponseCodes.SUCCESS }
    } catch (e: Exception) {
        Response().apply { resultCode = ResponseCodes.ERROR }
    }

    companion object {
        const val BASE_HH_API = "https://api.hh.ru/"
    }
}
