package com.androidtestapp.wipro.repository.remotedatastore.networking.retrofit

import com.androidtestapp.wipro.AppConstants
import com.androidtestapp.wipro.repository.remotedatastore.networking.FactsApi
import com.androidtestapp.wipro.repository.remotedatastore.networking.retrofit.interceptor.CacheInterceptor
import com.androidtestapp.wipro.repository.remotedatastore.networking.retrofit.interceptor.OfflineCacheInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient().newBuilder()
        .addNetworkInterceptor(CacheInterceptor())
        .addInterceptor(OfflineCacheInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    fun retrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

}