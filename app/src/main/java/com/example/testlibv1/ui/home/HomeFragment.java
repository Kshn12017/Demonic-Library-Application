package com.example.testlibv1.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.testlibv1.NovelHomeActivity;
import com.example.testlibv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    LinearLayout linear3;

    FirebaseFirestore db;

    List<String> novelname = new ArrayList<>();
    List<String> coveruri = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        linear3 = view.findViewById(R.id.linear3);

        db.collection("Novels").whereEqualTo("Type", "WebNovel").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String value = document.getId();
                        novelname.add(value);
                        db.collection("Novels").document(value).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String value2 = documentSnapshot.getString("Cover");
                                    coveruri.add(value2);
                                    layoutadd(value, value2);

                                } else {
                                    Toast.makeText(getActivity(), "Couldn't get image.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to get name", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void layoutadd(String value, String value2) {
        LayoutInflater layout = getLayoutInflater();
        View novelView = layout.inflate(R.layout.activity_novel_view, null);
        linear3.addView(novelView);

        ShapeableImageView novelCover = novelView.findViewById(R.id.novelCover);
        TextView novelName = novelView.findViewById(R.id.novelName);

        novelName.setText(value);
        Glide.with(getActivity()).load(value2).into(novelCover);

        novelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chapter = (String) novelName.getText();
                Intent change = new Intent(getActivity(), NovelHomeActivity.class);
                change.putExtra("NovelName", chapter);
                startActivity(change);
            }
        });
    }
}