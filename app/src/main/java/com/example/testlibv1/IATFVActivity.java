package com.example.testlibv1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class IATFVActivity extends AppCompatActivity {

    FirebaseDatabase db;
    FirebaseStorage fb;
    StorageReference storageRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ListView iatfv_list;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iatfvactivity);

        db = FirebaseDatabase.getInstance();

        iatfv_list = findViewById(R.id.iatfv_list);
        list = new ArrayList();

        db.getReference().child("IATFV");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}