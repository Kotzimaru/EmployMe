package ru.practicum.android.diploma.detail.data.remote

import com.google.gson.annotations.SerializedName

data class Employment(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)
