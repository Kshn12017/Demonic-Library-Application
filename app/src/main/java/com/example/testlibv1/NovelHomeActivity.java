package com.example.testlibv1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testlibv1.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NovelHomeActivity extends AppCompatActivity {

    FirebaseDatabase fdb;
    FirebaseStorage fb;
    StorageReference storageRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference dbRef;
    FirebaseFirestore db;

    RelativeLayout relative_layout;

    ArrayList<String> list;

    List<String> novellist = new ArrayList<>();
    List<String> novellink = new ArrayList<>();

    Button back;
    ImageView cover;
    TextView noveltitle, genre, author, status, description;
    ListView novel_list;

    String chapter;

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

        back = view.findViewById(R.id.back);
        cover = view.findViewById(R.id.coverimage);
        noveltitle = view.findViewById(R.id.noveltitle);
        genre = view.findViewById(R.id.genre);
        author = view.findViewById(R.id.author);
        status = view.findViewById(R.id.status);
        description = view.findViewById(R.id.description);
        novel_list = view.findViewById(R.id.novel_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chapter = extras.getString("ChapName");
        }

        db.collection("Novels").document(chapter).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

        fdb.getReference().child("Novels").child(chapter).child("Cover").addValueEventListener(new ValueEventListener() {
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
            dbRef.child(chapter).child("Chapter").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> values = new HashMap<String, Object>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        novellist.add(dataSnapshot.getKey());
                        ArrayAdapter adapter = new ArrayAdapter<String>(NovelHomeActivity.this, R.layout.dropdown, novellist);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change = new Intent(NovelHomeActivity.this, HomeFragment.class);
                startActivity(change);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}