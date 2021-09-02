package com.chinhnc.covid_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MapActivity extends AppCompatActivity {
    WebView webview_map;
    Button button_map,button_antoan,button_nguonlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        webview_map = (WebView)findViewById(R.id.webview_map);


        //setContentView(webview_map);


        WebSettings webSettings = webview_map.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // webview_map.setWebChromeClient(new GeoWebChromeClient());

        // https://maps.vnpost.vn/bando-chongdich/
        webview_map.loadUrl("https://maps.vnpost.vn/corona/#/app/");

        buttonclick();
    }


    /**
     * WebChromeClient subclass handles UI-related calls
     * Note: think chrome as in decoration, not the Chrome browser
     */
//    public class GeoWebChromeClient extends WebChromeClient {
//        @Override
//        public void onGeolocationPermissionsShowPrompt(String origin,
//                                                       GeolocationPermissions.Callback callback) {
//            // Always grant permission since the app itself requires location
//            // permission and the user has therefore already granted it
//            callback.invoke(origin, true, false);
//        }
//    }

    private void buttonclick(){
        button_map = findViewById(R.id.button_map);
        button_antoan = findViewById(R.id.button_antoan);
        button_nguonlay = findViewById(R.id.button_nguonlay);
        button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview_map.loadUrl("https://maps.vnpost.vn/corona/#/app/");
            }
        });
        button_antoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview_map.loadUrl("https://maps.vnpost.vn/bando-chongdich/");
            }
        });
        button_nguonlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview_map.loadUrl("https://nguyco.antoancovid.vn/analysis1");
                webview_map.getSettings().setBuiltInZoomControls(true);
                webview_map.getSettings().setSupportZoom(true);
            }
        });
    }
}