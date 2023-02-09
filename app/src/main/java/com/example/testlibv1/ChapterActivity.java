package com.example.testlibv1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ChapterActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        webView = findViewById(R.id.webView);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String chapter = extras.getString("ChapLink");
            webView.loadUrl(chapter);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
        }
    }
}