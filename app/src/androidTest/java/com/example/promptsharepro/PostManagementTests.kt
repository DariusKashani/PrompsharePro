package com.example.promptsharepro

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import android.view.View
import org.hamcrest.Matcher



@RunWith(AndroidJUnit4::class)
class PostManagementTests {

    @Test
    fun createPost() {
        ActivityScenario.launch(LoginActivity::class.java)

        // Step 1: Log in
        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Step 2: Navigate to "Create Post" screen
        onView(withId(R.id.create_post_button)) // Home screen button
            .perform(click())

        // Randomize post title to ensure unique test data
        val postTitle = "Test Post ${System.currentTimeMillis()}"
        val postLLM = "GPT-4"
        val postNotes = "This is a test post."

        // Step 3: Fill in the form fields
        onView(withId(R.id.title_input))
            .perform(typeText(postTitle), closeSoftKeyboard())
        onView(withId(R.id.llm_input))
            .perform(typeText(postLLM), closeSoftKeyboard())
        onView(withId(R.id.notes_input))
            .perform(typeText(postNotes), closeSoftKeyboard())

        // Step 4: Submit the post
        onView(withId(R.id.create_post_button)) // "Create Post" screen button
            .perform(click())

        // Step 5: Search for the newly created post title
        Thread.sleep(2000) // Allow the feed to refresh
        onView(withId(R.id.search_input))
            .perform(typeText(postTitle), closeSoftKeyboard())

        Thread.sleep(2000) // Allow search results to update

        // Step 6: Click on the first result in the feed
        onView(withId(R.id.post_list))
            .perform(clickChildViewAtPosition(0, R.id.read_more_button))

        // Step 7: Verify that the post details match
        onView(withId(R.id.post_title)).check(matches(withText(postTitle)))
        onView(withId(R.id.post_llm)).check(matches(withText(postLLM)))
        onView(withId(R.id.notes)).check(matches(withText(postNotes)))
    }


    @Test
    fun createPost_EmptyFields() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.emailEditText)).perform(typeText("somou@usc.edu"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.create_post_button))
            .perform(click())

        onView(withId(R.id.llm_input))
            .perform(typeText("GPT-4"), closeSoftKeyboard())
        onView(withId(R.id.notes_input))
            .perform(typeText("This is a test post."), closeSoftKeyboard())

        onView(withId(R.id.create_post_button))
            .perform(click())

        onView(withId(R.id.create_post_button)).check(matches(isDisplayed())) // Ensure we are still on the same screen
    }

    /**
     * Helper function to click on a child view (e.g., "Read More" button) at a specific position in a RecyclerView
     */
    private fun clickChildViewAtPosition(position: Int, childId: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)
            override fun getDescription(): String = "Click on a child view with ID $childId at position $position"
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
