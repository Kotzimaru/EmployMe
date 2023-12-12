package ru.practicum.android.diploma.search.data.models

enum class ResponseCodes(val code: Int) {
    NO_NET_CONNECTION(500),
    SUCCESS(200),
    ERROR(400)
}
