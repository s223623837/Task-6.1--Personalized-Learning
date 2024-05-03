package com.example.learnxperience;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learnxperience.helper.DatabaseHelper;
import com.example.learnxperience.model.User;

public class DashboardActivty extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_activty);

        // Initialize SharedPreferences and DatabaseHelper
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        dbHelper = new DatabaseHelper(this);

        // Retrieve user ID from SharedPreferences
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId != -1) {
            // Retrieve user details from database based on userId
            user = dbHelper.getUserById(userId);

            if (user != null) {
                // Populate user's name in the TextView
                TextView textUserName = findViewById(R.id.textUserName);
                textUserName.setText("Hello, " + user.getUsername());
            } else {
                // Handle case where user details are not found
                // You can display an error message or handle accordingly
            }
        } else {
            // Handle case where user ID is not found in SharedPreferences
            // You can navigate back to the login screen or handle accordingly
        }

        // Find the button arrow ImageView
        ImageView buttonArrow = findViewById(R.id.buttonArrow);

        // Set click listener on button arrow
        buttonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to GeneratedTaskActivity
                Intent intent = new Intent(DashboardActivty.this, GeneratedTaskActivity.class);
                startActivity(intent);
            }
        });
    }
}
