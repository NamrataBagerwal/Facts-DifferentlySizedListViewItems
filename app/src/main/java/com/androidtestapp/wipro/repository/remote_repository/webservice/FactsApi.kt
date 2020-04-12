package com.androidtestapp.wipro.repository.remote_repository.webservice

import com.androidtestapp.wipro.repository.remote_repository.webservice.entity.FactsApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface FactsApi {

    @GET("/facts")
    fun getFacts(): Deferred<Response<FactsApiResponse>>
}