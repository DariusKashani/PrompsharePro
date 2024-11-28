package com.example.promptsharepro

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Matcher
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import android.view.View
import org.hamcrest.Matchers.allOf

@RunWith(AndroidJUnit4::class)
class CommentTests {

    @Test
    fun addComment() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.post_list))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildViewWithId(R.id.read_more_button)
                )
            )

        onView(withHint("Type here..."))
            .perform(scrollTo(), typeText("This is a new comment"), closeSoftKeyboard())
        onView(withText("Add comment")).perform(scrollTo(), click())
        Thread.sleep(1000)
        onView(withText("This is a new comment")).check(matches(isDisplayed()))
    }

    @Test
    fun searchPostAndClick() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.search_input))
            .perform(typeText("test"), closeSoftKeyboard())
        Thread.sleep(2000)

        onView(withId(R.id.post_list))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(R.id.post_list))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildViewWithId(R.id.read_more_button)
                )
            )
    }

    @Test
    fun addEmptyComment() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.post_list))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildViewWithId(R.id.read_more_button)
                )
            )

        onView(withText("Add comment")).perform(scrollTo(), click())
        onView(withText("Comment cannot be empty")).check(matches(isDisplayed()))
    }

    @Test
    fun deleteComment() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.post_list))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildViewWithId(R.id.read_more_button)
                )
            )

        Thread.sleep(1000)
        onView(allOf(withId(R.id.comment_text), withText("This is a new comment")))
            .perform(scrollTo(), swipeLeft())
        onView(withText("Comment deleted")).check(matches(isDisplayed()))
    }

    @Test
    fun upVotePost() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.post_list))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildViewWithId(R.id.read_more_button)
                )
            )

        onView(withId(R.id.plus_button)).perform(click())
        onView(withId(R.id.post_rating)).check(matches(withText("1"))) // Assuming this increments the rating to 1
    }

    private fun clickChildViewWithId(id: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)
        override fun getDescription(): String = "Click on a child view with ID $id"
        override fun perform(uiController: UiController, view: View) {
            val childView = view.findViewById<View>(id)
                ?: throw PerformException.Builder()
                    .withCause(Throwable("View with ID $id not found"))
                    .build()
            childView.performClick()
        }
    }
}
