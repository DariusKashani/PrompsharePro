package com.example.promptsharepro;
import android.widget.EditText;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean logInPage = true;

    EditText emailEditText = findViewById(R.id.emailEditText);
    EditText passwordEditText = findViewById(R.id.passwordEditText);
    emailEditText.setText("example@example.com");
    passwordEditText.setText("password123");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        Button register = findViewById(R.id.registerButton); // Updated ID
        Button login = findViewById(R.id.loginButton);       // Updated ID

        emailEditText.addTextChangedListener(new TextWatcher()) {
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){
                // This method is called to notify you that the text is about to be changed
            }

            @Override
            public void onTextChanged (CharSequence s,int start, int before, int count){
                // This method is called for real-time changes to the text
                Log.d("EditText", "Email input changed: " + s.toString());
            }

            @Override
            public void afterTextChanged (Editable s){
                // This method is called after the text has been changed
            }
        });

        passwordEditText.addTextChangedListener(new

        TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){
            }

            @Override
            public void onTextChanged (CharSequence s,int start, int before, int count){
                Log.d("EditText", "Password input changed: " + s.toString());
            }

            @Override
            public void afterTextChanged (Editable s){
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logInPage) {
                    logInPage = false;
                    register.setText("Login");
                    login.setText("Register");
                } else {
                    logInPage = true;
                    register.setText("Register");
                    login.setText("Login");
                }

                Intent intent = new Intent(MainActivity.this, AppActivity.class);
                startActivity(intent);
            }
        });

        // Set an OnClickListener on the login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle login/register states
                if (logInPage) {
                    logInPage = false;
                    register.setText("Login");
                    login.setText("Register");
                } else {
                    logInPage = true;
                    register.setText("Register");
                    login.setText("Login");
                }
            }
        });
    }
}
