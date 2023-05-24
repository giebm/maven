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
            Toast.makeText(OrganizerDashboard.this, "Calendar", Toast.LENGTH_SHORT).show();
            // Button btnCalendar was clicked
            // Perform specific action for btnCalendar
            Intent intentCalendar= new Intent(OrganizerDashboard.this, CalendarActivity.class);
            startActivity(intentCalendar);
        } else if (click == R.id.btnToDoList) {
            Toast.makeText(OrganizerDashboard.this, "To Do List", Toast.LENGTH_SHORT).show();
            // Button btnToDoList was clicked
            // Perform specific action for btnToDoList
            Intent intentToDoList= new Intent(OrganizerDashboard.this, ToDoListActivity.class);
            startActivity(intentToDoList);
        } else if (click == R.id.btnTimer) {
            Toast.makeText(OrganizerDashboard.this, "Timer", Toast.LENGTH_SHORT).show();
            // Button btnTimer was clicked
            // Perform specific action for btnTimer
        } else if (click == R.id.btnGradesTracker) {
            Toast.makeText(OrganizerDashboard.this, "Grade Tracker", Toast.LENGTH_SHORT).show();
            // Button btnGradesTracker was clicked
            // Perform specific action for btnGradesTracker
        } else if (click == R.id.btnReminders) {
            Toast.makeText(OrganizerDashboard.this, "Reminders!", Toast.LENGTH_SHORT).show();
            // Button btnReminders was clicked
            // Perform specific action for btnReminders
        }
    }

}
