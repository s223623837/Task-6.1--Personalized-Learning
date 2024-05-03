package com.example.learnxperience;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnxperience.helper.DatabaseHelper;

public class NewAccountActivity extends AppCompatActivity {

        private EditText usernameEditText;
        private EditText emailEditText;
        private EditText confirmEmailEditText;
        private EditText passwordEditText;
        private EditText confirmPasswordEditText;
        private EditText phoneNumberEditText;
        private Button createAccountButton;

        private DatabaseHelper databaseHelper;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_account);
        databaseHelper = new DatabaseHelper(this);

            usernameEditText = findViewById(R.id.editUsername);
            emailEditText = findViewById(R.id.editEmail);
            confirmEmailEditText = findViewById(R.id.editConfirmEmail);
            passwordEditText = findViewById(R.id.editPassword);
            confirmPasswordEditText = findViewById(R.id.editConfirmPassword);
            phoneNumberEditText = findViewById(R.id.editPhoneNumber);
            createAccountButton = findViewById(R.id.buttonCreateAccount);

            createAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = usernameEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String confirmEmail = confirmEmailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String confirmPassword = confirmPasswordEditText.getText().toString();
                    String phoneNumber = phoneNumberEditText.getText().toString();

                    // Perform validation (e.g., check if fields are empty or passwords match)
                    if (username.isEmpty() || email.isEmpty() || confirmEmail.isEmpty() ||
                            password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
                        Toast.makeText(NewAccountActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    } else if (!email.equals(confirmEmail)) {
                        Toast.makeText(NewAccountActivity.this, "Emails do not match", Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(confirmPassword)) {
                        Toast.makeText(NewAccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    } else {
                        // All validation passed, proceed with account creation logic
                        // Insert user data into SQLite database
                        long result = databaseHelper.addUser(username, email, password, phoneNumber);
                        Toast.makeText(NewAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        // Finish the activity after account creation
                        finish();
                    }
                }
            });
        }
    }