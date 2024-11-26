package com.example.promptsharepro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test

class GeneralBehaviorTests {

    @Test
    fun navigateScreens() {
        // Click the login button and verify the home screen is displayed
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText("TEAM 42.")).check(matches(isDisplayed())) // Assuming "TEAM 42." is on the home screen

        // Navigate to the "Create Post" screen
        onView(withText("Create Post")).perform(click()) // Assuming the button has text "Create Post"
        onView(withText("Title your post")).check(matches(isDisplayed())) // Verify screen by matching text
    }

    @Test
    fun handleNetworkError() {
        // Simulate network error using mock tools
        simulateNetworkError() // You need to implement this using MockWebServer or another tool

        // Click the refresh button to retry
        onView(withId(R.id.btnmalzlist)).perform(click()) // Assuming this is the refresh button

        // Verify the error message is displayed
        onView(withText("Network error occurred")).check(matches(isDisplayed()))
    }
}
