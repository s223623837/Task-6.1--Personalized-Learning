package com.example.learnxperience.helper;

import android.content.Context;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



public class QuizFetcher {

    public static void fetchQuizData(Context context, String topic, final ApiCallback callback) {
        // Base URL of your Flask server
        String baseUrl = "http://192.168.1.108:5000/getQuiz?topic=";

        // Create full URL by appending the topic parameter
        String apiUrl = baseUrl + topic;

        // Instantiate the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create a StringRequest to make the GET request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Successful response received, invoke the callback
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error occurred, show a toast message or invoke error callback
                        Toast.makeText(context, "Error fetching quiz data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        callback.onError(error);
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }

    // Callback interface to handle API call response
    public interface ApiCallback {
        void onSuccess(String response);
        void onError(VolleyError error);
    }
}
