package ru.practicum.android.diploma.util

import android.content.Context
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.util.network.InternetConnectionValidator
import ru.practicum.android.diploma.util.network.NetworkClient
import ru.practicum.android.diploma.util.network.ResourceProvider
import ru.practicum.android.diploma.util.network.ResourceProviderImpl
import ru.practicum.android.diploma.util.network.RetrofitNetworkClient
import ru.practicum.android.diploma.util.storage.sharedpreference.SharedPrefStorageClient
import ru.practicum.android.diploma.util.storage.sharedpreference.StorageClient

val UtilModule = module {

    factory { InternetConnectionValidator(androidContext()) }

    single<NetworkClient> {
        RetrofitNetworkClient(validator = get())
    }

    single {
        androidContext().getSharedPreferences("empoyme_preferences", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<StorageClient> {
        SharedPrefStorageClient(sharedPref = get())
    }

    single<ResourceProvider> {
        ResourceProviderImpl(androidContext())
    }
}
