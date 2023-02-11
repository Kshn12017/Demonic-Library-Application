package com.example.testlibv1.ui.gallery;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.testlibv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    AutoCompleteTextView autoCompleteTextView;
    LinearLayout linear2;
    TextView clicktext, imagelink;

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
        clicktext = view.findViewById(R.id.clicktext);
        imagelink = view.findViewById(R.id.imagelink);

        db = FirebaseFirestore.getInstance();

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                linear2.removeAllViews();
                clicktext.setVisibility(View.VISIBLE);
                novelstr = autoCompleteTextView.getText().toString();
                db.collection("Novels").document(novelstr).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList values = (ArrayList) document.get("Gallery");
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

        novelCover.setClickable(true);
        novelCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagelink.setText(gallerystr);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.novels, R.layout.dropdown);
        autoCompleteTextView.setAdapter(adapter);
    }
}