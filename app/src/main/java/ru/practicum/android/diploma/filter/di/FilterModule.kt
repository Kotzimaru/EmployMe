package ru.practicum.android.diploma.filter.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.filter.data.FilterRepositoryImpl
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.impl.FilterInteractor
import ru.practicum.android.diploma.filter.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterCountryViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterIndustryViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterPlaceWorkViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterRegionViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterViewModel

val filterModule = module {

    single<FilterRepository> {
        FilterRepositoryImpl(get(), get())
    }

    factory<FilterInteractor> {
        FilterInteractorImpl(repository = get())
    }

    viewModel {
        FilterIndustryViewModel(
            get()
        )
    }

    viewModel {
        FilterRegionViewModel(
            get()
        )
    }

    viewModel {
        FilterCountryViewModel(
            get()
        )
    }

    viewModel {
        FilterPlaceWorkViewModel(
            get()
        )
    }

    viewModel {
        FilterViewModel(
            get()
        )
    }
}
