package com.example.testlibv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NovelHomeActivity extends AppCompatActivity {

    FirebaseDatabase fdb;
    DatabaseReference dbRef;
    FirebaseFirestore db;

    RelativeLayout relative_layout;

    List<String> novellist = new ArrayList<>();
    List<String> novellink = new ArrayList<>();

    ImageView cover;
    TextView noveltitle, genre, author, status, description;
    ListView novel_list;

    String novelname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_home);

        db = FirebaseFirestore.getInstance();
        fdb = FirebaseDatabase.getInstance();
        dbRef = fdb.getReference("Novels");

        LayoutInflater layout = getLayoutInflater();
        View view = layout.inflate(R.layout.novelpage, null);
        relative_layout = findViewById(R.id.relative_layout);
        relative_layout.addView(view);

        cover = view.findViewById(R.id.coverimage);
        noveltitle = view.findViewById(R.id.noveltitle);
        genre = view.findViewById(R.id.genre);
        author = view.findViewById(R.id.author);
        status = view.findViewById(R.id.status);
        description = view.findViewById(R.id.description);
        novel_list = view.findViewById(R.id.novel_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            novelname = extras.getString("NovelName");
        }

        db.collection("Novels").document(novelname).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                noveltitle.setText(documentSnapshot.getString("Title"));
                List<String> Genre = (List<String>) documentSnapshot.get("Genre");
                StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (String genrestr : Genre) {
                    if (first) {
                        first = false;
                        builder.append(genrestr);
                    } else {
                        builder.append(", ");
                        builder.append(genrestr);
                    }
                }
                genre.setText(builder.toString());
                author.setText(documentSnapshot.getString("Author"));
                status.setText(documentSnapshot.getString("Status"));
                description.setText(documentSnapshot.getString("Description"));
            }
        });

        fdb.getReference().child("Novels").child(novelname).child("Cover").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String coverdb = snapshot.getValue(String.class);
                if (coverdb != null) {
                    Glide.with(NovelHomeActivity.this).load(coverdb).into(cover);
                } else {
                    Toast.makeText(NovelHomeActivity.this, "Couldn't get image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        if (novellist.isEmpty()) {
            dbRef.child(novelname).child("Chapter").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> values = new HashMap<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        novellist.add(dataSnapshot.getKey());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(NovelHomeActivity.this, R.layout.dropdown, novellist);
                        novel_list.setAdapter(adapter);
                        novellink.add((String) dataSnapshot.getValue());
                        values.put(dataSnapshot.getKey(), dataSnapshot.getValue());

                        novel_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String chapterNum = (String) novel_list.getItemAtPosition(i);
                                String chapter = (String) values.get(chapterNum);
                                Intent change = new Intent(NovelHomeActivity.this, ChapterActivity.class);
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