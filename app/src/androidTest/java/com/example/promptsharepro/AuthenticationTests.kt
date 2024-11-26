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
        // Perform valid login
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Verify that the home screen is displayed (logout button exists)
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()))
    }

    @Test
    fun invalidLogin_NonUSCEmail() {
        // Attempt to log in with a non-USC email
        onView(withId(R.id.emailEditText)).perform(typeText("user@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Verify the error message
        onView(withText("Invalid email domain")).check(matches(isDisplayed()))
    }

    @Test
    fun invalidLogin_EmptyFields() {
        // Leave both fields empty and attempt login
        onView(withId(R.id.loginButton)).perform(click())

        // Verify error message for empty fields
        onView(withText("All fields must be filled in")).check(matches(isDisplayed()))
    }

    @Test
    fun invalidLogin_InvalidPassword() {
        // Enter valid email but incorrect password
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("wrongpassword"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Verify error message for incorrect credentials
        onView(withText("Invalid email or password")).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToRegistration() {
        // Click on the register button from the login screen
        onView(withId(R.id.registerButton)).perform(click())

        // Verify that the registration screen is displayed by checking the Full Name field
        onView(withId(R.id.fullNameEditText)).check(matches(isDisplayed()))
    }
}
