package com.lreisdeandrade.marvellapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.lreisdeandrade.marvelapp.ui.home.HomeActivity
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.rules.ActivityScenarioRule

/**
 * Created by gibranlyra on 14/09/17.
 */

class HomeScreenTest {
    /**
     * [IntentsTestRule] is a JUnit [@Rule][Rule] to launch your activity under test.
     *
     *
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    var activityScenarioRule: ActivityScenarioRule<HomeActivity> =
        ActivityScenarioRule(HomeActivity::class.java)

    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests significantly
     * more reliable.
     */

    @Test
    fun showFavoriteTab() {
        onView(withId(R.id.action_favorites)).perform(click())
        onView(withId(R.id.favoriteCharactersRecycler)).check(matches(isDisplayed()))
    }

    @Test
    fun showHomeFragmentTab() {
        onView(withId(R.id.action_home)).perform(click())
        onView(withId(R.id.charactersRecycler)).check(matches(isDisplayed()))
    }
}
