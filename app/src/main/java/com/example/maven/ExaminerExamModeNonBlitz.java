package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ExaminerExamModeNonBlitz extends AppCompatActivity {

    private TextView question;
    private TextView answer;
    private Button showAnswer;
    private Button correct;
    private Button wrong;

    private List<DocumentSnapshot> cardList;
    private int currentCardIndex;
    private int countCorrect;
    private int countWrong;

    public String deckId;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_exam_mode_non_blitz);

        question = findViewById(R.id.txtQuestionNonBlitz);
        answer = findViewById(R.id.txtAnswerNonBlitz);
        showAnswer = findViewById(R.id.btnShowAnswer);
        correct = findViewById(R.id.btnCorrect);
        wrong = findViewById(R.id.btnWrong);

        firestore = FirebaseFirestore.getInstance();

        // Retrieve the deckId from the intent
        Intent intent = getIntent();
        deckId = intent.getStringExtra("deckId");

        // Retrieve the card data from Firestore
        retrieveCardData(deckId);

        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setVisibility(View.VISIBLE);
                correct.setVisibility(View.VISIBLE);
                wrong.setVisibility(View.VISIBLE);
            }
        });

        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countCorrect++;
                showNextCard();
            }
        });

        wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countWrong++;
                showNextCard();
            }
        });
    }

    private void retrieveCardData(String deckId) {
        // Retrieve the card data from Firestore
        firestore.collection("decks")
                .document(deckId)
                .collection("cards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            if (!documents.isEmpty()) {
                                cardList = documents;
                                startExam();
                            } else {
                                Toast.makeText(ExaminerExamModeNonBlitz.this, "No cards found", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(ExaminerExamModeNonBlitz.this, "Failed to retrieve cards", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void startExam() {
        currentCardIndex = 0;
        countCorrect = 0;
        countWrong = 0;

        showCard(currentCardIndex);
    }

    private void showCard(int index) {
        if (index < cardList.size()) {
            DocumentSnapshot card = cardList.get(index);
            String cardQuestion = card.getString("question");
            String cardAnswer = card.getString("answer");

            question.setText(cardQuestion);
            answer.setText(cardAnswer);

            answer.setVisibility(View.INVISIBLE);
            correct.setVisibility(View.INVISIBLE);
            wrong.setVisibility(View.INVISIBLE);
        } else {
            finishExam();
        }
    }

    private void showNextCard() {
        currentCardIndex++;
        showCard(currentCardIndex);
    }

    private void finishExam() {
        // Pass the countCorrect, countWrong, and deckId to the results activity
        Intent intent = new Intent(ExaminerExamModeNonBlitz.this, ExaminerExamModeResults.class);
        intent.putExtra("countCorrect", countCorrect);
        intent.putExtra("countWrong", countWrong);
        intent.putExtra("deckId", deckId);
        startActivity(intent);

        finish();
    }
}
