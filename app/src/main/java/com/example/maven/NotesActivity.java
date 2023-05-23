package com.example.maven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Intent intent = getIntent();
        if (intent != null) {
            String selectedSubject = intent.getStringExtra("subject");
            if (selectedSubject != null) {
                // Use the selectedSubject to display the corresponding notes
                // For example, fetch the notes from a database or display a placeholder message
                setTitle(selectedSubject + " Notes");
            }
        }
    }
}