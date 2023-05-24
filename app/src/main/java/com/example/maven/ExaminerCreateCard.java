package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ExaminerCreateCard extends AppCompatActivity {

    private EditText tfQuestion;
    private EditText tfAnswer;
    private Button btnCancel;
    private Button btnAddCard;

    private FirebaseFirestore db;
    private CollectionReference deckCollection;
    private DocumentReference cardDocument;

    public String deckId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_create_card);

        db = FirebaseFirestore.getInstance();
        deckCollection = db.collection("decks");

        tfQuestion = findViewById(R.id.tfQuestion);
        tfAnswer = findViewById(R.id.tfAnswer);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddCard = findViewById(R.id.btnAddCard);

        // Retrieve deck name from intent
        Intent intent = getIntent();
        String deckName = intent.getStringExtra("deckName");


        deckCollection.whereEqualTo("name", deckName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String id = documentSnapshot.getId();
                            deckId = id;
                            // Use the deckId as needed
                        } else {
                            // Deck with the specified name not found
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure case
                    }
                });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cancel button click here
                finish();
            }
        });

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = tfQuestion.getText().toString().trim();
                String answer = tfAnswer.getText().toString().trim();

                // Check if both question and answer are not empty
                if (!question.isEmpty() && !answer.isEmpty()) {
                    // Assuming you have a "deckId" variable that holds the ID of the deck
                    cardDocument = deckCollection.document(deckId).collection("cards").document();

                    // Create a map with the question and answer data
                    Map<String, Object> cardData = new HashMap<>();
                    cardData.put("question", question);
                    cardData.put("answer", answer);

                    cardDocument.set(cardData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ExaminerCreateCard.this, "Card added successfully", Toast.LENGTH_SHORT).show();
                                    // Clear the EditText fields
                                    tfQuestion.setText("");
                                    tfAnswer.setText("");

                                    Intent intentCreateDeck = new Intent(ExaminerCreateCard.this, DeckActivity.class);
                                    startActivity(intentCreateDeck);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ExaminerCreateCard.this, "Failed to add card", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(ExaminerCreateCard.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
