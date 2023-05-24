package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckActivity extends AppCompatActivity {

    private TextView deckNameEditText;
    private Button addDeckButton;
    private Button btnBackToDashboard;
    private ListView deckListView;
    private List<String> deckList;
    private ArrayAdapter<String> deckListAdapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_deck);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        deckNameEditText = findViewById(R.id.tfDeckName);
        addDeckButton = findViewById(R.id.btnAddDeck);
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard);
        deckListView = findViewById(R.id.lvDecks);
        deckList = new ArrayList<>();
        deckListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deckList);
        deckListView.setAdapter(deckListAdapter);

        // Retrieve existing decks from Firestore
        retrieveDecks();

        // Add deck button click listener
        addDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deckName = deckNameEditText.getText().toString().trim();
                if (!deckName.isEmpty()) {
                    createDeck(deckName);
                }
            }
        });

        // Deck list item click listener
        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDeck = deckList.get(position);
                navigateToDeckOverview(selectedDeck);
            }
        });

        btnBackToDashboard.setOnClickListener(v -> {
            // Start the ExaminerDashboard activity
            Intent intent = new Intent(DeckActivity.this, ExaminerDashboard.class);
            startActivity(intent);
        });
    }

    // Retrieve existing decks from Firestore
    private void retrieveDecks() {
        CollectionReference decksCollection = firestore.collection("decks");
        decksCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    deckList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String deckName = document.getString("name");
                        deckList.add(deckName);
                    }
                    deckListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DeckActivity.this, "Failed to retrieve decks.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Create a new deck in Firestore
    private void createDeck(String deckName) {
        Map<String, Object> deckData = new HashMap<>();
        deckData.put("name", deckName);

        firestore.collection("decks").add(deckData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            deckNameEditText.setText("");
                            Toast.makeText(DeckActivity.this, "Deck created.", Toast.LENGTH_SHORT).show();
                            retrieveDecks();
                        } else {
                            Toast.makeText(DeckActivity.this, "Failed to create deck.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // Navigate to DeckOverviewActivity
    private void navigateToDeckOverview(String deckName) {
        Intent intent = new Intent(DeckActivity.this, ExaminerDeckOverview.class);
        intent.putExtra("deckName", deckName);
        startActivity(intent);
    }
}
