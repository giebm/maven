package com.example.maven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText tfName;
    Button btnProceed;

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        tfName = findViewById(R.id.tfName);
        btnProceed = findViewById(R.id.btnProceed);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                userName = tfName.getText().toString();

                Toast.makeText(Login.this, "Welcome to Maven!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Login.this, Dashboard.class);
                intent.putExtra("name", userName);
                intent.putExtra("textValue", userName);
                startActivity(intent);
            }
        });

    }

}
