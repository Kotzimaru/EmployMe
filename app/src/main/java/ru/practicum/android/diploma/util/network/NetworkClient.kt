package ru.practicum.android.diploma.util.network

import ru.practicum.android.diploma.search.data.models.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
