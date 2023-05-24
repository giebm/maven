package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ExaminerProgressReport extends AppCompatActivity {

    private TextView txtDeckName;
    private TextView txtBest;
    private TextView txtBetter;
    private TextView txtGood;
    private Button btnToDashboard;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_progress);

        txtDeckName = findViewById(R.id.txtDeckNameProgress);
        txtBest = findViewById(R.id.txtBest);
        txtBetter = findViewById(R.id.txtBetter);
        txtGood = findViewById(R.id.txtGood);
        btnToDashboard = findViewById(R.id.btnToDashboard);

        firestore = FirebaseFirestore.getInstance();

        btnToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExaminerProgressReport.this, ExaminerDashboard.class);
                startActivity(intent);
            }
        });

        // Retrieve the selected deck ID from the intent
        Intent intent = getIntent();
        String deckId = intent.getStringExtra("deckId");

        if (deckId != null) {
            retrieveScores(deckId);
        } else {
            Log.d("ExaminerProgressReport", "Deck ID not provided");
        }
    }

    // Retrieve scores for the selected deck
    private void retrieveScores(String deckId) {
        firestore.collection("decks")
                .document(deckId)
                .collection("scores")
                .orderBy("average", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder bestScores = new StringBuilder();
                            StringBuilder betterScores = new StringBuilder();
                            StringBuilder goodScores = new StringBuilder();

                            int count = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String average = document.getString("average");
                                String score = document.getString("score");

                                if (average != null && score != null) {
                                    String scoreText = score + " (" + average + ")";
                                    switch (count) {
                                        case 1:
                                            bestScores.append(scoreText);
                                            break;
                                        case 2:
                                            betterScores.append(scoreText);
                                            break;
                                        case 3:
                                            goodScores.append(scoreText);
                                            break;
                                    }
                                    count++;
                                }
                            }

                            txtBest.setText(bestScores.toString());
                            txtBetter.setText(betterScores.toString());
                            txtGood.setText(goodScores.toString());
                        } else {
                            Log.d("ExaminerProgressReport", "Failed to retrieve scores", task.getException());
                        }
                    }
                });
    }
}
