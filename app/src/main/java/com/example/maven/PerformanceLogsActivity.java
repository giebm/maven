package com.example.maven;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformanceLogsActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private RecyclerView subjectsRecyclerView;
    private Map<String, List<LogItem>> logsMap;
    private SubjectsAdapter subjectsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_logs);

        firestore = FirebaseFirestore.getInstance();
        logsMap = new HashMap<>();

        subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView);
        subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjectsAdapter = new SubjectsAdapter(logsMap);
        subjectsRecyclerView.setAdapter(subjectsAdapter);

        Button addLogsButton = findViewById(R.id.addLogsButton);
        addLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddLogsDialog();
            }
        });

        fetchLogsFromFirestore(); // Fetch logs from Firestore during initialization
    }


    private void fetchLogsFromFirestore() {
        firestore.collection("logs")
                .orderBy("subject")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            logsMap.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                String subject = document.getString("subject");
                                String activity = document.getString("activity");
                                int scorePercentage = document.getLong("scorePercentage").intValue();
                                int activityPercentage = document.getLong("activityPercentage").intValue();

                                LogItem logItem = new LogItem(subject, activity, scorePercentage, activityPercentage);

                                if (logsMap.containsKey(subject)) {
                                    List<LogItem> logsList = logsMap.get(subject);
                                    logsList.add(logItem);
                                } else {
                                    List<LogItem> logsList = new ArrayList<>();
                                    logsList.add(logItem);
                                    logsMap.put(subject, logsList);
                                }
                            }
                            subjectsAdapter.notifyDataSetChanged();
                        } else {
                            // Error fetching logs
                        }
                    }
                });
    }




    private void showAddLogsDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_logs);
        dialog.setCancelable(true);

        Button submitButton = dialog.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText subjectEditText = dialog.findViewById(R.id.subjectEditText);
                EditText activityEditText = dialog.findViewById(R.id.activityEditText);
                EditText scorePercentageEditText = dialog.findViewById(R.id.scorePercentageEditText);
                EditText activityPercentageEditText = dialog.findViewById(R.id.activityPercentageEditText);

                String subject = subjectEditText.getText().toString();
                String activity = activityEditText.getText().toString();
                int scorePercentage = Integer.parseInt(scorePercentageEditText.getText().toString());
                int activityPercentage = Integer.parseInt(activityPercentageEditText.getText().toString());

                saveLogsToFirestore(subject, activity, scorePercentage, activityPercentage);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void saveLogsToFirestore(String subject, String activity, int scorePercentage, int activityPercentage) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("subject", subject);
        logData.put("activity", activity);
        logData.put("scorePercentage", scorePercentage);
        logData.put("activityPercentage", activityPercentage);

        firestore.collection("logs").add(logData);
    }
}
