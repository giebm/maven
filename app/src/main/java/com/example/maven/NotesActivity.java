package com.example.maven;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NotesActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PERMISSION_REQUEST_CODE = 3;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ListenerRegistration notesListener;
    private List<Note> notesList;
    private ArrayAdapter<Note> notesAdapter;
    private String selectedSubject;
    private String currentNoteId;
    private ImageView photoImageView;

    private EditText noteEditText;
    private Button addNoteButton;
    private Button addPhotoButton;
    private ListView notesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        notesList = new ArrayList<>();
        notesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView = findViewById(R.id.listNotes);
        notesListView.setAdapter(notesAdapter);

        photoImageView = findViewById(R.id.imageViewPhoto);

        noteEditText = findViewById(R.id.editTextNote);
        addNoteButton = findViewById(R.id.btnAddNote);
        addPhotoButton = findViewById(R.id.btnAddPhoto);

        Intent intent = getIntent();
        if (intent != null) {
            selectedSubject = intent.getStringExtra("subject");
            if (selectedSubject != null) {
                setTitle(selectedSubject + " Notes");
                retrieveNotes();
            }
        }

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission()) {
                    openImagePicker();
                } else {
                    requestPermission();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (notesListener != null) {
            notesListener.remove();
        }
    }

    private void retrieveNotes() {
        CollectionReference notesRef = db.collection("notes");
        notesListener = notesRef
                .whereEqualTo("subject", selectedSubject)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("NotesActivity", "Error retrieving notes", e);
                            return;
                        }

                        notesList.clear();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot) {
                                Note note = document.toObject(Note.class);
                                if (note != null) {
                                    note.setId(document.getId());
                                    notesList.add(note);
                                }
                            }
                            notesAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void addNote() {
        EditText noteEditText = findViewById(R.id.editTextNote);
        String noteText = noteEditText.getText().toString().trim();
        if (noteText.isEmpty()) {
            Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the note to Firestore
        CollectionReference notesRef = db.collection("notes");
        Map<String, Object> noteData = new HashMap<>();
        noteData.put("subject", selectedSubject);
        noteData.put("content", noteText); // Add the note content to the noteData map

        if (currentNoteId != null) {
            // Update existing note
            DocumentReference noteRef = notesRef.document(currentNoteId);
            noteRef.update(noteData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NotesActivity.this, "Note updated", Toast.LENGTH_SHORT).show();
                                resetNoteFields();
                            } else {
                                Toast.makeText(NotesActivity.this, "Failed to update note", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Create new note
            notesRef.add(noteData)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NotesActivity.this, "Note added", Toast.LENGTH_SHORT).show();
                                resetNoteFields();
                            } else {
                                Toast.makeText(NotesActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void resetNoteFields() {
        EditText noteEditText = findViewById(R.id.editTextNote);
        noteEditText.setText("");
        currentNoteId = null;
        photoImageView.setImageBitmap(null);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }



    private boolean hasPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        // Request the necessary permission
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            // Specify the location or path within the Firebase Storage bucket
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images");
            StorageReference imageRef = storageRef.child(imageUri.getLastPathSegment());

            // Upload the image file to the specified location
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            // You can perform any additional actions here, such as displaying a success message
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occur during the upload process
                        }
                    });
        }
    }

    private void uploadPhoto(Uri imageUri) {
        if (imageUri != null) {
            final StorageReference photoRef = storageRef.child("photos/" + UUID.randomUUID().toString());

            final UploadTask uploadTask = photoRef.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    // Continue with the task to get the download URL
                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            String photoUrl = downloadUri.toString();
                            savePhotoUrl(photoUrl);
                        }
                    } else {
                        Toast.makeText(NotesActivity.this, "Failed to upload photo", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void savePhotoUrl(String photoUrl) {
        // Save the photo URL to the current note or create a new note
        if (currentNoteId != null) {
            // Update existing note with photo URL
            DocumentReference noteRef = db.collection("notes").document(currentNoteId);
            noteRef.update("photoUrl", photoUrl)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NotesActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NotesActivity.this, "Failed to upload photo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Create new note with photo URL
            CollectionReference notesRef = db.collection("notes");
            Map<String, Object> noteData = new HashMap<>();
            noteData.put("subject", selectedSubject);
            noteData.put("content", "");
            noteData.put("photoUrl", photoUrl);

            notesRef.add(noteData)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NotesActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
                                resetNoteFields();
                            } else {
                                Toast.makeText(NotesActivity.this, "Failed to upload photo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
