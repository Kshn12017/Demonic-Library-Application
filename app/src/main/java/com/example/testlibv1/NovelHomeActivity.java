package com.example.testlibv1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testlibv1.ui.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NovelHomeActivity extends AppCompatActivity {

    FirebaseDatabase fdb;
    DatabaseReference dbRef;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    RelativeLayout relative_layout;
    LinearLayout linear2, linear3;

    List<String> commentlist = new ArrayList<>();
    List<String> novellink = new ArrayList<>();

    ShapeableImageView uimage;
    ImageView cover, submit;
    TextView noveltitle, genre, author, status, uname, ucomment;
    TextInputLayout comlay;
    TextInputEditText comment;
    Button chapters;
    ListView comment_list;

    String novelname, commentstr, uimagestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_home);

        LayoutInflater layout = getLayoutInflater();
        View view = layout.inflate(R.layout.novelpage, null);
        relative_layout = findViewById(R.id.relative_layout);
        relative_layout.addView(view);

        db = FirebaseFirestore.getInstance();
        fdb = FirebaseDatabase.getInstance();
        dbRef = fdb.getReference("Novels");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        ExpandableTextView expTv = view.findViewById(R.id.expand_text_view).findViewById(R.id.expand_text_view);

        chapters = view.findViewById(R.id.chapters);
        cover = view.findViewById(R.id.coverimage);
        submit = view.findViewById(R.id.submit);
        noveltitle = view.findViewById(R.id.noveltitle);
        genre = view.findViewById(R.id.genre);
        author = view.findViewById(R.id.author);
        status = view.findViewById(R.id.status);
        linear2 = view.findViewById(R.id.linear2);
        linear3 = view.findViewById(R.id.linear3);
        comlay = view.findViewById(R.id.comlay);
        comment = view.findViewById(R.id.comment);
        comment_list = view.findViewById(R.id.comment_list);

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
                expTv.setText(documentSnapshot.getString("Description"));
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

        chapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change = new Intent(NovelHomeActivity.this, ChapterListActivity.class);
                change.putExtra("NovelName", novelname);
                startActivity(change);
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd", Locale.CANADA);
        Date now = new Date();
        String filename = dateFormat.format(now);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = mUser.getDisplayName();

                fdb.getReference().child("User_Details").child(username).child("uuname").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.getValue(String.class);
                        commentstr = comment.getText().toString();

                        Map<String, Object> values = new HashMap<>();
                        values.put("Name", name);
                        values.put("Comment", commentstr);

                        String username = mUser.getDisplayName();

                        fdb.getReference().child("User_Details").child(username).child("profile").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String profile = snapshot.getValue(String.class);
                                uimagestr = profile;
                                values.put("Display", uimagestr);

                                List<Map<String, Object>> values2 = new ArrayList<>();
                                values2.add(values);

                                Map<String, Object> values3 = new HashMap<>();
                                values3.put(username, values);

                                db.collection("Comments").document(novelname).update(values3);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(NovelHomeActivity.this, "Task Cancelled.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        if (commentlist.isEmpty()) {
            db.collection("Comments").document(novelname).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    Map<String, Object> Comments = (Map<String, Object>) documentSnapshot.getData();

                    if(Comments != null) {
                        for (Map.Entry<String, Object> snap : Comments.entrySet()) {
                            Log.d(TAG, "Snap:" + snap);
                            Map<String, Object> snap2 = (Map<String, Object>) snap.getValue();
                            Log.d(TAG, "Snap2: " + snap2);
                            String namaestr = (String) snap2.get("Name");
                            String comstr = (String) snap2.get("Comment");
                            String dpstr = (String) snap2.get("Display");

                            layoutadd(namaestr, comstr, dpstr);
                        }
                    }
                }
            });
        }
    }

    private void layoutadd(String namaestr, String comstr, String dpstr) {
        LayoutInflater layout2 = getLayoutInflater();
        View view2 = layout2.inflate(R.layout.comment, null);
        linear3.addView(view2);

        uimage = view2.findViewById(R.id.uimage);
        uname = view2.findViewById(R.id.uname);
        ucomment = view2.findViewById(R.id.ucomment);

        uname.setText(namaestr);
        ucomment.setText(comstr);

        if (dpstr != null) {
            Glide.with(NovelHomeActivity.this).load(dpstr).into(uimage);
        } else {
            uimage.setImageResource(R.drawable.user_icon);
        }
        Log.d(TAG, "Ran: " + namaestr);
    }
}

            /*dbRef.child(novelname).child("Chapter").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> values = new HashMap<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        commentlist.add(dataSnapshot.getKey());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(NovelHomeActivity.this, R.layout.dropdown, commentlist);
                        comment_list.setAdapter(adapter);
                        novellink.add((String) dataSnapshot.getValue());
                        values.put(dataSnapshot.getKey(), dataSnapshot.getValue());

                        comment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String chapterNum = (String) comment_list.getItemAtPosition(i);
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
            });*/