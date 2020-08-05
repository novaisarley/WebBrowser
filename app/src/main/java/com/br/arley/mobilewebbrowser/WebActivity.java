package com.br.arley.mobilewebbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    WebView wv;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url", "https://www.google.com/");
        }

        wv = findViewById(R.id.activity_web_webview);
        wv.loadUrl("url");

        WebSettings webSettings = wv.getSettings();
        //Habilitando o JavaScript
        webSettings.setJavaScriptEnabled(true);
    }
}