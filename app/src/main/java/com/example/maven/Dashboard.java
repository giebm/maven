package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    TextView txtName;
    Button btnOrganizer, btnTester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        txtName = findViewById(R.id.txtName);
        btnOrganizer = findViewById(R.id.btnOrganizer);
        btnTester = findViewById(R.id.btnTester);

        // name of user
        String userName = getIntent().getStringExtra("textValue");
        txtName.setText(userName);

        // button organizer
        btnOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Toast.makeText(Dashboard.this, "Welcome to Maven's Organizer!", Toast.LENGTH_SHORT).show();

//                Intent intentOrganizer = new Intent(Dashboard.this, OrganizerDashboard.class);
//                startActivity(intentOrganizer);
            }
        });

        // button exam prepper
        btnTester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Toast.makeText(Dashboard.this, "Welcome to Maven's Examiner!", Toast.LENGTH_SHORT).show();

//                Intent intentExaminer = new Intent(Dashboard.this, ExaminerDashboard.class);
//                startActivity(intentExaminer);
            }
        });

    };
}
