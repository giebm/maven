package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExaminerProgressDashboardDeckList extends AppCompatActivity {

    private ListView deckListView;
    private Button dashboardButton;
    private List<String> deckList;
    private ArrayAdapter<String> deckListAdapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_progress_dashboard_deck_list);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI components
        deckListView = findViewById(R.id.lvDecksProgress);
        dashboardButton = findViewById(R.id.btnDashBoard);
        deckList = new ArrayList<>();
        deckListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deckList);
        deckListView.setAdapter(deckListAdapter);

        // Retrieve existing decks from Firestore
        retrieveDecks();

        // Deck list item click listener
        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDeck = deckList.get(position);
                // Start the ExaminerProgressReport activity and pass the selected deck name
                Intent intent = new Intent(ExaminerProgressDashboardDeckList.this, ExaminerProgressReport.class);
                intent.putExtra("selectedDeck", selectedDeck);
                startActivity(intent);
            }
        });

        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExaminerProgressDashboardDeckList.this, ExaminerDashboard.class);
                startActivity(intent);
                Toast.makeText(ExaminerProgressDashboardDeckList.this, "Back to Dashboard clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrieve existing decks from Firestore
    private void retrieveDecks() {
        CollectionReference decksCollection = firestore.collection("decks");
        decksCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    deckList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String deckName = document.getString("name");
                        deckList.add(deckName);
                    }
                    deckListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ExaminerProgressDashboardDeckList.this, "Failed to retrieve decks.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
