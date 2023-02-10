package com.example.testlibv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    Handler handle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent change = new Intent(SplashActivity.this, NavigationActivity.class);
                    startActivity(change);
                    finish();
            }
        }, 3000);

    }
}