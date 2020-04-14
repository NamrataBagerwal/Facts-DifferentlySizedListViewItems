package com.android_test_app.wipro.ui.activity

import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.android_test_app.wipro.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class FactsActivityInternetOnInstrumentationTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule
    var activityScenarioRule = activityScenarioRule<FactsActivity>()

    @Test
    fun testListIsLoaded() {
        assertUIComponents()
    }

    private fun assertUIComponents() {
        val recyclerView = onView(withId(R.id.factsRecyclerView))
        recyclerView.check(matches(isDisplayed()))

        val errorTextView = onView(withId(R.id.errorTextView))
        errorTextView.check(matches(not(isDisplayed())))

        val actionBarView = onView(
            Matchers.allOf(
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    0
                )
            )
        )
        actionBarView.check(matches((isDisplayed())))
        actionBarView.check(matches(ViewMatchers.withText("About Canada")))

        val rowTitleView = onView(
            Matchers.allOf(
                withId(R.id.rowTitleTextView),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.factsRecyclerView),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        rowTitleView.check(matches(ViewMatchers.withText("Flag")))
    }

    @Test
    fun testRecreateActivity(){
       val activityScenario: ActivityScenario<FactsActivity> = activityScenarioRule.scenario

        runBlocking(context = Dispatchers.Default) {
            activityScenario.recreate()
            assertUIComponents()
            Thread.sleep(5000)
        }
    }

    @Test
    fun testPullToRefresh(){
        onView(withId(R.id.swipeToRefresh)).perform(swipeDown())
        assertUIComponents()
        Thread.sleep(5000)
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}