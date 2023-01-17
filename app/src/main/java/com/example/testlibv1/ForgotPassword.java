package com.example.testlibv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {

    EditText Femail;
    Button Fpassword;
    TextView skip;

    String email;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Femail = findViewById(R.id.Femail);
        Fpassword = findViewById(R.id.Fpassword);
        skip = findViewById(R.id.skip);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Fpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = Femail.getText().toString();

                if(email.isEmpty())
                {
                    Femail.setError("Please provide an email.");
                }
                else
                {
                    forgetPassword();
                }
            }
        });
    }
    private void forgetPassword()
    {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPassword.this, "Check your email", Toast.LENGTH_SHORT).show();
                            Intent change = new Intent(ForgotPassword.this, LoginActivity.class);
                            startActivity(change);
                        }
                        else
                        {
                            Toast.makeText(ForgotPassword.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}