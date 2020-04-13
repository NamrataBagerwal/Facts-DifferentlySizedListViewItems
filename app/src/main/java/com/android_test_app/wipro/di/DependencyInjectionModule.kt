package com.android_test_app.wipro.di

import com.android_test_app.wipro.repository.remote_repository.webservice.FactsApi
import com.android_test_app.wipro.repository.remote_repository.webservice.FactsApiFactory
import com.android_test_app.wipro.repository.remote_repository.webservice.FactsRepositoryImpl
import com.android_test_app.wipro.viewmodel.FactsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyInjectionModule {
    /*val diModule = module {
        single {
            val factsApi: FactsApi = FactsApiFactory.factsApi
            return@single FactsRepositoryImpl(factsApi)
        }
        viewModel { FactsViewModel(get()) }
    }*/

    /*val apiModule = module {
       *//* fun provideUserApi(retrofit: Retrofit): UserApi {
            return retrofit.create(UserApi::class.java)
        }*//*

        single { FactsApiFactory.factsApi }
    }*/

    val repositoryModule = module {
        /*fun provideFactsRepository(api: FactsApi): FactsRepositoryImpl {
            return FactsRepositoryImpl(api)
        }

        single { provideFactsRepository(get()) }*/
        single {
            val factsApi: FactsApi = FactsApiFactory.factsApi
            return@single FactsRepositoryImpl(factsApi)
        }
    }

    val viewModelModule = module {
        viewModel { FactsViewModel(get()) }
    }

}