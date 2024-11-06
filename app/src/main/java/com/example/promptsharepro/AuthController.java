package com.example.promptsharepro;


import android.view.View;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthController {

    private FirebaseAuth auth;

    public AuthController() {
        auth = FirebaseAuth.getInstance();
    }

    public void registerUser(String email, String password, String uscId, OnCompleteListener<AuthResult> onComplete) {
        // Validate USC ID and email
        if (!isValidUscId(uscId) || !isValidUscEmail(email)) {
            onComplete.onComplete(null); // Or handle invalid input
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onComplete);
    }

    public void signInUser(String email, String password, OnCompleteListener<AuthResult> onComplete) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onComplete);
    }

    public void signOut() {
        auth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    private boolean isValidUscId(String uscId) {
        return uscId.matches("\\d{10}");
    }

    private boolean isValidUscEmail(String email) {
        return email.endsWith("@usc.edu");
    }
}


/*
// In your Activity (e.g., MainActivity)
// ... (other imports)

    private AuthController authController;
    private DatabaseManager databaseManager;

    authController = new AuthController();
      databaseManager = new DatabaseManager();

    // ... (other methods)

    private void createPost() {
        // Get post data from UI elements
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        // ... (get other post data)

        Post post = new Post(authController.getCurrentUser().getUid(), title, content, "Gemini", 5);
        databaseManager.createPost(post, task -> {
            if (task.isSuccessful()) {
                // Post created successfully
            } else {
                // Handle post creation error
            }
        });
    }

    private void searchPosts(String keyword) {
        databaseManager.searchPostsByTitleKeyword(keyword, task -> {
            if (task.isSuccessful()) {
                // Display search results in the UI
            } else {
                // Handle search error
            }
        });
    }

    private void createComment(String postId) {
        // Get comment data from UI elements
        String content = commentEditText.getText().toString();
        // ... (get other comment data)

        Comment comment = new Comment(authController.getCurrentUser().getUid(), content, 5);
        databaseManager.createComment(postId, comment, task -> {
            if (task.isSuccessful()) {
                // Comment created successfully
            } else {
                // Handle comment creation error
            }
        });
    }

    private void deleteComment(String postId, String commentId) {
        databaseManager.deleteComment(postId, commentId, task -> {
            if (task.isSuccessful()) {
                // Comment deleted successfully
            } else {
                // Handle comment deletion error
            }
        });
    }
}

 */

/*
// ... (other imports)

public class LoginActivity extends AppCompatActivity {

    private AuthController authController;
    private DatabaseManager databaseManager;
    private EditText emailEditText, passwordEditText;private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authController = new AuthController();
        databaseManager = new DatabaseManager();

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            progressBar.setVisibility(View.VISIBLE);
            authController.signInUser(email, password, task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // Login successful, navigate to main activity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    // Handle login error
                    Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
 */