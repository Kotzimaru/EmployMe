package ru.practicum.android.diploma.favorite.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorite.data.impl.FavoriteRepositoryImpl
import ru.practicum.android.diploma.favorite.domain.FavoriteInteractor
import ru.practicum.android.diploma.favorite.domain.api.FavoriteRepository
import ru.practicum.android.diploma.favorite.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.favorite.presentation.FavoriteViewModel
import ru.practicum.android.diploma.util.storage.db.AppDatabase
import ru.practicum.android.diploma.util.storage.db.converter.FavoriteDbConverter

val favoriteModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        FavoriteDbConverter()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

    viewModel {
        FavoriteViewModel(
            get()
        )
    }
}
