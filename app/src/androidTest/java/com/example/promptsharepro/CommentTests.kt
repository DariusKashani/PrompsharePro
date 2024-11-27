package com.example.promptsharepro

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import org.hamcrest.Matchers.allOf
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommentTests {

    @Test
    fun addComment() {
        // Step 1: Launch the LoginActivity
        ActivityScenario.launch(LoginActivity::class.java)

        // Step 2: Log in with valid credentials
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // TODO: Selecting the post is not working. I need them to get selected.
        onView(withId(R.id.post_list))
            .perform(actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(0, click()))

        // Step 4: Type a comment in the comment EditText
        onView(withHint("Type here..."))
            .perform(scrollTo(), typeText("This is a new comment"), closeSoftKeyboard())

        // Step 5: Click the "Add comment" button
        onView(withText("Add comment")).perform(scrollTo(), click())

        // TODO: Check if this verification works
        onView(withText("This is a new comment")).check(matches(isDisplayed()))
    }

    @Test
    fun addEmptyComment() {
        // Step 1: Launch the LoginActivity
        ActivityScenario.launch(LoginActivity::class.java)

        // Step 2: Log in with valid credentials
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // TODO: Selecting the post is not working. I need them to get selected.
        onView(withId(R.id.post_list))
            .perform(actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(0, click()))

        // Step 4: Type a comment in the comment EditText
        onView(withHint("Type here..."))
            .perform(scrollTo(), typeText("This is a new comment"), closeSoftKeyboard())

        // Step 5: Click the "Add comment" button
        onView(withText("Add comment")).perform(scrollTo(), click())

        // TODO: Check if this verification works
        // Step 6: Verify that the newly added comment appears
        onView(withText("This is a new comment")).check(matches(isDisplayed()))
    }

    //TODO: Find out what's wrong with this test
    @Test
    fun deleteComment() {
        // Step 1: Launch the LoginActivity
        ActivityScenario.launch(LoginActivity::class.java)

        // Step 2: Log in with valid credentials
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Step 3: Select the first post (with scrolling if needed)
        onView(withId(R.id.post_list))
            .perform(actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(0, click()))

        // Step 4: Swipe to delete the first comment
        onView(allOf(withId(R.id.comment_text), withText("This is a new comment")))
            .perform(scrollTo(), swipeLeft())

        // Step 5: Verify that the comment was deleted
        onView(withText("Comment deleted")).check(matches(isDisplayed()))
    }
}
