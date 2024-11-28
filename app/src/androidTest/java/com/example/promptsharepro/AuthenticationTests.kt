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

    //TODO: Completed
    @Test
    fun validLogin() {
        ActivityScenario.launch(LoginActivity::class.java)
        // Perform valid login
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Check if the home screen is displayed (logout button exists)
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()))
    }
    //TODO: Completed
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
    //TODO: Completed
    @Test
    fun invalidLogin_EmptyFields() {
        ActivityScenario.launch(LoginActivity::class.java)
        // Leave both fields empty and attempt login
        onView(withId(R.id.loginButton)).perform(click())

        // Check that it doesn't log in
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }
    //TODO: Completed
    @Test
    fun rightEmailWrongLogin() {
        ActivityScenario.launch(LoginActivity::class.java)
        // Enter valid email but incorrect password
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("wrongpassword"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Check if it doesn't log in
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }
    //TODO: Completed
    @Test
    fun navigateToRegistration() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.registerButton)).perform(click())

        // Check if the registration screen is displayed by checking the Full Name field
        onView(withId(R.id.fullNameEditText)).check(matches(isDisplayed()))
    }
    //TODO: Completed
    @Test
    fun emptyFieldsRegistration() {
        ActivityScenario.launch(RegisterActivity::class.java)

        onView(withId(R.id.registerButton)).perform(click())
        // Verify that we are still on the register screen
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))
    }
    //TODO: Completed
    @Test
    fun invalidEmailRegistration() {
        // Launch the RegisterActivity
        ActivityScenario.launch(RegisterActivity::class.java)

        // Fill in the registration form with valid data
        onView(withId(R.id.fullNameEditText))
            .perform(typeText("Mr Guy"), closeSoftKeyboard())

        onView(withId(R.id.emailRegisterEditText))
            .perform(typeText("Guy@ucla.com"), closeSoftKeyboard())

        onView(withId(R.id.uscIdEditText)).perform(typeText("1234567890"), closeSoftKeyboard())

        onView(withId(R.id.passwordRegisterEditText))
            .perform(typeText("password123"), closeSoftKeyboard())

        // Click "Register"
        onView(withId(R.id.registerButton)).perform(click())

        // Verify that the home screen is displayed (logout button exists)
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))
    }

    //TODO: Completed
    @Test
    fun shortInvalidPasswordRegistration() {
        ActivityScenario.launch(RegisterActivity::class.java)
        // Fill in the registration form with valid data
        onView(withId(R.id.fullNameEditText)).perform(typeText("Ms.s guy"), closeSoftKeyboard())
        onView(withId(R.id.emailRegisterEditText)).perform(typeText("Guy@eusc.edu"), closeSoftKeyboard())
        onView(withId(R.id.uscIdEditText)).perform(typeText("1234567890"), closeSoftKeyboard())

        onView(withId(R.id.passwordRegisterEditText)).perform(typeText("23"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        // Verify that still on register screen
        onView(withId(R.id.registerButton)).check(matches(isDisplayed()))
    }
    //TODO: Completed
    @Test
    fun validRegistration() {
        ActivityScenario.launch(RegisterActivity::class.java)

        // Generate random first and last names
        val randomFirstName = (1..10)
            .map { ('a'..'z').random() }
            .joinToString("")
            .replaceFirstChar { it.uppercaseChar() } // Capitalize the first letter

        val randomLastName = (1..10)
            .map { ('a'..'z').random() }
            .joinToString("")
            .replaceFirstChar { it.uppercaseChar() } // Capitalize the first letter

        val randomFullName = "$randomFirstName $randomLastName"

        // Generate a random email with @usc.edu domain
        val randomEmailPrefix = (1..10)
            .map { ('a'..'z').random() }
            .joinToString("")
        val randomEmail = "$randomEmailPrefix@usc.edu"

        // Generate a random USC ID (10-digit number)
        val randomUscId = (1_000_000_000L..9_999_999_999L).random().toString()

        // Generate a random password (8-12 characters with letters and digits)
        val randomPassword = (1..(8..12).random())
            .map { (('a'..'z') + ('A'..'Z') + ('0'..'9')).random() }
            .joinToString("")

        // Fill in the registration form with valid data
        onView(withId(R.id.fullNameEditText)).perform(typeText(randomFullName), closeSoftKeyboard())
        onView(withId(R.id.emailRegisterEditText)).perform(typeText(randomEmail), closeSoftKeyboard())
        onView(withId(R.id.uscIdEditText)).perform(typeText(randomUscId), closeSoftKeyboard())
        onView(withId(R.id.passwordRegisterEditText)).perform(typeText(randomPassword), closeSoftKeyboard())
        onView(withId(R.id.registerButton)).perform(click())
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()))
    }

    //TODO: Completed
    @Test
    fun loginThenLogout() {
        ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()))
        onView(withId(R.id.logout_button)).perform(click())
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

}
