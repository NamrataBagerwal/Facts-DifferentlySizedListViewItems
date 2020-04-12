package com.android_test_app.wipro

import android.app.Application
import com.android_test_app.wipro.di.DependencyInjectionModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FactsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // get list of all modules
        val diModuleList = listOf(DependencyInjectionModule.diModule)
        // start koin with the module list
        startKoin {
            // Android context
            androidContext(this@FactsApplication)
            // modules
            modules(diModuleList)
        }
    }
}