package com.example.testlibv1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RequestViewActivity extends AppCompatActivity {

    TextInputEditText name, group, description, genre, author, link, position, id;

    FirebaseFirestore db;
    FirebaseStorage fb;
    FirebaseDatabase fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_view);

        name = findViewById(R.id.name);
        group = findViewById(R.id.group);
        description = findViewById(R.id.description);
        genre = findViewById(R.id.genre);
        author = findViewById(R.id.author);
        link = findViewById(R.id.link);
        position = findViewById(R.id.position);
        id = findViewById(R.id.id);

        db = FirebaseFirestore.getInstance();
        fdb = FirebaseDatabase.getInstance();
        fb = FirebaseStorage.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String request = extras.getString("Filename");
            db.collection("Requests").document(request).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    name.setText(documentSnapshot.getString("Novel"));
                    group.setText(documentSnapshot.getString("Group"));
                    description.setText(documentSnapshot.getString("Description"));
                    genre.setText(documentSnapshot.getString("Genre"));
                    author.setText(documentSnapshot.getString("Author"));
                    link.setText(documentSnapshot.getString("Link"));
                    position.setText(documentSnapshot.getString("Position"));
                    id.setText(documentSnapshot.getString("Discord ID"));
                    if(position.getText().toString().isEmpty()){
                        position.setText("N/A");
                    }
                    if(id.getText().toString().isEmpty()){
                        id.setText("N/A");
                    }
                }
            });

        }
    }
}