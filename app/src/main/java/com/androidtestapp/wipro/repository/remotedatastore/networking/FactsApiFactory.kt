package com.androidtestapp.wipro.repository.remotedatastore.networking

import com.androidtestapp.wipro.AppConstants
import com.androidtestapp.wipro.repository.remotedatastore.networking.retrofit.RetrofitFactory

object FactsApiFactory {
    val factsApi: FactsApi = RetrofitFactory.retrofit(AppConstants.FACTS_BASE_URL)
        .create(FactsApi::class.java)
}