package com.example.maven;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ExaminerDashboard extends AppCompatActivity implements View.OnClickListener {

    Button btnCreateDeck, btnCreateCard, btnExamMode, btnProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer);

        btnCreateDeck = findViewById(R.id.btnCreateSubject);
        btnCreateDeck.setOnClickListener(this);

        btnCreateCard = findViewById(R.id.btnCalendar);
        btnCreateCard.setOnClickListener(this);

        btnExamMode = findViewById(R.id.btnToDoList);
        btnExamMode.setOnClickListener(this);

        btnProgress = findViewById(R.id.btnTimer);
        btnProgress.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int click = v.getId();

        if (click == R.id.btnCreateDeck) {
            // Button btnCreateSubject was clicked
            // Perform specific action for btnCreateSubject
        } else if (click == R.id.btnCreateCard) {
            // Button btnCalendar was clicked
            // Perform specific action for btnCalendar
        } else if (click == R.id.btnExamMode) {
            // Button btnToDoList was clicked
            // Perform specific action for btnToDoList
        } else if (click == R.id.btnProgress) {
            // Button btnTimer was clicked
            // Perform specific action for btnTimer
        }
    }

}
