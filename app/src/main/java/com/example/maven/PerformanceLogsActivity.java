package com.example.maven;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformanceLogsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference logsCollection;
    private List<Log> logsList;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "PerformanceLogsPrefs";
    private static final String LOGS_KEY = "LogsKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_logs);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        logsCollection = db.collection("logs");

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Button addLogsButton = findViewById(R.id.add_logs_button);
        addLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddLogsDialog();
            }
        });

        // Retrieve logs from local storage
        logsList = retrieveLogsFromLocalStorage();

        // Display logs
        displayLogs(logsList);

        // Fetch and update logs from Firestore
        fetchAndDisplayLogs();
    }

    private void showAddLogsDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_logs);

        // Find dialog views
        final EditText subjectEditText = dialog.findViewById(R.id.subject_edit_text);
        final EditText activityEditText = dialog.findViewById(R.id.activity_edit_text);
        final EditText scoreEditText = dialog.findViewById(R.id.score_edit_text);
        final EditText activityPercentageEditText = dialog.findViewById(R.id.activity_percentage_edit_text);

        Button submitButton = dialog.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectEditText.getText().toString();
                String activity = activityEditText.getText().toString();
                int score = Integer.parseInt(scoreEditText.getText().toString());
                int activityPercentage = Integer.parseInt(activityPercentageEditText.getText().toString());

                // Save the logs locally
                Log newLog = new Log(subject, activity, score, activityPercentage);
                logsList.add(newLog);
                saveLogsToLocalStorage(logsList);

                dialog.dismiss();

                // Display updated logs
                displayLogs(logsList);

                // Save the logs to the Firebase Firestore
                saveLogsToFirestore(subject, activity, score, activityPercentage);
            }
        });

        dialog.show();
    }

    private void saveLogsToFirestore(String subject, String activity, int score, int activityPercentage) {
        // Create a new log entry
        Map<String, Object> log = new HashMap<>();
        log.put("subject", subject);
        log.put("activity", activity);
        log.put("score", score);
        log.put("activityPercentage", activityPercentage);

        // Add the log entry to the "logs" collection in Firebase Firestore
        logsCollection.add(log)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Log entry added successfully
                        } else {
                            // Error occurred while adding log entry
                        }
                    }
                });
    }

    private void fetchAndDisplayLogs() {
        logsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Clear existing logs
                    logsList.clear();

                    // Iterate over the retrieved logs
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("subject");
                        String activity = document.getString("activity");
                        Long scoreLong = document.getLong("score");
                        Long activityPercentageLong = document.getLong("activityPercentage");
                        int score = (scoreLong != null) ? scoreLong.intValue() : 0;
                        int activityPercentage = (activityPercentageLong != null) ? activityPercentageLong.intValue() : 0;

                        // Create a new log object
                        Log log = new Log(subject, activity, score, activityPercentage);

                        // Add the log to the list
                        logsList.add(log);
                    }

                    // Save the updated logs to local storage
                    saveLogsToLocalStorage(logsList);

                    // Display the updated logs
                    displayLogs(logsList);
                } else {
                    // Error occurred while fetching logs
                }
            }
        });
    }


    private void saveLogsToLocalStorage(List<Log> logs) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(logs);
        editor.putString(LOGS_KEY, json);
        editor.apply();
    }

    private List<Log> retrieveLogsFromLocalStorage() {
        String json = sharedPreferences.getString(LOGS_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Log>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    private void displayLogs(List<Log> logs) {
        TableLayout tableLayout = findViewById(R.id.logs_table_layout);

        // Clear existing rows from the table
        tableLayout.removeAllViews();

        // Sort the logs list by subject in alphabetical order
        Collections.sort(logs, new Comparator<Log>() {
            @Override
            public int compare(Log log1, Log log2) {
                return log1.getSubject().compareToIgnoreCase(log2.getSubject());
            }
        });

        // Iterate over the sorted logs list and display each log in the table layout
        for (Log log : logs) {
            // Create a new table row
            TableRow row = new TableRow(this);

            // Create text views for each column
            TextView subjectTextView = new TextView(this);
            subjectTextView.setText(log.getSubject());
            TextView activityTextView = new TextView(this);
            activityTextView.setText(log.getActivity());
            TextView scoreTextView = new TextView(this);
            scoreTextView.setText(String.valueOf(log.getScore()));
            TextView activityPercentageTextView = new TextView(this);
            activityPercentageTextView.setText(String.valueOf(log.getActivityPercentage()));

            // Add text views to the row
            row.addView(subjectTextView);
            row.addView(activityTextView);
            row.addView(scoreTextView);
            row.addView(activityPercentageTextView);

            // Add the row to the table layout
            tableLayout.addView(row);
        }
    }


}
