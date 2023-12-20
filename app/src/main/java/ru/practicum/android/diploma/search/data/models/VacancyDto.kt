package ru.practicum.android.diploma.search.data.models

import ru.practicum.android.diploma.search.data.models.dto.Area
import ru.practicum.android.diploma.search.data.models.dto.Department
import ru.practicum.android.diploma.search.data.models.dto.Employer
import ru.practicum.android.diploma.search.data.models.dto.Type
import ru.practicum.android.diploma.util.Salary

data class VacancyDto(
    val area: Area,
    val department: Department?,
    val employer: Employer,
    val id: String,
    val name: String,
    val salary: Salary?,
    val type: Type
)
