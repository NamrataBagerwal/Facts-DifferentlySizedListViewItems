package com.androidtestapp.wipro.repository.remotedatastore.entity

// Data Model for the Response returned
data class FactsApiResponse(
    val rows: List<Row>,
    val title: String
)