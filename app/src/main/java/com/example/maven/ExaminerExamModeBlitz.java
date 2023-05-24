package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class ExaminerExamModeBlitz extends AppCompatActivity {

    private TextView timer;
    private TextView question;
    private TextView answer;
    private Button showAnswer;
    private Button correct;
    private Button wrong;

    private List<DocumentSnapshot> cardList;
    private int currentCardIndex;
    private boolean timedMode;
    private int countCorrect;
    private int countWrong;

    public String deckId;

    private CountDownTimer countDownTimer;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_exam_mode_blitz);

        timer = findViewById(R.id.txtTimer);
        question = findViewById(R.id.txtQuestionBlitz);
        answer = findViewById(R.id.txtAnswerBlitz);
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
                                Toast.makeText(ExaminerExamModeBlitz.this, "No cards found", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(ExaminerExamModeBlitz.this, "Failed to retrieve cards", Toast.LENGTH_SHORT).show();
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

        timedMode = true; // Set to your switch state

        if (timedMode) {
            startTimer(3 * 60 * 1000); // Set the time limit in milliseconds
        }
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

    private void startTimer(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timer.setText(String.format("Time remaining: %02d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                finishExam();
            }
        };
        countDownTimer.start();
    }

    private void finishExam() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Pass the countCorrect, countWrong, and deckId to the results activity
        Intent intent = new Intent(ExaminerExamModeBlitz.this, ExaminerExamModeResults.class);
        intent.putExtra("countCorrect", countCorrect);
        intent.putExtra("countWrong", countWrong);
        intent.putExtra("deckId", deckId);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
