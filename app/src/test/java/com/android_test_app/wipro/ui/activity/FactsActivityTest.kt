package com.android_test_app.wipro.ui.activity

import android.os.Build
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android_test_app.wipro.R
import com.android_test_app.wipro.di.DependencyInjectionModule
import com.android_test_app.wipro.repository.remote_repository.webservice.entity.FactsApiResponse
import com.android_test_app.wipro.repository.remote_repository.webservice.entity.Row
import com.android_test_app.wipro.ui.adapter.FactsAdapter
import com.android_test_app.wipro.viewmodel.FactsViewModel
import kotlinx.android.synthetic.main.activity_facts.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.*
import org.mockito.BDDMockito.given
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FactsActivityTest : KoinTest {

    companion object {
        private const val IS_NOT_VISIBLE = "is not visible"
        private const val IS_VISIBLE = "is visible"
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: FactsViewModel

    private lateinit var activityScenario: ActivityScenario<FactsActivity>

    @Mock
    private lateinit var factsLiveData: LiveData<FactsApiResponse>

    @Captor
    private lateinit var factsObserverCaptor: ArgumentCaptor<Observer<FactsApiResponse>>

    @Before
    fun setUp() {
        koinApplication {
            modules(
                listOf(
                    DependencyInjectionModule.viewModelModule,
                    DependencyInjectionModule.repositoryModule
                )
            )
        }

        viewModel = declareMock {
            given(this.getFactsLiveData()).willReturn(factsLiveData)

        }

        activityScenario = launchActivity()
        activityScenario.moveToState(Lifecycle.State.CREATED)

        verify(factsLiveData, times(1))
            .observe(
                ArgumentMatchers.any(LifecycleOwner::class.java),
                factsObserverCaptor.capture()
            )
    }

    @Test
    fun `has visible content loading progress bar view on create`() {

        activityScenario.onActivity { activity ->
            assertEquals(
                "contentLoadingProgressBar $IS_VISIBLE",
                View.VISIBLE,
                activity.contentLoadingProgressBar.visibility
            )
        }
    }

    @Test
    fun `has hidden recycler view and error view on create`() {
        activityScenario.onActivity { activity ->
            assertEquals(
                "factsRecyclerView $IS_NOT_VISIBLE",
                View.GONE,
                activity.factsRecyclerView.visibility
            )
            assertEquals(
                "errorTextView $IS_NOT_VISIBLE",
                View.GONE,
                activity.errorTextView.visibility
            )
        }
    }

    @Test
    fun `displays facts list when available`() {
        val title = "About Canada"

        val factsApiResponse = getTestResponse(title)

        factsObserverCaptor.value.onChanged(factsApiResponse)

        activityScenario.onActivity { activity ->
            assertDisplayOfFactsList(title, activity, factsApiResponse)
        }

    }

    @Test
    fun `displays error view when factsList is null or empty`() {
        factsObserverCaptor.value.onChanged(null)

        activityScenario.onActivity { activity ->

            val title = activity.getString(R.string.app_name)
            assertEquals("ActionBar title is $title", title, activity.supportActionBar?.title)

            assertEquals(
                "factsRecyclerView $IS_NOT_VISIBLE",
                View.GONE,
                activity.factsRecyclerView.visibility
            )
            assertEquals(
                "contentLoadingProgressBar $IS_NOT_VISIBLE",
                View.GONE,
                activity.contentLoadingProgressBar.visibility
            )
            assertEquals(
                "errorTextView $IS_VISIBLE",
                View.VISIBLE,
                activity.errorTextView.visibility
            )

            assertEquals(
                "FactsList in Adapter is as expected",
                null,
                (activity.factsRecyclerView.adapter as? FactsAdapter)?.factsList
            )

        }
        verify(viewModel).updateFactsLiveData()
    }

    @Test
    fun `pull to refresh updates facts list`() {
        val title = "About Canada"

        val factsApiResponse = getTestResponse(title)

        factsObserverCaptor.value.onChanged(factsApiResponse)

        activityScenario.onActivity { activity ->

            // Assert Facts List and Action Bar Title - Old
            assertDisplayOfFactsList(title, activity, factsApiResponse)

            // Refreshing swipeToRefresh
            activity.swipeToRefresh.isRefreshing = true

            val newTitle = "About Facts"

            val newFactsApiResponse = getTestResponse(newTitle)

            // Setting Api Response with new title
            factsObserverCaptor.value.onChanged(newFactsApiResponse)

            // Assert Facts List and Action Bar Title - New
            assertDisplayOfFactsList(newTitle, activity, newFactsApiResponse)
        }
    }


    @After
    fun tearDown() {
        activityScenario.close()
        stopKoin()
    }

    private fun assertDisplayOfFactsList(
        newTitle: String,
        activity: FactsActivity,
        newFactsApiResponse: FactsApiResponse
    ) {
        assertEquals("ActionBar title is $newTitle", newTitle, activity.supportActionBar?.title)
        assertEquals(
            "factsRecyclerView $IS_VISIBLE",
            View.VISIBLE,
            activity.factsRecyclerView.visibility
        )
        assertEquals(
            "contentLoadingProgressBar $IS_NOT_VISIBLE",
            View.GONE,
            activity.contentLoadingProgressBar.visibility
        )
        assertEquals("errorTextView $IS_NOT_VISIBLE", View.GONE, activity.errorTextView.visibility)

        assertEquals(
            "FactsList in Adapter is as expected",
            newFactsApiResponse.rows,
            (activity.factsRecyclerView.adapter as? FactsAdapter)?.factsList
        )
    }

    private fun getTestResponse(title: String): FactsApiResponse {
        val factsList = listOf(
            Row(
                "Beavers",
                "Beavers are second only to humans in their ability to manipulate and change their environment.",
                "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
            ),
            Row(
                "Housing",
                "Warmer than you might think.",
                "hhttp://icons.iconarchive.com/icons/iconshock/alaska/256/Igloo-icon.png"
            )
        )

        return FactsApiResponse(title = title, rows = factsList)
    }


}
