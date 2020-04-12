package com.android_test_app.wipro.di

import com.android_test_app.wipro.repository.remote_repository.webservice.FactsApi
import com.android_test_app.wipro.repository.remote_repository.webservice.FactsApiFactory
import com.android_test_app.wipro.repository.remote_repository.webservice.FactsRepositoryImpl
import com.android_test_app.wipro.viewmodel.FactsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyInjectionModule {
    val diModule = module {
        single {
            val factsApi: FactsApi = FactsApiFactory.factsApi
            return@single FactsRepositoryImpl(factsApi)
        }
        viewModel { FactsViewModel(get()) }
    }
}