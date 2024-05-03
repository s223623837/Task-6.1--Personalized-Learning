package com.example.learnxperience;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learnxperience.helper.DatabaseHelper;
import com.example.learnxperience.model.User;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signUpButton;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        usernameEditText = findViewById(R.id.editUsername);
        passwordEditText = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.buttonLogin);
        signUpButton = findViewById(R.id.buttonSignUp);
        databaseHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Perform authentication logic (e.g., check credentials with backend)
                if (!username.isEmpty() && !password.isEmpty()) {
                    // Authentication successful, navigate to main activity or dashboard
                    boolean isAuthenticated = checkAuthentication(username, password);

                    if (isAuthenticated) {


                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    // Example: Navigate to MainActivity
                    Intent intent = new Intent(MainActivity.this, DashboardActivty.class);
                    startActivity(intent);
                    finish(); // Finish the login activity to prevent back navigation
                         } else {
                        Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Authentication failed, show error message
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Create Account activity
                Intent intent = new Intent(MainActivity.this, NewAccountActivity.class);
                startActivity(intent);
            }
        });

    }
    // Example method to check authentication (replace with your actual authentication logic)
    private boolean checkAuthentication(String username,String password) {

        User authenticatedUser = databaseHelper.authenticateUser(username, password);

        if (authenticatedUser != null) {
            // Authentication successful, store user ID in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", authenticatedUser.getId());
            editor.apply();
            return true;
        } else {
            return false;
        }
    }
}
