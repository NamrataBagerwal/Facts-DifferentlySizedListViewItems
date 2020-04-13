package com.android_test_app.wipro.repository.remote_repository.webservice

import com.android_test_app.wipro.repository.remote_repository.BaseRepository
import com.android_test_app.wipro.repository.remote_repository.webservice.entity.FactsApiResponse
import retrofit2.awaitResponse

class FactsRepositoryImpl(private val factsApi: FactsApi) : BaseRepository() {

    suspend fun getFacts(): FactsApiResponse? {
        return makeApiCall(
            call = { factsApi.getFacts().awaitResponse() },
            errorMessage = "Error Fetching Facts"
        )

    }
}