package com.example.promptsharepro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.matches

@RunWith(AndroidJUnit4::class)
class PostManagementTests {

    @Test
    fun createPost() {
        // Click the "Create Post" button
        onView(withId(R.id.create_post_button)).perform(click())

        // Enter a title in the post title EditText (hint matches the XML)
        onView(withHint("ex. Create an iOS Minesweeper app."))
            .perform(typeText("Test Post"), closeSoftKeyboard())

        // Enter content in the notes input EditText (updated hint from XML)
        onView(withHint("Enter prompt and author notes here..."))
            .perform(typeText("This is a test post."), closeSoftKeyboard())

        // Click the "Create Post" button again to submit
        onView(withId(R.id.create_post_button)).perform(click())

        // Verify success message (assume it's a toast message)
        onView(withText("Post created successfully")).check(matches(isDisplayed()))
    }

    @Test
    fun createPost_EmptyContent() {
        // Click the "Create Post" button
        onView(withId(R.id.create_post_button)).perform(click())

        // Enter a title but leave the content field empty
        onView(withHint("ex. Create an iOS Minesweeper app."))
            .perform(typeText("Title"), closeSoftKeyboard())
        onView(withHint("Enter prompt and author notes here..."))
            .perform(clearText(), closeSoftKeyboard()) // Ensure the field is empty

        // Click the "Create Post" button again to submit
        onView(withId(R.id.create_post_button)).perform(click())

        // Verify error message
        onView(withText("Content cannot be empty")).check(matches(isDisplayed()))
    }
}
