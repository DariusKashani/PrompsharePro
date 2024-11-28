package com.example.promptsharepro

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.hamcrest.Matcher
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import kotlin.random.Random
import org.hamcrest.Matchers.allOf


@RunWith(AndroidJUnit4::class)
class CommentTests {

    @Test
    fun searchPostAndClick() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.search_input))
            .perform(typeText("test"), closeSoftKeyboard())
        Thread.sleep(2000)

        onView(withId(R.id.main_scroll_view)).perform(scrollToBottomOrFivePosts())
        onView(withId(R.id.post_list))
            .perform(clickChildViewAtPosition(0, R.id.read_more_button))

        // Verify the "Add Comment" field is visible
        onView(withId(R.id.comment_input)).check(matches(isDisplayed()))
    }

    // TODO: Do this one
    @Test
    fun addComment() {
        ActivityScenario.launch(LoginActivity::class.java)

        // Log in
        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        Thread.sleep(2000) // Allow the login to complete and posts to load

        // Scroll and select the first post
        onView(withId(R.id.main_scroll_view)).perform(scrollToBottomOrFivePosts())
        onView(withId(R.id.post_list))
            .perform(clickChildViewAtPosition(0, R.id.read_more_button))

        // Randomize the comment text
        val randomComment = "Test Comment ${Random.nextInt(1000, 9999)}"

        // Add a new comment
        onView(withId(R.id.comment_input))
            .perform(scrollTo(), typeText(randomComment), closeSoftKeyboard())
        onView(withId(R.id.add_comment_button)).perform(scrollTo(), click())

        // Allow time for the comment to be added
        Thread.sleep(1000)

        // Ensure the RecyclerView has the latest data
        val lastCommentPosition = getLastCommentPosition()

        // Scroll to the last comment
        onView(withId(R.id.comments_recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(lastCommentPosition))

        // Verify the added comment is displayed at the bottom of the RecyclerView
        onView(allOf(withText(randomComment), isDescendantOfA(withId(R.id.comments_recycler_view))))
            .check(matches(isDisplayed()))
    }


    @Test
    fun addEmptyComment() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        Thread.sleep(2000)

        // Scroll and select the first post
        onView(withId(R.id.main_scroll_view)).perform(scrollToBottomOrFivePosts())
        onView(withId(R.id.post_list))
            .perform(clickChildViewAtPosition(0, R.id.read_more_button))

        // Attempt to add an empty comment
        onView(withId(R.id.add_comment_button)).perform(scrollTo(), click())

        // Verify the comment was not added (input field still visible)
        onView(withId(R.id.comment_input)).check(matches(isDisplayed()))
    }

    @Test
    fun upVotePost() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        Thread.sleep(2000)

        // Scroll and select the first post
        onView(withId(R.id.main_scroll_view)).perform(scrollToBottomOrFivePosts())
        onView(withId(R.id.post_list))
            .perform(clickChildViewAtPosition(0, R.id.read_more_button))

        // Save the current rating
        val currentRating = Array(1) { "" }
        onView(withId(R.id.post_rating)).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> = isAssignableFrom(TextView::class.java)
            override fun getDescription(): String = "Get current rating value"
            override fun perform(uiController: UiController, view: View) {
                val textView = view as TextView
                currentRating[0] = textView.text.toString()
            }
        })

        // Increment the rating
        onView(withId(R.id.plus_button)).perform(click())

        // Verify the rating increased by 1
        val expectedRating = (currentRating[0].toInt() + 1).toString()
        onView(withId(R.id.post_rating)).check(matches(withText(expectedRating)))
    }

    @Test
    fun downVotePost() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText))
            .perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        Thread.sleep(2000)

        // Scroll and select the first post
        onView(withId(R.id.main_scroll_view)).perform(scrollToBottomOrFivePosts())
        onView(withId(R.id.post_list))
            .perform(clickChildViewAtPosition(0, R.id.read_more_button))

        // Save the current rating
        val currentRating = Array(1) { "" }
        onView(withId(R.id.post_rating)).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> = isAssignableFrom(TextView::class.java)
            override fun getDescription(): String = "Get current rating value"
            override fun perform(uiController: UiController, view: View) {
                val textView = view as TextView
                currentRating[0] = textView.text.toString()
            }
        })

        // Decrement the rating
        onView(withId(R.id.minus_button)).perform(click())

        // Verify the rating decreased by 1
        val expectedRating = (currentRating[0].toInt() - 1).toString()
        onView(withId(R.id.post_rating)).check(matches(withText(expectedRating)))
    }

    private fun scrollToBottomOrFivePosts(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)
            override fun getDescription(): String = "Scroll to the bottom or next 5 posts"
            override fun perform(uiController: UiController, view: View) {
                view.scrollBy(0, 500) // Adjust based on your layout
                uiController.loopMainThreadUntilIdle()
            }
        }
    }

    private fun clickChildViewAtPosition(position: Int, childId: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)
            override fun getDescription(): String = "Click on child view with id $childId at position $position"
            override fun perform(uiController: UiController, view: View) {
                val childView = view.findViewById<View>(childId)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("View with ID $childId not found"))
                        .build()
                childView.performClick()
            }
        }
    }
}

private fun scrollToBottom(): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)
        override fun getDescription(): String = "Scroll to the bottom of the view"
        override fun perform(uiController: UiController, view: View) {
            view.scrollBy(0, Int.MAX_VALUE) // Scroll to the bottom
            uiController.loopMainThreadUntilIdle()
        }
    }
}

private fun getLastCommentPosition(): Int {
    var itemCount = 0
    onView(withId(R.id.comments_recycler_view)).perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(RecyclerView::class.java)
        override fun getDescription(): String = "Get the last position of comments in RecyclerView."
        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            itemCount = recyclerView.adapter?.itemCount ?: 0
        }
    })
    return itemCount - 1 // Last position is itemCount - 1
}

