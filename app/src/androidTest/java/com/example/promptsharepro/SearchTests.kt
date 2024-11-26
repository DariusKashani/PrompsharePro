package com.example.promptsharepro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import androidx.test.espresso.assertion.ViewAssertions.matches


class SearchTests {

    @Test
    fun searchForKeyword() {
        // Enter a valid keyword into the search field
        onView(withId(R.id.search_input))
            .perform(typeText("LLM"), closeSoftKeyboard())

        // Check if the result list contains a post with the keyword
        onView(withId(R.id.post_list))
            .check(matches(hasDescendant(withText("LLM"))))
    }

    @Test
    fun searchNonExistentKeyword() {
        // Enter a non-existent keyword into the search field
        onView(withId(R.id.search_input))
            .perform(typeText("NonExistentKeyword"), closeSoftKeyboard())

        // Verify that "No results found" is displayed
        onView(withText("No results found")).check(matches(isDisplayed()))
    }
}
