package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class ReportActivity extends AppCompatActivity {

    WebView brower;
    WebSettings settings;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        brower= findViewById(R.id.webkit);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        brower.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSdC3GBap3Vc0kRQpH61nFTXvRebg7yFURhAzl96SNdfAkgadw/viewform?usp=sf_link");
        brower.setWebViewClient(new WebViewClient());

        settings = brower.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(false);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
    }
}