package com.example.maven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SubjectsActivity extends AppCompatActivity {

    private ListView subjectsListView;
    private EditText subjectEditText;
    private Button addSubjectButton;
    private ArrayAdapter<String> subjectsAdapter;
    private List<String> subjectsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        // Initialize views
        subjectsListView = findViewById(R.id.listSubjects);
        subjectEditText = findViewById(R.id.editTextSubject);
        addSubjectButton = findViewById(R.id.btnAddSubject);

        // Initialize subjects list
        subjectsList = new ArrayList<>();

        // Initialize subjects adapter
        subjectsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjectsList);

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
        }
    }
}