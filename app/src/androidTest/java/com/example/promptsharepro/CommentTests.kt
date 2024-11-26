package com.example.promptsharepro

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test

class CommentTests {

    @Test
    fun addComment() {
        // Select the first post in the list
        onView(withText("Do my CSCI 270 homework for me")).perform(click())

        // Type a comment in the comment EditText (using hint to identify the field)
        onView(withHint("Type here..."))
            .perform(typeText("This is a comment"), closeSoftKeyboard())

        // Click the "Add comment" button (using text to identify the button)
        onView(withText("Add comment")).perform(click())

        // Verify that the success message or comment appears
        onView(withText("Comment added")).check(matches(isDisplayed()))
    }

    @Test
    fun deleteComment() {
        // Select the first post in the list
        onView(withText("Do my CSCI 270 homework for me")).perform(click())

        // Perform a swipe action to delete the first comment in the RecyclerView
        onView(withHint("Comments")) // Adjust this if you have an explicit RecyclerView ID
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, swipeLeft()))

        // Verify that the success message or UI reflects the deletion
        onView(withText("Comment deleted")).check(matches(isDisplayed()))
    }
}
