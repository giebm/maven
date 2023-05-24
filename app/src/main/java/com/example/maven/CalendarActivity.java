package com.example.maven;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private FirebaseFirestore firestore;
    private SimpleDateFormat dateFormatter;
    private RecyclerView remindersRecyclerView;
    private RemindersAdapter reminderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        remindersRecyclerView = findViewById(R.id.remindersRecyclerView);

        firestore = FirebaseFirestore.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        reminderAdapter = new RemindersAdapter(new ArrayList<>());
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        remindersRecyclerView.setAdapter(reminderAdapter);

        // Set a listener for date selection
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Display the selected date
                String selectedDate = dateFormatter.format(new Date(year - 1900, month, dayOfMonth));
                Toast.makeText(CalendarActivity.this, "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();

                // Retrieve reminders for the selected date from Firestore
                retrieveRemindersForDate(selectedDate);
            }
        });
    }

    private void retrieveRemindersForDate(String selectedDate) {
        CollectionReference remindersRef = firestore.collection("reminders");

        Query query = remindersRef.whereGreaterThanOrEqualTo("datetime", selectedDate + " 00:00")
                .whereLessThanOrEqualTo("datetime", selectedDate + " 23:59");

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    List<Reminder> reminders = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String reminderText = document.getString("text");
                        String dateTime = document.getString("datetime");
                        reminders.add(new Reminder(reminderText, dateTime));
                    }
                    reminderAdapter.setReminders(reminders);
                } else {
                    reminderAdapter.clearReminders();
                }
            } else {
                Toast.makeText(CalendarActivity.this, "Failed to retrieve reminders.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
