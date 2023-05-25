package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExaminerDashboard extends AppCompatActivity implements View.OnClickListener {

    Button btnCreateDeck, btnExamMode, btnProgress, btnTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner);

        btnCreateDeck = findViewById(R.id.btnCreateDeck);
        btnCreateDeck.setOnClickListener(this);

        btnExamMode = findViewById(R.id.btnExamMode);
        btnExamMode.setOnClickListener(this);

        btnProgress = findViewById(R.id.btnProgress);
        btnProgress.setOnClickListener(this);

        btnTips = findViewById(R.id.btnTips);
        btnTips.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int click = v.getId();

        if (click == R.id.btnCreateDeck) {
            Toast.makeText(ExaminerDashboard.this, "Create Deck!", Toast.LENGTH_SHORT).show();
            Intent intentCreateDeck = new Intent(ExaminerDashboard.this, DeckActivity.class);
            startActivity(intentCreateDeck);
        } else if (click == R.id.btnExamMode) {
            Toast.makeText(ExaminerDashboard.this, "Exam!", Toast.LENGTH_SHORT).show();
            Intent intentExam = new Intent(ExaminerDashboard.this, ExaminerExamMode.class);
            startActivity(intentExam);
        } else if (click == R.id.btnProgress) {
            Toast.makeText(ExaminerDashboard.this, "Progress!", Toast.LENGTH_SHORT).show();
            Intent intentProgress = new Intent(ExaminerDashboard.this, ExaminerProgressDashboardDeckList.class);
            startActivity(intentProgress);
        } else if (click == R.id.btnTips) {
                Toast.makeText(ExaminerDashboard.this, "Tips!", Toast.LENGTH_SHORT).show();
                Intent intentTips = new Intent(ExaminerDashboard.this, ExaminerTips.class);
                startActivity(intentTips);
            }
    }

}
