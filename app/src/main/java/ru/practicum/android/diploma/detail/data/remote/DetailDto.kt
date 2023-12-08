package ru.practicum.android.diploma.detail.data.remote

data class DetailDto(
    @SerializedName("area")
    val area: Area,
    @SerializedName("employer")
    val employer: Employer,
    @SerializedName("employment")
    val employment: Employment,
    @SerializedName("experience")
    val experience: Experience?,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("salary")
    val salary: Salary?,
)
