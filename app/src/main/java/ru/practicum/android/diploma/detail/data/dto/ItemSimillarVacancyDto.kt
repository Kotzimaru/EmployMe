package ru.practicum.android.diploma.detail.data.dto

import com.google.gson.annotations.SerializedName

data class ItemSimillarVacancyDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("area")
    val area: AreaDetailVacancyDto,
    @SerializedName("salary")
    val salary: SalaryDetailVacancyDto?,
    @SerializedName("employer")
    val employer: EmployerDetailVacancyDto,
)
