package com.android_test_app.wipro.repository.remote_repository.webservice

import com.android_test_app.wipro.AppConstants
import com.android_test_app.wipro.repository.remote_repository.networking_retrofit.RetrofitFactory

object FactsApiFactory {
    val factsApi: FactsApi = RetrofitFactory.retrofit(AppConstants.FACTS_BASE_URL)
        .create(FactsApi::class.java)
}