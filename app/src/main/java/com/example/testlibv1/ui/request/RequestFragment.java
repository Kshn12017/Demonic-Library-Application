package com.example.testlibv1.ui.request;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testlibv1.NavigationActivity;
import com.example.testlibv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestFragment extends Fragment {

    LinearLayout layout_yes;
    TextInputEditText novel, group, description,genre, author, link, position,discord;
    AutoCompleteTextView member;
    Button request;

    FirebaseFirestore db;
    FirebaseStorage fb;
    FirebaseDatabase fdb;

    String novelstr, groupstr, descriptionstr, genrestr, authorstr, linkstr, memberstr, positionstr, discordstr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        fdb = FirebaseDatabase.getInstance();
        fb = FirebaseStorage.getInstance();

        novel = view.findViewById(R.id.requested_Novel);
        group = view.findViewById(R.id.requested_Group);
        description = view.findViewById(R.id.requested_Description);
        genre = view.findViewById(R.id.requested_Genre);
        author = view.findViewById(R.id.requested_Author);
        link = view.findViewById(R.id.requested_Link);
        layout_yes = view.findViewById(R.id.layout_yes);
        position = view.findViewById(R.id.requested_Position);
        discord = view.findViewById(R.id.requested_Discord);
        member = view.findViewById(R.id.member);
        request = view.findViewById(R.id.request);

        member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String yesno = member.getText().toString();
                if(yesno.equals("Yes")) {
                    layout_yes.setVisibility(View.VISIBLE);
                }
                if(yesno.equals("No")){
                    layout_yes.setVisibility(View.GONE);
                }
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestnovel();

                db.collection("Requests")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });

    }

    private void requestnovel() {

        novelstr =novel.getText().toString();
        groupstr=group.getText().toString();
        descriptionstr=description.getText().toString();
        genrestr=genre.getText().toString();
        authorstr=author.getText().toString();
        linkstr=link.getText().toString();
        memberstr=member.getText().toString();
        positionstr=position.getText().toString();
        discordstr=discord.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String filename = dateFormat.format(now);

        if(novelstr.isEmpty()){
            novel.setError("Enter novel title.");
        } else if(groupstr.isEmpty()){
            group.setError("Enter translation group name");
        } else if(descriptionstr.isEmpty()){
            description.setError("Enter novel synopsis");
        } else if(genrestr.isEmpty()){
            genre.setError("Enter atleast 1 genre");
        } else if(authorstr.isEmpty()){
            author.setError("Enter author name");
        } else if(linkstr.isEmpty()){
            link.setError("Enter link for any of the chapter");
        } else if(memberstr.isEmpty()){
            member.setError("Please specify");
        } else{
            if(memberstr.equals("Yes")){
                if(positionstr.isEmpty()){
                    position.setError("Please specify");
                } else if(discordstr.isEmpty()){
                    discord.setError("Please specify");
                } else{
                    Map<String, Object> values = new HashMap<>();
                    values.put("Novel",novelstr);
                    values.put("Group",groupstr);
                    values.put("Description",descriptionstr);
                    values.put("Genre",genrestr);
                    values.put("Author", authorstr);
                    values.put("Link",linkstr);
                    values.put("Position",positionstr);
                    values.put("Discord ID", discordstr);
                    db.collection("Requests").document(filename).set(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Request Submitted", Toast.LENGTH_SHORT).show();
                            Intent change = new Intent(getActivity(), NavigationActivity.class);
                            startActivity(change);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Adding failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            if(memberstr.equals("No")){

                Map<String, Object> values = new HashMap<>();
                values.put("Novel",novelstr);
                values.put("Group",groupstr);
                values.put("Description",descriptionstr);
                values.put("Genre",genrestr);
                values.put("Author", authorstr);
                values.put("Link",linkstr);
                db.collection("Requests").document(filename).set(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Request Submitted", Toast.LENGTH_SHORT).show();
                        Intent change = new Intent(getActivity(), NavigationActivity.class);
                        startActivity(change);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Adding failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.yes_no, R.layout.dropdown);
        member.setAdapter(adapter);
    }
}