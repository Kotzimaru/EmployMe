package ru.practicum.android.diploma.search.domain.models

data class Vacancy(
    val id: String,
    val area: String,
    // val department: String,
    val employerImgUrl: String,
    val employer: String,
    val name: String,
    val salary: String,
    val type: String
)
