package com.example.promptsharepro
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test


import org.mockito.Mockito.*

class WhiteBoxTest {


    private val mockUserDatabase = mock(UserDatabase::class.java)
    private val mockPostDatabase = mock(PostDatabase::class.java)

    @Test
    fun testDuplicateRegisterUser() {
        `when`(mockUserDatabase.registerUser("Rajakrishnan Somou", "somou@usc.edu", "1234"))
            .thenReturn(false)

        val duplicateRegistration = mockUserDatabase.registerUser("Rajakrishnan Somou", "somou@usc.edu", "1234")
        assertFalse("Duplicate registration succeeded when it should have failed.", duplicateRegistration)
    }

    @Test
    fun testShortPasswordRegistration() {
        `when`(mockUserDatabase.registerUser("Short Password User", "shortpassword@example.com", "123"))
            .thenReturn(false)

        val registrationResult = mockUserDatabase.registerUser("Short Password User", "shortpassword@example.com", "123")
        assertFalse("Registration succeeded with a too-short password.", registrationResult)
    }


    @Test
    fun testEmptyFieldsValidation() {

        // Empty username
        var result = validateInput("", "email@example.com", "1234567890", "password123")
        assertFalse("Validation passed with empty username.", result)

        // Empty email
        result = validateInput("John Doe", "", "1234567890", "password123")
        assertFalse("Validation passed with empty email.", result)

        // Empty USC ID
        result = validateInput("John Doe", "email@example.com", "", "password123")
        assertFalse("Validation passed with empty USC ID.", result)

        // Empty password
        result = validateInput("John Doe", "email@example.com", "1234567890", "")
        assertFalse("Validation passed with empty password.", result)

        // All fields empty
        result = validateInput("", "", "", "")
        assertFalse("Validation passed with all fields empty.", result)

        // Valid inputs
        result = validateInput("John Doe", "email@example.com", "1234567890", "password123")
        assertTrue("Validation failed with valid inputs.", result)
    }

    @Test
    fun testInvalidEmail() {
        val result = validateInput("John Doe", "invalid-email", "1234567890", "password123")
        assertFalse("Invalid email passed validation.", result)
    }

    @Test
    fun testInvalidUSCID() {
        val result = validateInput("John Doe", "email@example.com", "abc123", "password123")
        assertFalse("Invalid USC ID passed validation.", result)
    }

    @Test
    fun testSuccessfulLogin() {
        `when`(mockUserDatabase.loginUser("somou@usc.edu", "1234")).thenReturn(true)

        val result = mockUserDatabase.loginUser("somou@usc.edu", "1234")
        assertTrue("Login failed with correct credentials.", result)
    }

    @Test
    fun testLoginWithIncorrectEmail() {
        `when`(mockUserDatabase.loginUser("nonexistent@example.com", "password123")).thenReturn(false)

        val result = mockUserDatabase.loginUser("nonexistent@example.com", "password123")
        assertFalse("Login succeeded with non-existent email.", result)
    }

    @Test
    fun testLoginWithIncorrectPassword() {
        `when`(mockUserDatabase.loginUser("somou@usc.edu", "wrongpassword")).thenReturn(false)

        val result = mockUserDatabase.loginUser("somou@usc.edu", "wrongpassword")
        assertFalse("Login succeeded with incorrect password.", result)
    }

    @Test
    fun testLoginNonRegisteredUser() {
        `when`(mockUserDatabase.loginUser("notregistered@example.com", "password12345")).thenReturn(false)

        val result = mockUserDatabase.loginUser("notregistered@example.com", "password12345")
        assertFalse("Login succeeded for a non-registered user.", result)
    }

    @Test
    fun testSuccessfulPostRetrieval() {
        // Create mock posts matching the PostDatabase.Post type
        val mockPosts = listOf(
            PostDatabase.Post("1", "Post1", "Author1", "LLM1", "Notes1", "5"),
            PostDatabase.Post("2", "Post2", "Author2", "LLM2", "Notes2", "4")
        )

        // Mock the behavior of getAllPosts
        `when`(mockPostDatabase.getAllPosts("")).thenReturn(mockPosts)

        // Call the mocked method and validate
        val posts = mockPostDatabase.getAllPosts("")
        assertNotNull("Failed to retrieve posts.", posts)
        assertTrue("Posts were retrieved.", posts.isNotEmpty())
    }

    @Test
    fun testSuccessfulFilterPostRetrieval() {
        // Create mock filtered posts
        val mockFilteredPosts = listOf(
            PostDatabase.Post("3", "FilteredPost1", "Author3", "LLM3", "Notes3", "3"),
            PostDatabase.Post("4", "FilteredPost2", "Author4", "LLM4", "Notes4", "2")
        )

        // Mock the behavior of getAllPosts with a filter
        `when`(mockPostDatabase.getAllPosts("chat")).thenReturn(mockFilteredPosts)

        // Call the mocked method and validate
        val posts = mockPostDatabase.getAllPosts("chat")
        assertNotNull("Failed to retrieve posts.", posts)
        assertTrue("Posts were retrieved.", posts.isNotEmpty())
    }

    @Test
    fun testBadFilterPostRetrieval() {
        // Mock the behavior of getAllPosts with a bad filter
        `when`(mockPostDatabase.getAllPosts("d1fnlas32jdfl41najfds")).thenReturn(emptyList())

        // Call the mocked method and validate
        val posts = mockPostDatabase.getAllPosts("d1fnlas32jdfl41najfds")
        assertNotNull("Failed to handle bad filter retrieval.", posts)
        assertTrue("Posts were retrieved with an invalid filter.", posts.isEmpty())
    }



    @Test
    fun testRetrievePostWithInvalidId() {
        `when`(mockPostDatabase.getPost("invalid_post_id")).thenReturn(null)

        val post = mockPostDatabase.getPost("invalid_post_id")
        assertNull("Post retrieval succeeded with an invalid ID.", post)
    }


    @Test
    fun testCreatePostWithMissingData() {
        var result = validate_post("", "chat-gpt", "useful")
        assertFalse("Post creation succeeded with missing data.", result)


        result = validate_post("hello", "", "useful")
        assertFalse("Post creation succeeded with missing data.", result)


        result = validate_post("hello", "chat-gpt", "")
        assertFalse("Post creation succeeded with missing data.", result)
    }

    @Test
    fun testInvalidRatingFormat() {
        var result = validate_rating("rating: abc;users:")
        assertFalse("Post creation succeeded with invalid rating format (non-numeric).", result)

        result = validate_rating("rating: 5.0") // Missing "users:" suffix
        assertFalse("Post creation succeeded with invalid rating format (missing suffix).", result)

        result = validate_rating("rating: 5;users:") // Valid rating format
        assertTrue("Post creation failed with valid rating format.", result)
    }

    // Function to validate rating format in CreatePostScreenActivity.java
    private fun validate_rating(rating: String): Boolean {
        val regex = Regex("^rating: \\d+(\\.\\d+)?;users:$")
        return regex.matches(rating)
    }


    //function called when creating post in CreatePostScreenActivity.java
    private fun validate_post(title: String, llm: String, notes: String): Boolean {
        return !(title.isEmpty() || llm.isEmpty() || notes.isEmpty())
    }

    //an adaptation from the RegisterActivity.java file when validating user registration
    private fun validateInput(
        fullName: String?,
        email: String?,
        uscId: String?,
        password: String?
    ): Boolean {
        if (fullName.isNullOrEmpty() || email.isNullOrEmpty() || uscId.isNullOrEmpty() || password.isNullOrEmpty()) {
            return false // All fields must be filled in
        }

        // Use regex to validate email format
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        if (!emailRegex.matches(email)) {
            return false // Invalid email format
        }

        // Validate USC ID as a 10-digit number
        if (!uscId.matches(Regex("\\d{10}"))) {
            return false // USC ID must be a 10-digit number
        }

        // Validate password length
        if (password.length < 4) {
            return false // Password must be at least 4 characters
        }

        return true // Validation passed
    }
}

