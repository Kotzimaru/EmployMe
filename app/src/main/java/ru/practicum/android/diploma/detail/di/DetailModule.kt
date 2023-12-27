package ru.practicum.android.diploma.detail.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.detail.data.SimilarRepositoryImpl
import ru.practicum.android.diploma.detail.data.VacancyDetailRepositoryImpl
import ru.practicum.android.diploma.detail.domain.DetailVacancyInteractor
import ru.practicum.android.diploma.detail.domain.SimilarInteractor
import ru.practicum.android.diploma.detail.domain.api.SimilarRepository
import ru.practicum.android.diploma.detail.domain.api.VacancyDetailRepository
import ru.practicum.android.diploma.detail.domain.impl.DetailVacancyInteractorImpl
import ru.practicum.android.diploma.detail.domain.impl.SimilarInteractorImpl
import ru.practicum.android.diploma.detail.presentation.detail.DetailViewModel
import ru.practicum.android.diploma.detail.presentation.similar.SimilarViewModel

val detailModule = module {

    single<VacancyDetailRepository> {
        VacancyDetailRepositoryImpl(networkClient = get())
    }

    single<SimilarRepository> {
        SimilarRepositoryImpl(networkClient = get())
    }

    factory<DetailVacancyInteractor> {
        DetailVacancyInteractorImpl(repository = get())
    }

    factory<SimilarInteractor> {
        SimilarInteractorImpl(repository = get())
    }

    viewModel {
        DetailViewModel(
            get(),
            get(),
            get()
        )
    }

    viewModel {
        SimilarViewModel(
            get(),
            get()
        )
    }
}
