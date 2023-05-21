package com.example.maven;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity{

    private static final long SPLASH_SCREEN_DELAY = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the delay
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish(); // Prevents the user from going back to the splash screen
            }
        }, SPLASH_SCREEN_DELAY);
    }

}