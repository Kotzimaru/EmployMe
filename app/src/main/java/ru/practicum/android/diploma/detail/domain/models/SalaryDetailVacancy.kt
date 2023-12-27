package ru.practicum.android.diploma.detail.domain.models

import java.text.NumberFormat
import java.util.Locale

data class SalaryDetailVacancy(
    val from: Int?,
    val to: Int?,
    val currency: CurrencyDetailVacancy,
    val gross: Boolean = false
) {
    fun getSalaryToText(): String {
        val text = StringBuilder()
        if (from == null && to == null) {
            text.append(SALARY_NOT_SPECIFIED)
        } else if (from == null) {
            text.append("$FROM ${spacedNumber(to)} ${currency.symbol}")
        } else if (to == null) {
            text.append("$FROM ${spacedNumber(from)} ${currency.symbol}")
        } else {
            text.append("$FROM ${spacedNumber(from)} $TO ${spacedNumber(to)} ${currency.symbol}")
        }
        return text.toString()
    }

    private fun spacedNumber(value: Int?): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat
            .format(value)
            .replace(COMMA, SPACE)
    }
}

const val SALARY_NOT_SPECIFIED = "Зарплата не указана"
const val FROM = "от"
const val TO = "до"
const val SPACE = " "
const val COMMA = ","
