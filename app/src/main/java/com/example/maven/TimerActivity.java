package com.example.maven;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    private TextView timerTextView;
    private EditText durationEditText;
    private Button startButton, stopButton, restartButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerTextView = findViewById(R.id.timer_text_view);
        durationEditText = findViewById(R.id.duration_edit_text);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        restartButton = findViewById(R.id.restart_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartTimer();
            }
        });
    }

    private void startTimer() {
        if (!timerRunning) {
            String durationString = durationEditText.getText().toString().trim();

            if (durationString.isEmpty()) {
                return;
            }

            long durationInMinutes = Long.parseLong(durationString);
            long durationInMillis = durationInMinutes * 60 * 1000;
            timeLeftInMillis = durationInMillis;

            countDownTimer = new CountDownTimer(durationInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    timerRunning = false;
                    updateButtons();
                }
            };

            countDownTimer.start();
            timerRunning = true;
            updateButtons();
        }
    }

    private void stopTimer() {
        if (timerRunning) {
            countDownTimer.cancel();
            timerRunning = false;
            updateButtons();
        }
    }

    private void restartTimer() {
        stopTimer();
        startTimer();
    }

    private void updateTimer() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        }

        timerTextView.setText(timeLeftFormatted);
    }

    private void updateButtons() {
        startButton.setEnabled(!timerRunning);
        stopButton.setEnabled(timerRunning);
        restartButton.setEnabled(timerRunning);
    }
}
