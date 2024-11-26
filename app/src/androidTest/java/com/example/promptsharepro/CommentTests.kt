package com.example.promptsharepro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import org.hamcrest.Matchers.allOf

class CommentTests {

    @Test
    fun addComment() {
        // Select the first post in the list
        onView(withId(R.id.post_list))
            .perform(actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(0, click()))

        // Type a comment in the comment EditText (using hint to identify the field)
        onView(withHint("Type here..."))
            .perform(typeText("This is a new comment"), closeSoftKeyboard())

        // Click the "Add comment" button
        onView(withText("Add comment")).perform(click())

        // Verify that the newly added comment appears
        onView(withText("This is a new comment")).check(matches(isDisplayed()))
    }

    @Test
    fun deleteComment() {
        // Select the first post in the list
        onView(withId(R.id.post_list))
            .perform(actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(0, click()))

        // Swipe to delete the first comment (target parent layout or match by content)
        onView(allOf(withId(R.id.comment_text), withText("This is a new comment")))
            .perform(swipeLeft())

        // Verify that the comment was deleted
        onView(withText("Comment deleted")).check(matches(isDisplayed()))
    }
}
