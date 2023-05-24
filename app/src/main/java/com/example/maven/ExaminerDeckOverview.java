package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExaminerDeckOverview extends AppCompatActivity {

    private TextView txtDeckName;
    private ListView lvCards;
    private Button btnCreateCardFromDeck;
    private FirebaseFirestore firestore;
    private List<String> questionList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_deck_cards_overview);

        // Initialize views
        txtDeckName = findViewById(R.id.txtDeckName);
        lvCards = findViewById(R.id.lvCards);
        btnCreateCardFromDeck = findViewById(R.id.btnCreateCardFromDeck);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Retrieve deck name from intent
        Intent intent = getIntent();
        String deckName = intent.getStringExtra("deckName");

        // Set deck name to TextView
        txtDeckName.setText(deckName);

        // Set button click listener
        btnCreateCardFromDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ExaminerCreateCard activity
                Intent intent = new Intent(ExaminerDeckOverview.this, ExaminerCreateCard.class);
                intent.putExtra("deckName", deckName);
                startActivity(intent);
            }
        });

        // Initialize questionList
        questionList = new ArrayList<>();

        // Initialize adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questionList);
        lvCards.setAdapter(adapter);

        // Retrieve questions from Firestore
        CollectionReference deckRef = firestore.collection("decks");
        deckRef.whereEqualTo("name", deckName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String deckId = document.getId();
                                CollectionReference cardRef = deckRef.document(deckId).collection("cards");
                                cardRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot cardDocument : task.getResult()) {
                                                String question = cardDocument.getString("question");
                                                questionList.add(question);
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

        // Set item click listener for the ListView
        lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                // Example:
                String selectedQuestion = questionList.get(position);
                // Do something with the selected question
            }
        });
    }
}
