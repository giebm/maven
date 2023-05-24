package com.example.maven;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ToDoListActivity extends AppCompatActivity {

    private ArrayList<String> tasksList;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private FirebaseFirestore firestore;
    private CollectionReference tasksCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
        tasksCollection = firestore.collection("tasks");

        // Initialize the tasks list and adapter
        tasksList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasksList);

        // Get reference to the ListView
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Load tasks from Firestore
        loadTasks();

        // Handle item click events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String task = tasksList.get(position);
                deleteTask(task);
            }
        });
    }

    // Method to load tasks from Firestore
    private void loadTasks() {
        tasksCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                tasksList.clear();
                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    String task = documentSnapshot.getString("task");
                    tasksList.add(task);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                showToast("Failed to load tasks. Please try again.");
            }
        });
    }

    // Method to add a new task
    private void addTask(String task) {
        Map<String, Object> taskData = new HashMap<>();
        taskData.put("task", task);

        tasksCollection.add(taskData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                showToast("Task added successfully.");
                loadTasks();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                showToast("Failed to add task. Please try again.");
            }
        });
    }

    // Method to delete a task
    private void deleteTask(final String task) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tasksCollection.whereEqualTo("task", task).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot querySnapshot) {
                                        if (!querySnapshot.isEmpty()) {
                                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                            document.getReference().delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            showToast("Task deleted successfully.");
                                                            loadTasks();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(Exception e) {
                                                            showToast("Failed to delete task. Please try again.");
                                                        }
                                                    });
                                        } else {
                                            showToast("Task not found.");
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        showToast("Failed to delete task. Please try again.");
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Method to display toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method to handle the "Add Task" button click event
    public void onAddTaskClicked(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);

        final EditText taskEditText = dialogView.findViewById(R.id.taskEditText);

        new AlertDialog.Builder(this)
                .setTitle("Add Task")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = taskEditText.getText().toString().trim();
                        if (!task.isEmpty()) {
                            addTask(task);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
