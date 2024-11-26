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
        // Select the first post in the list (using the RecyclerView ID)
        onView(withId(R.id.post_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Type a comment in the comment EditText (using the hint to identify the field)
        onView(withHint("Type here..."))
            .perform(typeText("This is a comment"), closeSoftKeyboard())

        // Click the "Add comment" button (using the button's text)
        onView(withText("Add comment")).perform(click())

        // Verify that the success message or the comment itself appears
        onView(withText("Comment added")).check(matches(isDisplayed()))
    }

    @Test
    fun deleteComment() {
        // Select the first post in the list (using the RecyclerView ID)
        onView(withId(R.id.post_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Perform a swipe action to delete the first comment in the RecyclerView
        onView(withId(R.id.comment_list)) // Adjust this to match the ID of your RecyclerView for comments
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, swipeLeft()))

        // Verify that the success message or UI reflects the deletion
        onView(withText("Comment deleted")).check(matches(isDisplayed()))
    }
}
