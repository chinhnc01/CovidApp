package com.chinhnc.covid_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class MapActivity extends AppCompatActivity {
    WebView webview_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        webview_map = (WebView)findViewById(R.id.webview_map);

        webview_map.loadUrl("https://bandodichte.bacgiang.gov.vn/bacgiang?locale=vn");
    }
}