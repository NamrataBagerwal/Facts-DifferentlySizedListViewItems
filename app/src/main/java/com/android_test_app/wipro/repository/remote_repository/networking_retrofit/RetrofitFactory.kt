package com.android_test_app.wipro.repository.remote_repository.networking_retrofit

import com.android_test_app.wipro.repository.remote_repository.networking_retrofit.interceptor.CacheInterceptor
import com.android_test_app.wipro.repository.remote_repository.networking_retrofit.interceptor.OfflineCacheInterceptor
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
        .build()

}