package com.android_test_app.wipro.repository.remote_repository.webservice.entity

// Data Model for the Response returned
data class FactsApiResponse(
    val rows: List<Row>,
    val title: String
)