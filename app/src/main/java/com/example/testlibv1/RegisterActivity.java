package com.example.testlibv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText name, username, email, password, c_password;
    Button register, cancel;
    FirebaseAuth mAuth;
    DatabaseReference db;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        register = findViewById(R.id.register);
        cancel = findViewById(R.id.cancel);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("User_Details");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(change);
            }
        });
    }

    private void PerformAuth() {

        String uemail = email.getText().toString();
        String upass = password.getText().toString();
        String ucpass = c_password.getText().toString();

        if (!uemail.matches(emailPattern)) {
            email.setError("Enter correct email.");
        } else if (upass.isEmpty() || upass.length() < 6) {
            password.setError("Enter proper password.");
        } else if (!upass.equals(ucpass)) {
            c_password.setError("Password did not match");
        } else {
            progressDialog.setMessage("Please Wait...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(uemail, upass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        updateUser();
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Exception: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void updateUser() {

        String uname = name.getText().toString();
        String uuname = username.getText().toString();
        String uemail = email.getText().toString();

        db.child(uname).child("uname").setValue(uname);
        db.child(uname).child("uuname").setValue(uuname);
        db.child(uname).child("uemail").setValue(uemail);

        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(uname).build();
        mAuth.getCurrentUser().updateProfile(changeRequest);
        mAuth.signOut();
        openLogin();
    }

    private void openLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}