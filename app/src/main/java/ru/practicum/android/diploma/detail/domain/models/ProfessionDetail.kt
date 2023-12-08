package ru.practicum.android.diploma.detail.domain.models

data class ProfessionDetail(
    val id: String,
    val name: String,
    val employmentId: String?,
    val employmentName: String?,
    val employerId: String,
    val employerName: String,
    val employerLogo: String?,
    val employerCity: String?,
    val experienceId: String?,
    val experienceName: String?,
    val salaryCurrency: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?
)
