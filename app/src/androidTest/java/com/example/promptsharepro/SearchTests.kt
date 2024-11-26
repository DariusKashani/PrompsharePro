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
        onView(withId(R.id.search_input)) // Assuming "searchField" is "editaramalzeme"
            .perform(typeText("Test Keyword"), closeSoftKeyboard())

        // Click the search button
        onView(withId(R.id.create_post_button)) // Assuming "searchButton" is "btnmalzlist"
            .perform(click())

        // Check if the result list contains a post with the keyword
        onView(withId(R.id.mylist)) // Assuming "postList" is "mylist"
            .check(matches(hasDescendant(withText("Test Keyword"))))
    }

    @Test
    fun searchNonExistentKeyword() {
        // Enter a non-existent keyword into the search field
        onView(withId(R.id.search_input)) // Assuming "searchField" is "editaramalzeme"
            .perform(typeText("NonExistentKeyword"), closeSoftKeyboard())

        // Click the search button
        onView(withId(R.id.create_post_button)) // Assuming "searchButton" is "btnmalzlist"
            .perform(click())

        // Verify that "No results found" is displayed
        onView(withText("No results found")).check(matches(isDisplayed()))
    }
}
