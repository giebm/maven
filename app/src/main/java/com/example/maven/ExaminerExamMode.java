package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExaminerExamMode extends AppCompatActivity {

    private ListView lvDecksQuiz;
    private Switch switchBlitz;
    private boolean isBlitzModeEnabled;
    private FirebaseFirestore firestore;
    private List<String> deckList;
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_exam_mode_decks);

        lvDecksQuiz = findViewById(R.id.lvDecksQuiz);
        switchBlitz = findViewById(R.id.switchBlitz);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize deckList
        deckList = new ArrayList<>();

        // Initialize adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deckList);
        lvDecksQuiz.setAdapter(adapter);

        // Retrieve deck names from Firestore
        firestore.collection("decks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String deckName = document.getString("name");
                                deckList.add(deckName);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        // Set up item click listener for the list view
        lvDecksQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedDeckName = deckList.get(position);
                if (isBlitzModeEnabled) {
                    startBlitzMode(selectedDeckName);
                } else {
                    startNonBlitzMode(selectedDeckName);
                }
            }
        });

        // Set up switch change listener
        switchBlitz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBlitzModeEnabled = isChecked;
                String mode = isChecked ? "Blitz Mode" : "Non-Blitz Mode";
                Toast.makeText(ExaminerExamMode.this, "Switched to " + mode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startBlitzMode(String deckName) {
        // Retrieve the deckId based on the deckName
        firestore.collection("decks")
                .whereEqualTo("name", deckName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentReference deckRef = task.getResult().getDocuments().get(0).getReference();
                            String deckId = deckRef.getId();

                            // Start the Blitz mode activity and pass the deckId
                            Intent intent = new Intent(ExaminerExamMode.this, ExaminerExamModeBlitz.class);
                            intent.putExtra("deckId", deckId);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void startNonBlitzMode(String deckName) {
        // Retrieve the deckId based on the deckName
        firestore.collection("decks")
                .whereEqualTo("name", deckName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentReference deckRef = task.getResult().getDocuments().get(0).getReference();
                            String deckId = deckRef.getId();

                            // Start the Non-Blitz mode activity and pass the deckId
                            Intent intent = new Intent(ExaminerExamMode.this, ExaminerExamModeNonBlitz.class);
                            intent.putExtra("deckId", deckId);
                            startActivity(intent);
                        }
                    }
                });
    }
}
