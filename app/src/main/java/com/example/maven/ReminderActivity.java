package com.example.maven;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReminderActivity extends AppCompatActivity {
    private List<Reminder> reminderList;
    private RemindersAdapter remindersAdapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        RecyclerView remindersRecyclerView = findViewById(R.id.remindersRecyclerView);
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderList = new ArrayList<>();
        remindersAdapter = new RemindersAdapter(reminderList);
        remindersRecyclerView.setAdapter(remindersAdapter);

        // Load reminders from Firestore
        loadReminders();

        // Add new reminder button click listener
        FloatingActionButton addReminderButton = findViewById(R.id.addReminderButton);
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddReminderDialog();
            }
        });
    }

    private void loadReminders() {
        // Reference to the "reminders" collection in Firestore
        CollectionReference remindersRef = firestore.collection("reminders");

        remindersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    reminderList.clear();
                    Calendar currentDate = Calendar.getInstance(); // Get the current date and time

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Retrieve reminder data from Firestore
                        String reminderText = document.getString("text");
                        String dateTime = document.getString("datetime");

                        // Parse the date and time from the reminder
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        Calendar reminderDate = Calendar.getInstance();
                        try {
                            reminderDate.setTime(sdf.parse(dateTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Check if the reminder date is in the future
                        if (reminderDate.after(currentDate)) {
                            // Create Reminder object
                            Reminder reminder = new Reminder(reminderText, dateTime);

                            // Add reminder to the list
                            reminderList.add(reminder);
                        }
                    }

                    // Sort the reminders by date in ascending order
                    Collections.sort(reminderList, new Comparator<Reminder>() {
                        @Override
                        public int compare(Reminder r1, Reminder r2) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            try {
                                Date date1 = sdf.parse(r1.getDateTime());
                                Date date2 = sdf.parse(r2.getDateTime());
                                return date1.compareTo(date2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

                    // Notify the adapter that the dataset has changed
                    remindersAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ReminderActivity.this, "Failed to load reminders.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*private void showAddReminderDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_reminder, null);
        dialogBuilder.setView(dialogView);

        EditText reminderEditText = dialogView.findViewById(R.id.reminderEditText);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = reminderEditText.getText().toString().trim();
                if (!reminderText.isEmpty()) {
                    // Save the reminder to Firestore
                    saveReminder(reminderText);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(ReminderActivity.this, "Please enter a reminder.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

  /*  private void saveReminder(String reminderText) {
        // Reference to the "reminders" collection in Firestore
        CollectionReference remindersRef = firestore.collection("reminders");

        // Create a new reminder document
        Map<String, Object> reminderData = new HashMap<>();
        reminderData.put("text", reminderText);
        reminderData.put("datetime", "Specific date and time");

        // Add the reminder to Firestore
        remindersRef.add(reminderData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ReminderActivity.this, "Reminder added successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReminderActivity.this, "Failed to add reminder.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/

    // ...

    private void showAddReminderDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_reminder, null);
        dialogBuilder.setView(dialogView);

        EditText reminderEditText = dialogView.findViewById(R.id.reminderEditText);
        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = reminderEditText.getText().toString().trim();
                if (!reminderText.isEmpty()) {
                    // Get selected date and time
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDayOfMonth();
                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();

                    // Format date and time as desired (e.g., "yyyy-MM-dd HH:mm")
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day, hour, minute);
                    String dateTime = sdf.format(calendar.getTime());

                    // Save the reminder to Firestore
                    saveReminder(reminderText, dateTime);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(ReminderActivity.this, "Please enter a reminder.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveReminder(String reminderText, String dateTime) {
        // Reference to the "reminders" collection in Firestore
        CollectionReference remindersRef = firestore.collection("reminders");

        // Create a new reminder document
        Map<String, Object> reminderData = new HashMap<>();
        reminderData.put("text", reminderText);
        reminderData.put("datetime", dateTime);

        // Add the reminder to Firestore
        remindersRef.add(reminderData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ReminderActivity.this, "Reminder added successfully.", Toast.LENGTH_SHORT).show();
                            // Refresh the list of reminders
                            loadReminders();
                        } else {
                            Toast.makeText(ReminderActivity.this, "Failed to add reminder.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

// ...

}
