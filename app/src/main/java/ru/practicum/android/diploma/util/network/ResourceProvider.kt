package ru.practicum.android.diploma.util.network

interface ResourceProvider {
    fun getString(resId: Int): String
}
