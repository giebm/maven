package com.example.maven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubjectsActivity extends AppCompatActivity {

    private ListView subjectsListView;
    private EditText subjectEditText;
    private Button addSubjectButton;
    private ArrayAdapter<String> subjectsAdapter;
    private List<String> subjectsList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        db = FirebaseFirestore.getInstance();


        // Initialize views
        subjectsListView = findViewById(R.id.listSubjects);
        subjectEditText = findViewById(R.id.editTextSubject);
        addSubjectButton = findViewById(R.id.btnAddSubject);

        // Initialize subjects list
        subjectsList = new ArrayList<>();

        // Initialize subjects adapter
        subjectsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjectsList);

        loadSubjectsFromSharedPreferences();


        // Set adapter to subjects list view
        subjectsListView.setAdapter(subjectsAdapter);

        // Add subject button click listener
        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubject();
            }
        });

        subjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = subjectsList.get(position);
                // Start NotesActivity and pass the selected subject
                Intent intent =new Intent(SubjectsActivity.this, NotesActivity.class);
                intent.putExtra("subject", selectedSubject);
                startActivity(intent);
            }
        });
    }

    private void addSubject() {
        String subject = subjectEditText.getText().toString().trim();
        if (!subject.isEmpty()) {
            subjectsList.add(subject);
            subjectsAdapter.notifyDataSetChanged();
            subjectEditText.setText("");

            // Save subject to Firestore
            db.collection("subjects").document(subject).set(new HashMap<>());
            saveSubjectsToSharedPreferences();

        }
    }

    private void saveSubjectsToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> subjectsSet = new HashSet<>(subjectsList);
        editor.putStringSet("subjects", subjectsSet);
        editor.apply();
    }

    private void loadSubjectsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> subjectsSet = sharedPreferences.getStringSet("subjects", new HashSet<>());
        subjectsList.addAll(subjectsSet);
        subjectsAdapter.notifyDataSetChanged();
    }
}