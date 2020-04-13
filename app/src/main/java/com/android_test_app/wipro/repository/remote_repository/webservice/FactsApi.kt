package com.android_test_app.wipro.repository.remote_repository.webservice

import com.android_test_app.wipro.repository.remote_repository.webservice.entity.FactsApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface FactsApi {

    @GET("facts/")
    fun getFacts(): Call<FactsApiResponse>
}