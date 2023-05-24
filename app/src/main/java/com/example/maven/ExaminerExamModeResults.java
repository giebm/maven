package com.example.maven;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExaminerExamModeResults extends AppCompatActivity {

    private TextView txtCorrect;
    private TextView txtWrong;
    private Button btnDashboard;

    private int countCorrect;
    private int countWrong;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_exam_mode_results);

        txtCorrect = findViewById(R.id.txtCorrect);
        txtWrong = findViewById(R.id.txtWrong);
        btnDashboard = findViewById(R.id.btnDashboard);

        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            countCorrect = extras.getInt("countCorrect");
            countWrong = extras.getInt("countWrong");

            double average = calculateAverage(countCorrect, countWrong);

            // Display the results
            txtCorrect.setText(String.valueOf(countCorrect));
            txtWrong.setText(String.valueOf(countWrong));

            // Retrieve the deckId from the extras
            String deckId = extras.getString("deckId");

            // Save the results to Firestore
            saveResultsToFirestore(deckId, countCorrect, countWrong, average);
        }
    }

    private double calculateAverage(int correct, int wrong) {
        if (correct + wrong > 0) {
            return (double) correct / (correct + wrong);
        } else {
            return 0.0;
        }
    }

    private void saveResultsToFirestore(String deckId, int countCorrect, int countWrong, double average) {
        // Access the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new document in the "scores" collection within the specific deck
        DocumentReference scoreRef = db.collection("decks")
                .document(deckId)
                .collection("scores")
                .document();

        Score score = new Score(countCorrect, countWrong, average);

        // Save the score document
        scoreRef.set(score)
                .addOnSuccessListener(aVoid -> {
                    // Success! The results have been saved to Firestore
                })
                .addOnFailureListener(e -> {
                    // An error occurred while saving the results
                });
    }
}
