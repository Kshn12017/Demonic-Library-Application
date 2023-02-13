package com.example.testlibv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChapterListActivity extends AppCompatActivity {

    List<String> chapterslist = new ArrayList<>();
    List<String> chapterslink = new ArrayList<>();
    ListView chapters_list;

    FirebaseDatabase fdb;
    DatabaseReference dbRef;
    FirebaseFirestore db;

    String novelname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        chapters_list = findViewById(R.id.chapters_list);

        db = FirebaseFirestore.getInstance();
        fdb = FirebaseDatabase.getInstance();
        dbRef = fdb.getReference("Novels");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            novelname = extras.getString("NovelName");
        }

        if (chapterslist.isEmpty()) {
            dbRef.child(novelname).child("Chapter").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> values = new HashMap<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        chapterslist.add(dataSnapshot.getKey());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ChapterListActivity.this, R.layout.dropdown, chapterslist);
                        chapters_list.setAdapter(adapter);
                        chapterslink.add((String) dataSnapshot.getValue());
                        values.put(dataSnapshot.getKey(), dataSnapshot.getValue());

                        chapters_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String chapterNum = (String) chapters_list.getItemAtPosition(i);
                                String chapter = (String) values.get(chapterNum);
                                Intent change = new Intent(ChapterListActivity.this, ChapterActivity.class);
                                change.putExtra("ChapLink", chapter);
                                startActivity(change);
                            }
                        });

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
}