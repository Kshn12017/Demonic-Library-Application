package com.example.testlibv1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NovelViewActivity extends AppCompatActivity {

    ImageView novelCover;
    TextView novelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_view);

        novelCover = findViewById(R.id.novelCover);
        novelName = findViewById(R.id.novelName);

    }
}