package com.android_test_app.wipro.ui.activity

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.rules.activityScenarioRule
import com.android_test_app.wipro.di.DependencyInjectionModule
import com.android_test_app.wipro.repository.remote_repository.webservice.entity.FactsApiResponse
import com.android_test_app.wipro.viewmodel.FactsViewModel
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.*
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class FactsActivityTest: KoinTest {

    private lateinit var activity: FactsActivity

    private lateinit var activityController: ActivityController<FactsActivity>


//    @get:Rule
//    val koinTestRule = KoinTestRule.create {
//        modules(listOf(DependencyInjectionModule.viewModelModule, DependencyInjectionModule.repositoryModule))
//    }

    // required to make your Mock via Koin
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    private val viewModel : FactsViewModel by inject()

    @Mock
    private lateinit var factsLiveData: LiveData<FactsApiResponse>

    @Captor
    private lateinit var teamsObserverCaptor: ArgumentCaptor<Observer<FactsApiResponse>>

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {

        startKoin { modules(listOf(DependencyInjectionModule.viewModelModule, DependencyInjectionModule.repositoryModule)) }

        declareMock<FactsViewModel> ()

//        `when`(viewModel.getFactsLiveData()).thenReturn(factsLiveData)

            activityController = Robolectric.buildActivity(FactsActivity::class.java)
            activity = activityController.get()

            activityController.create()
//        activity.setTestViewModel(viewModel)

//        activityController.start()
            verify(factsLiveData).observe(
                ArgumentMatchers.any(LifecycleOwner::class.java),
                teamsObserverCaptor.capture()
            )
        }

        @Test
        fun `has visible loading view on create`() {
//            assertEquals(View.VISIBLE, activity.contentLoadingProgressBar.visibility)
            println("FactsActivityTest $viewModel")
            Log.d("FactsActivityTest", (viewModel == null).toString())
            assertNotNull(viewModel)
        }

        @After
        fun tearDown() {
            stopKoin()
        }

}
