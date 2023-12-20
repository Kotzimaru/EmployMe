package ru.practicum.android.diploma.util.network

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.detail.data.models.DetailRequest
import ru.practicum.android.diploma.detail.data.models.SimilarRequest
import ru.practicum.android.diploma.filter.data.model.FilterRequest
import ru.practicum.android.diploma.search.data.models.Response
import ru.practicum.android.diploma.search.data.models.ResponseCodes
import ru.practicum.android.diploma.search.data.models.SearchRequest

class RetrofitNetworkClient(
    private val validator: InternetConnectionValidator
) : NetworkClient {

    private val retrofitHh =
        Retrofit.Builder()
            .baseUrl(BASE_HH_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val hhService = retrofitHh.create(HhApiVacancy::class.java)

    override suspend fun doRequest(dto: Any): Response {
        if (!validator.isConnected()) return Response()

        return when (dto) {
            is SearchRequest -> try {
                val resp = hhService.getVacancyList(
                    options = dto.queryMap
                )
                resp.apply { resultCode = ResponseCodes.SUCCESS }
            } catch (e: Exception) {
                Response().apply { resultCode = ResponseCodes.ERROR }
            }

            is DetailRequest -> try {
                val resp = hhService.getDetail(dto.id)
                Log.d("TAG results", "resp $resp")
                Response().apply {
                    resultCode = ResponseCodes.SUCCESS
                }
            } catch (e: Exception) {
                Log.d("TAG results", "error $e")
                Response().apply { resultCode = ResponseCodes.ERROR }
            }

            is SimilarRequest -> try {
                val resp = hhService.getSimilarVacancies(dto.vacancy)
                Log.d("TAG results", "resp $resp")
                Response().apply {
                    resultCode = ResponseCodes.SUCCESS
                }
            } catch (e: Exception) {
                Log.d("TAG results", "error $e")
                Response().apply { resultCode = ResponseCodes.ERROR }
            }

            is FilterRequest.Industries -> try {
                val resp = hhService.getIndustries()
                Log.d("TAG results", "resp $resp")
                Response().apply {
                    resultCode = ResponseCodes.SUCCESS
                }
            } catch (e: Exception) {
                Log.d("TAG results", "error $e")
                Response().apply { resultCode = ResponseCodes.ERROR }
            }

            is FilterRequest.Countries -> try {
                val resp = hhService.getCountries()
                Log.d("TAG results", "resp $resp")
                Response().apply {
                    resultCode = ResponseCodes.SUCCESS
                }
            } catch (e: Exception) {
                Log.d("TAG results", "error $e")
                Response().apply { resultCode = ResponseCodes.ERROR }
            }

            is FilterRequest.Countries -> try {
                val resp = hhService.getCountries()
                Log.d("TAG results", "resp $resp")
                Response().apply {
                    resultCode = ResponseCodes.SUCCESS
                    // data = resp
                }
            } catch (e: Exception) {
                Log.d("TAG results", "error $e")
                Response().apply { resultCode = ResponseCodes.ERROR }
            }

            is FilterRequest.Regions -> try {
                val resp = hhService.getRegions()
                Log.d("TAG results", "resp $resp")
                Response().apply {
                    resultCode = ResponseCodes.SUCCESS
                    // data = resp
                }
            } catch (e: Exception) {
                Log.d("TAG results", "error $e")
                Response().apply { resultCode = ResponseCodes.ERROR }
            }

            is FilterRequest.RegionsByCountry -> try {
                val resp = hhService.getRegionsByCountry(dto.countryId)
                Log.d("TAG results", "resp $resp")
                Response().apply {
                    resultCode = ResponseCodes.SUCCESS
                    // data = resp
                }
            } catch (e: Exception) {
                Log.d("TAG results", "error $e")
                Response().apply { resultCode = ResponseCodes.ERROR }
            }

            else -> {
                Log.d("TAG results", "error erred")
                Response().apply { resultCode = ResponseCodes.ERROR }
            }
        }
    }

    companion object {
        const val BASE_HH_API = "https://api.hh.ru/"
    }
}
