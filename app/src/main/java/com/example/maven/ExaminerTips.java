package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ExaminerTips extends AppCompatActivity {

    private Button previous;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examiner_tips);

        previous = findViewById(R.id.btnBack2Dashboard);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ExaminerDashboard activity
                Intent intent = new Intent(ExaminerTips.this, ExaminerDashboard.class);
                startActivity(intent);
            }
        });
    }
}
