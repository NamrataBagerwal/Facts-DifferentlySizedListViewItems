package com.androidtestapp.wipro.repository.remote_repository.webservice

import com.androidtestapp.wipro.AppConstants
import com.androidtestapp.wipro.repository.remote_repository.networking_retrofit.RetrofitFactory

object FactsApiFactory {
    val factsApi: FactsApi = RetrofitFactory.retrofit(AppConstants.FACTS_BASE_URL)
        .create(FactsApi::class.java)
}