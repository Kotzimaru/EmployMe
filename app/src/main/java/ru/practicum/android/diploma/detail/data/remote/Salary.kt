package ru.practicum.android.diploma.detail.data.remote

import com.google.gson.annotations.SerializedName

data class Salary(
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("from")
    val from: Int?,
    @SerializedName("gross")
    val gross: Boolean,
    @SerializedName("to")
    val to: Int?
)


