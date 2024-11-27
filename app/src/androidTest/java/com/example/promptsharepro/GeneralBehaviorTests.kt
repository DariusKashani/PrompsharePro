package com.example.promptsharepro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import androidx.test.espresso.assertion.ViewAssertions.matches

class GeneralBehaviorTests {

    //TODO: Find out whats not working
    @Test
    fun navigateScreens() {
        // Click the login button and verify the home screen is displayed
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.teamNameTextView))
            .check(matches(withText("TEAM 42.")))

        // Navigate to the "Create Post" screen
        onView(withId(R.id.create_post_button)).perform(click())
        onView(withId(R.id.post_title))
            .check(matches(withText("Title your post")))
    }
}
