package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.util.network.ResourceProvider

object TextUtils {
    fun checkSalaryBorder(salaryDto: Salary?, resourceProvider: ResourceProvider): String {
        var from = ""
        var to = ""

        salaryDto?.from?.let {
            from = "${resourceProvider.getString(R.string.from)} ${addSeparator(it)}"
        }

        salaryDto?.to?.let {
            to = "${resourceProvider.getString(R.string.to)} ${addSeparator(it)}"
        }

        return "$from $to ${checkCurrencyIcon(salaryDto?.currency, resourceProvider)}"
    }

    fun getSalaryString(salaryDto: Salary?, resourceProvider: ResourceProvider): String =
        if (salaryDto == null) {
            resourceProvider.getString(R.string.not_salary)
        } else {
            checkSalaryBorder(salaryDto, resourceProvider)
        }

    fun checkCurrencyIcon(currency: String?, resourceProvider: ResourceProvider): String {
        return when (currency) {
            "RUR" -> resourceProvider.getString(R.string.RUR)
            "USD" -> resourceProvider.getString(R.string.USD)
            "AZN" -> resourceProvider.getString(R.string.AZN)
            "BYR" -> resourceProvider.getString(R.string.BYR)
            "EUR" -> resourceProvider.getString(R.string.EUR)
            "GEL" -> resourceProvider.getString(R.string.GEL)
            "KGS" -> resourceProvider.getString(R.string.KGS)
            "KZT" -> resourceProvider.getString(R.string.KZT)
            "UAH" -> resourceProvider.getString(R.string.UAH)
            "UZS" -> resourceProvider.getString(R.string.UZS)
            else -> return ""
        }
    }
    fun addSeparator(number: Int) = "%,d".format(number).replace(",", " ")
}
