package com.example.testlibv1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    ListView request_list;

    FirebaseFirestore db;
    FirebaseStorage fb;
    FirebaseDatabase fdb;

    List<String> requestlist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        request_list = view.findViewById(R.id.request_list);

        db = FirebaseFirestore.getInstance();
        fdb = FirebaseDatabase.getInstance();
        fb = FirebaseStorage.getInstance();

        db.collection("Requests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        String request = documentSnapshot.getId();

                        requestlist.add(request);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown, requestlist);
                        request_list.setAdapter(adapter);

                        request_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String requestid = (String) request_list.getItemAtPosition(i);
                                Intent change = new Intent(getActivity(), RequestViewActivity.class);
                                change.putExtra("Filename", requestid);
                                startActivity(change);
                            }
                        });
                    }
                }
            }
        });
    }
}