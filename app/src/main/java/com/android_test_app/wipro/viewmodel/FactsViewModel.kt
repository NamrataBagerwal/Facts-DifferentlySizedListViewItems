package com.android_test_app.wipro.viewmodel

import androidx.lifecycle.*
import com.android_test_app.wipro.repository.remote_repository.webservice.FactsRepositoryImpl
import com.android_test_app.wipro.repository.remote_repository.webservice.entity.FactsApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

class FactsViewModel(private val factsRepository: FactsRepositoryImpl): ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
    get() = parentJob + viewModelScope.coroutineContext + Dispatchers.IO

    private val factsLiveData: MutableLiveData<FactsApiResponse> by lazy {
        MutableLiveData<FactsApiResponse>().apply {
            loadFacts()
        }
    }

    private fun loadFacts(){
        liveData(context = coroutineContext) {
            emit(factsRepository.getFacts())
        }
    }

    fun getFactsLiveData(): LiveData<FactsApiResponse> {
        return factsLiveData
    }

    fun updateFactsLiveData(){
        loadFacts()
    }

    override fun onCleared() {
        super.onCleared()
        if (coroutineContext.isActive){
            coroutineContext.cancelChildren()
        }
        if(parentJob.isActive){
            parentJob.cancel()
        }
    }
}