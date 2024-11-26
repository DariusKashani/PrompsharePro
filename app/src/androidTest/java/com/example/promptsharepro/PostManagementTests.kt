package com.example.promptsharepro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostManagementTests {

    @Test
    fun createPost() {
        // Click the "Create Post" button (button with text "Create Post")
        onView(withText("Create Post")).perform(click())

        // Enter a title in the post title EditText (hint matches "ex. Create an IOS minesweeper app.")
        onView(withHint("ex. Create an IOS minesweeper app."))
            .perform(typeText("Test Post"), closeSoftKeyboard())

        // Enter content in the post content EditText (hint matches "Enter notes here...")
        onView(withHint("Enter notes here..."))
            .perform(typeText("This is a test post."), closeSoftKeyboard())

        // Click the "Create Post" button again to submit
        onView(withText("Create Post")).perform(click())

        // Verify success message (assumed to appear on screen or as a toast)
        onView(withText("Post created successfully")).check(matches(isDisplayed()))
    }

    @Test
    fun createPost_EmptyContent() {
        // Click the "Create Post" button (button with text "Create Post")
        onView(withText("Create Post")).perform(click())

        // Enter a title but leave the content field empty
        onView(withHint("ex. Create an IOS minesweeper app."))
            .perform(typeText("Title"), closeSoftKeyboard())
        onView(withHint("Enter notes here..."))
            .perform(typeText(""), closeSoftKeyboard())

        // Click the "Create Post" button again to submit
        onView(withText("Create Post")).perform(click())

        // Verify error message (assumed to appear on screen or as a toast)
        onView(withText("Content cannot be empty")).check(matches(isDisplayed()))
    }
}
