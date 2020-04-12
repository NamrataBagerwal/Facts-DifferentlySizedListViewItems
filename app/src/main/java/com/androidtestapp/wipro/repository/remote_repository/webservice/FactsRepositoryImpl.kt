package com.androidtestapp.wipro.repository.remote_repository.webservice

import com.androidtestapp.wipro.repository.remote_repository.BaseRepository
import com.androidtestapp.wipro.repository.remote_repository.webservice.entity.FactsApiResponse

class FactsRepositoryImpl(private val factsApi: FactsApi) : BaseRepository() {

    suspend fun getFacts(): FactsApiResponse? {
        return makeApiCall(
            call = { factsApi.getFacts().await() },
            errorMessage = "Error Fetching Facts"
        )

    }
}