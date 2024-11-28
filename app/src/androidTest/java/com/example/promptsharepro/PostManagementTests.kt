package com.example.promptsharepro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.core.app.ActivityScenario
import javax.annotation.MatchesPattern


@RunWith(AndroidJUnit4::class)
class PostManagementTests {

    @Test
    fun createPost() {
        // Step 1: Launch the LoginActivity
        ActivityScenario.launch(LoginActivity::class.java)

        // Step 2: Log in with valid credentials
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Step 3: Navigate to the "Create Post" screen
        onView(withId(R.id.create_post_button)) // Home screen button
            .perform(click())

        // Step 4: Fill in the post title
        onView(withId(R.id.title_input))
            .perform(typeText("Test Post"), closeSoftKeyboard())

        // Step 5: Fill in the LLM field
        onView(withId(R.id.llm_input))
            .perform(typeText("GPT-4"), closeSoftKeyboard())

        // Step 6: Fill in the notes input
        onView(withId(R.id.notes_input))
            .perform(typeText("This is a test post."), closeSoftKeyboard())

        // Step 7: Submit the post
        onView(withId(R.id.create_post_button)) // "Create Post" screen button
            .perform(click())

    }


    @Test
    fun createPost_EmptyFields() {

        // Step 1: Launch the LoginActivity
        ActivityScenario.launch(LoginActivity::class.java)

        // Step 2: Log in with valid credentials
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Step 3: Navigate to the "Create Post" screen
        onView(withId(R.id.create_post_button)) // Home screen button
            .perform(click())

        // Step 4: Don't fill in the post title

        // Step 5: Fill in the LLM field
        onView(withId(R.id.llm_input))
            .perform(typeText("GPT-4"), closeSoftKeyboard())

        // Step 6: Fill in the notes input
        onView(withId(R.id.notes_input))
            .perform(typeText("This is a test post."), closeSoftKeyboard())

        // Step 7: Submit the post
        onView(withId(R.id.create_post_button)) // "Create Post" screen button
            .perform(click())

        // Click the "Create Post" button
        onView(withId(R.id.create_post_button)).perform(click())

    }
}
