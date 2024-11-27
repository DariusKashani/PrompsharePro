package com.example.promptsharepro

import androidx.test.core.app.ActivityScenario
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
        ActivityScenario.launch(LoginActivity::class.java)
        // Perform valid login
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Verify that the home screen is displayed (logout button exists)
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()))
    }

    @Test
    fun invalidLogin_NonUSCEmail() {
        ActivityScenario.launch(LoginActivity::class.java)
        // Attempt to log in with a non-USC email
        onView(withId(R.id.emailEditText)).perform(typeText("user@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Verify it doesn't log in
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

    @Test
    fun invalidLogin_EmptyFields() {
        ActivityScenario.launch(LoginActivity::class.java)
        // Leave both fields empty and attempt login
        onView(withId(R.id.loginButton)).perform(click())

        // Verify it doesn't log in
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

    @Test
    fun rightEmailWrongLogin() {
        ActivityScenario.launch(LoginActivity::class.java)
        // Enter valid email but incorrect password
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("wrongpassword"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Verify it doesn't log in
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToRegistration() {
        ActivityScenario.launch(LoginActivity::class.java)
        // Click on the register button from the login screen
        onView(withId(R.id.registerButton)).perform(click())

        // Verify that the registration screen is displayed by checking the Full Name field
        onView(withId(R.id.fullNameEditText)).check(matches(isDisplayed()))
    }
    fun emptyFieldsRegistration() {
        ActivityScenario.launch(RegisterActivity::class.java)
        // Fill in the registration form with valid data

        // TODO: I think that maybe its not scrolling ot the registration they keyboard is getting in the way
        onView(withId(R.id.registerButton)).perform(click())
        // Verify that we are still on the register screen
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))
    }
    fun invalidEmailRegistration() {
        ActivityScenario.launch(RegisterActivity::class.java)
        // Fill in the registration form with valid data
        onView(withId(R.id.fullNameEditText)).perform(typeText("Mr Guy"), closeSoftKeyboard())

        onView(withId(R.id.emailEditText)).perform(typeText("Guy@ucla.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        // Verify that the home screen is displayed (logout button exists)
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()))
    }
    fun shortInvalidPasswordRegistration() {
        ActivityScenario.launch(RegisterActivity::class.java)
        // Fill in the registration form with valid data
        onView(withId(R.id.fullNameEditText)).perform(typeText("John Doe"), closeSoftKeyboard())

        onView(withId(R.id.emailEditText)).perform(typeText("john.doe@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("23"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        // Verify that still on register screen
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))
    }
    fun validRegistration() {
        ActivityScenario.launch(RegisterActivity::class.java)
        // Fill in the registration form with valid data
        onView(withId(R.id.fullNameEditText)).perform(typeText("John Doe"), closeSoftKeyboard())

        onView(withId(R.id.emailEditText)).perform(typeText("john.doe@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        // Verify that the home screen is displayed (logout button exists)
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()))

    }
}
