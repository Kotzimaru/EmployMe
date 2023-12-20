package ru.practicum.android.diploma.search.domain.models

data class Filter(
    var page: Int = 0,
    var request: String = "",
    val area: String?,
    val industry: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean
)
