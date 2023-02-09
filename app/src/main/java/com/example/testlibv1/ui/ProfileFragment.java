package com.example.testlibv1.ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.testlibv1.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileFragment extends Fragment {

    TextView t2, t4, t6;
    ImageView upic;

    FirebaseDatabase db;
    FirebaseStorage fb;
    StorageReference storageRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ActivityResultLauncher<String> picSelect;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        t2 = view.findViewById(R.id.textview2);
        t4 = view.findViewById(R.id.textview4);
        t6 = view.findViewById(R.id.textview6);
        upic = view.findViewById(R.id.uimage);

        db = FirebaseDatabase.getInstance();
        fb = FirebaseStorage.getInstance();
        storageRef = fb.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String name = mUser.getDisplayName();

        db.getReference().child("User_Details").child(name).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getActivity() == null) {
                    return;
                }
                String profile = snapshot.getValue(String.class);
                if (profile != null) {
                    Glide.with(ProfileFragment.this).load(profile).into(upic);
                } else {
                    upic.setImageResource(R.drawable.user_icon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Task Cancelled.", Toast.LENGTH_SHORT).show();
            }
        });

        db.getReference().child("User_Details").child(name).child("uuname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                t2.setText(name);
                t4.setText(mUser.getDisplayName());
                t6.setText(mUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Couldn't set info.", Toast.LENGTH_SHORT).show();
            }
        });

        picSelect = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                storageRef.child("image/profile_" + mUser.getDisplayName()).putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.child("image/profile_" + mUser.getDisplayName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                upic.setImageURI(null);
                                String uristr = uri.toString();
                                Log.d(TAG, "uristr: " + uristr);
                                db.getReference().child("User_Details").child(name).child("profile").setValue(uristr)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Intent change = new Intent(getActivity(), ProfileFragment.class);
                                                startActivity(change);
                                                Toast.makeText(getActivity(), "Task Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnCanceledListener(new OnCanceledListener() {
                                            @Override
                                            public void onCanceled() {
                                                Intent change = new Intent(getActivity(), ProfileFragment.class);
                                                startActivity(change);
                                                Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Intent change = new Intent(getActivity(), ProfileFragment.class);
                                startActivity(change);
                                Toast.makeText(getActivity(), "Task Failed", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                Intent change = new Intent(getActivity(), ProfileFragment.class);
                                startActivity(change);
                                Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent change = new Intent(getActivity(), ProfileFragment.class);
                        startActivity(change);
                        Toast.makeText(getActivity(), "Task Failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Intent change = new Intent(getActivity(), ProfileFragment.class);
                        startActivity(change);
                        Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        upic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Recommended image ratio - 1:1", Toast.LENGTH_SHORT).show();
                picSelect.launch("image/*");
            }
        });
    }
}