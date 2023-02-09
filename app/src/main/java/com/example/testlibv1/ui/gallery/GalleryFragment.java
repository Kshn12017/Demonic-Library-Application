package com.example.testlibv1.ui.gallery;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testlibv1.NavigationActivity;
import com.example.testlibv1.R;
import com.example.testlibv1.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GalleryFragment extends Fragment {

    AutoCompleteTextView autoCompleteTextView;
    LinearLayout linear2;

    FirebaseFirestore db;

    String novelstr;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.fragment_gallery, container, false));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        linear2 = view.findViewById(R.id.linear2);

        db = FirebaseFirestore.getInstance();

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                linear2.removeAllViews();
                novelstr = autoCompleteTextView.getText().toString();
                db.collection("Novels").document(novelstr).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList values = (ArrayList) document.get("Gallery");
                                Log.d(TAG, "values: " + values);
                                for (Object images : values) {
                                    String imgstr = String.valueOf(images);
                                    Log.d(TAG, "imgstr: " + imgstr);
                                    imagecall(imgstr);
                                }
                            }
                        }
                    }
                });
            }
        });

    }

    private void imagecall(String gallerystr) {
        LayoutInflater layout = getLayoutInflater();
        View novelView = layout.inflate(R.layout.activity_novel_view, null);
        linear2.addView(novelView);

        ShapeableImageView novelCover = novelView.findViewById(R.id.novelCover);
        TextView novelName = novelView.findViewById(R.id.novelName);

        novelName.setVisibility(View.GONE);
        Glide.with(GalleryFragment.this).load(gallerystr).into(novelCover);
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.novels, R.layout.dropdown);
        autoCompleteTextView.setAdapter(adapter);
    }
}