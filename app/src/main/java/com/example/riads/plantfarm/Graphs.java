package com.example.riads.plantfarm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Graphs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        WebView simpleWebView = (WebView) findViewById(R.id.webview);
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        // specify the url of the web page in loadUrl function
        simpleWebView.loadUrl("https://test-e6695.firebaseapp.com/");
    }
}
