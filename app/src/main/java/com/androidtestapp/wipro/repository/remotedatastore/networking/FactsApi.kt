package com.androidtestapp.wipro.repository.remotedatastore.networking

import com.androidtestapp.wipro.repository.remotedatastore.entity.FactsApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface FactsApi {

    @GET("/facts")
    fun getFacts(): Deferred<Response<FactsApiResponse>>
}