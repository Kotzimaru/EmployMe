package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.detail.di.detailModule
import ru.practicum.android.diploma.search.di.searchModule
import ru.practicum.android.diploma.util.UtilModule
import ru.practicum.android.diploma.util.network.netModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(UtilModule, searchModule, detailModule, netModule)
        }
    }
}
