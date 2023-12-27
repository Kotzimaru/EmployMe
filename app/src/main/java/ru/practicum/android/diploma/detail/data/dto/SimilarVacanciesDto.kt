package ru.practicum.android.diploma.detail.data.dto

import com.google.gson.annotations.SerializedName

data class SimilarVacanciesDto(
    @SerializedName("items")
    val itemSimillarVacancyDtos: List<ItemSimillarVacancyDto>,
)
