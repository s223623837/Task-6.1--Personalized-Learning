package com.example.learnxperience;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.learnxperience.helper.QuizFetcher;
import com.example.learnxperience.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GeneratedTaskActivity extends AppCompatActivity {

    private LinearLayout containerQuestions;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_task);

        containerQuestions = findViewById(R.id.containerQuestions);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Specify the topic for the quiz
        String topic = "life";

        // Call the fetchQuizData method from QuizFetcher
        QuizFetcher.fetchQuizData(this, topic, new QuizFetcher.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    List<Question> questions = parseQuizJson(response);
                    displayQuestions(questions);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GeneratedTaskActivity.this, "Error parsing quiz data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(GeneratedTaskActivity.this, "Failed to fetch quiz data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set onClickListener for the submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCorrectAnswers();
                // Redirect to DashboardActivity after displaying correct answers
                Intent intent = new Intent(GeneratedTaskActivity.this, DashboardActivty.class);
                startActivity(intent);
                finish(); // Finish current activity to prevent returning to it via back button

            }
        });
    }

    private List<Question> parseQuizJson(String quizJson) throws JSONException {
        List<Question> questions = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(quizJson);

        JSONArray quizArray = jsonObject.getJSONArray("quiz");
        for (int i = 0; i < quizArray.length(); i++) {
            JSONObject questionObject = quizArray.getJSONObject(i);
            String questionText = questionObject.getString("question");
            JSONArray optionsArray = questionObject.getJSONArray("options");

            List<String> options = new ArrayList<>();
            for (int j = 0; j < optionsArray.length(); j++) {
                options.add(optionsArray.getString(j));
            }

            String correctAnswer = questionObject.getString("correct_answer");

            Question question = new Question(questionText, options, correctAnswer);
            questions.add(question);
        }

        return questions;
    }

    private void displayQuestions(List<Question> questions) {
        for (Question question : questions) {
            // Inflate question layout
            View questionLayout = getLayoutInflater().inflate(R.layout.item_question, containerQuestions, false);

            // Set question text
            TextView textViewQuestion = questionLayout.findViewById(R.id.textQuestion);
            textViewQuestion.setText(question.getQuestionText());

            // Get reference to answer options layout
            LinearLayout answerOptionsLayout = questionLayout.findViewById(R.id.answerOptionsLayout);

            // Add answer options dynamically
            for (String option : question.getAnswerOptions()) {
                // Inflate answer option layout
                View optionView = getLayoutInflater().inflate(R.layout.item_answer_option, answerOptionsLayout, false);

                // Set option text
                TextView textViewOption = optionView.findViewById(R.id.textAnswerOption);
                textViewOption.setText(option);

                // Attach Question object to the TextView
                textViewOption.setTag(question);

                // Add click listener to handle option selection
                optionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleOptionSelection(textViewOption, question);
                    }
                });

                // Add optionView to answerOptionsLayout
                answerOptionsLayout.addView(optionView);
            }

            // Add question layout to containerQuestions
            containerQuestions.addView(questionLayout);
        }
    }

    private void handleOptionSelection(TextView textViewOption, Question question) {
        String selectedOption = textViewOption.getText().toString();
        if (!selectedOption.equals(question.getCorrectAnswer())) {
            textViewOption.setTextColor(Color.RED);
        }
    }
    private void displayCorrectAnswers() {
        int correctCount = 0; // Variable to count correct answers

        int childCount = containerQuestions.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View questionLayout = containerQuestions.getChildAt(i);
            if (questionLayout instanceof LinearLayout) {
                LinearLayout answerOptionsLayout = questionLayout.findViewById(R.id.answerOptionsLayout);
                int optionsCount = answerOptionsLayout.getChildCount();
                for (int j = 0; j < optionsCount; j++) {
                    View optionView = answerOptionsLayout.getChildAt(j);
                    if (optionView instanceof TextView) {
                        TextView textViewOption = (TextView) optionView;
                        Question question = (Question) textViewOption.getTag();
                        if (question != null) {
                            if (textViewOption.getCurrentTextColor() == Color.RED) {
                                // Show correct answer for incorrect options
                                String correctAnswer = question.getCorrectAnswer();
                                textViewOption.setText(textViewOption.getText() + " (Correct: " + correctAnswer + ")");
                                textViewOption.setTextColor(Color.GREEN);
                            } else if (textViewOption.getCurrentTextColor() == Color.BLACK) {
                                // Display correct answer for correct options
                                if (textViewOption.getText().toString().equals(question.getCorrectAnswer())) {
                                    textViewOption.setTextColor(Color.GREEN);
                                    correctCount++; // Increment correct count
                                }
                            }
                        }
                    }
                }
            }
        }

        // Display toast message with the number of correct answers
        Toast.makeText(GeneratedTaskActivity.this, "Number of correct answers: " + correctCount, Toast.LENGTH_SHORT).show();

        // Disable button after submission
        buttonSubmit.setEnabled(false);
    }

}
