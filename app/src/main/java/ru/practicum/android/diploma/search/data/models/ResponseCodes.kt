package ru.practicum.android.diploma.search.data.models

const val NO_NET_CONNECTION_CODE = 500
const val SUCCESS_CODE = 200
const val ERROR_CODE = 400

enum class ResponseCodes(val code: Int) {
    NO_NET_CONNECTION(NO_NET_CONNECTION_CODE),
    SUCCESS(SUCCESS_CODE),
    ERROR(ERROR_CODE)
}
