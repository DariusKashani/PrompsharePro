package com.example.promptsharepro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.matches

@RunWith(AndroidJUnit4::class)
class AuthenticationTests {

    @Test
    fun validLogin() {
        onView(withId(R.id.emailEditText)).perform(typeText("user@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("validPassword"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Check if the home screen is displayed by seeing if logout button is there
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()))
    }

    @Test
    fun invalidLogin_NonUSCEmail() {
        onView(withId(R.id.emailEditText)).perform(typeText("user@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Check if the error message is displayed
        onView(withText("Invalid email domain")).check(matches(isDisplayed()))
    }
}
