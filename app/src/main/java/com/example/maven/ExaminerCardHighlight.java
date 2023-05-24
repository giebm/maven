package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExaminerCardHighlight extends AppCompatActivity {

    private TextView questionTextView;
    private TextView answerTextView;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_card_highlight);

        questionTextView = findViewById(R.id.txtQuestionHighlight);
        answerTextView = findViewById(R.id.txtAnswerHighlight);

        firestore = FirebaseFirestore.getInstance();

        // Retrieve the deckId and cardId from the intent
        Intent intent = getIntent();
        String deckId = intent.getStringExtra("deckId");
        String cardId = intent.getStringExtra("cardId");

        // Retrieve the question and answer from Firestore
        firestore.collection("decks")
                .document(deckId)
                .collection("cards")
                .document(cardId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String question = document.getString("question");
                                String answer = document.getString("answer");

                                // Set the retrieved values to the TextViews
                                questionTextView.setText(question);
                                answerTextView.setText(answer);
                            } else {
                                Log.d("ExaminerCardHighlight", "No such document");
                            }
                        } else {
                            Log.d("ExaminerCardHighlight", "get failed with ", task.getException());
                        }
                    }
                });
    }
}
