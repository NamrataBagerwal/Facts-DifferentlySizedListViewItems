package com.android_test_app.wipro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android_test_app.wipro.repository.remote_repository.webservice.FactsRepositoryImpl
import com.android_test_app.wipro.repository.remote_repository.webservice.entity.FactsApiResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FactsViewModel(private val factsRepository: FactsRepositoryImpl) : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + viewModelScope.coroutineContext + Dispatchers.IO

    private val factsLiveData: MutableLiveData<FactsApiResponse> by lazy {
        MutableLiveData<FactsApiResponse>().apply {
            loadFacts()
        }
    }

    private fun loadFacts() {
        viewModelScope.launch(context = coroutineContext) {
            val facts = factsRepository.getFacts()
            factsLiveData.postValue(facts)
        }
    }

    fun getFactsLiveData(): LiveData<FactsApiResponse> {
        return factsLiveData
    }

    fun updateFactsLiveData() {
        loadFacts()
    }

    override fun onCleared() {
        super.onCleared()
        if (coroutineContext.isActive) {
            coroutineContext.cancelChildren()
        }
        if (parentJob.isActive) {
            parentJob.cancel()
        }
    }
}
