package com.example.promptsharepro

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import androidx.test.espresso.assertion.ViewAssertions.matches

class CommentTests {

    @Test
    fun addComment() {
        // Select the first post in the list
        onView(withId(R.id.post_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Type a comment in the comment EditText (using the hint to identify the field)
        onView(withHint("Type here..."))
            .perform(typeText("This is a comment"), closeSoftKeyboard())

        // Click the "Add comment" button (using text to identify the button)
        onView(withText("Add comment")).perform(click())

        // Verify that the comment appears in the list
        onView(withId(R.id.comment_text)) // Matches the newly added comment
            .check(matches(withText("This is a comment")))
    }

    @Test
    fun deleteComment() {
        // Select the first post in the list
        onView(withId(R.id.post_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Perform a swipe action to delete the first comment
        onView(withId(R.id.comment_text)) // Target the comment directly by its ID
            .perform(swipeLeft())

        // Verify that the comment was deleted
        onView(withText("Comment deleted")).check(matches(isDisplayed()))
    }
}
