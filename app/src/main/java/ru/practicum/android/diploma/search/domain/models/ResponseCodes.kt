package ru.practicum.android.diploma.search.domain.models

enum class ResponseCodes(private val code: Int) {
    NO_NET_CONNECTION(500),
    SUCCESS(200),
    ERROR(400)
}
