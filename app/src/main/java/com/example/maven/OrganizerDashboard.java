package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerDashboard extends AppCompatActivity implements View.OnClickListener {

    Button btnCreateSubject, btnCalendar, btnToDoList, btnTimer, btnGradesTracker, btnReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer);

        btnCreateSubject = findViewById(R.id.btnCreateSubject);
        btnCreateSubject.setOnClickListener(this);

        btnCalendar = findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(this);

        btnToDoList = findViewById(R.id.btnToDoList);
        btnToDoList.setOnClickListener(this);

        btnTimer = findViewById(R.id.btnTimer);
        btnTimer.setOnClickListener(this);

        btnGradesTracker = findViewById(R.id.btnGradesTracker);
        btnGradesTracker.setOnClickListener(this);

        btnReminders = findViewById(R.id.btnReminders);
        btnReminders.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int click = v.getId();

        if (click == R.id.btnCreateSubject) {
            // Button btnCreateSubject was clicked
            // Perform specific action for btnCreateSubject
            Toast.makeText(OrganizerDashboard.this, "Welcome Subjects page!", Toast.LENGTH_SHORT).show();

            Intent intentSubjects = new Intent(OrganizerDashboard.this, SubjectsActivity.class);
            startActivity(intentSubjects);
        } else if (click == R.id.btnCalendar) {
            // Button btnCalendar was clicked
            // Perform specific action for btnCalendar
        } else if (click == R.id.btnToDoList) {
            // Button btnToDoList was clicked
            // Perform specific action for btnToDoList
        } else if (click == R.id.btnTimer) {
            // Button btnTimer was clicked
            // Perform specific action for btnTimer
        } else if (click == R.id.btnGradesTracker) {
            // Button btnGradesTracker was clicked
            // Perform specific action for btnGradesTracker
        } else if (click == R.id.btnReminders) {
            // Button btnReminders was clicked
            // Perform specific action for btnReminders
        }
    }

}
