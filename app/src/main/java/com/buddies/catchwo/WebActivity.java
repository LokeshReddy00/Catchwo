package com.buddies.catchwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {
    WebView simpleWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");


//       String url = "https://firebasestorage.googleapis.com/v0/b/police-1e8ff.appspot.com/o/User%20Details%2FAll%20PDF%2F1645118105822pdf?alt=media&token=2d2772ce-f3cb-4638-bb24-881c7c47452c";


        /*Add in Oncreate() funtion after setContentView()*/
// initiate a web view
        simpleWebView = (WebView) findViewById(R.id.simpleWebView);
        simpleWebView.loadUrl(url);

// set web view client
        simpleWebView.setWebViewClient(new MyWebViewClient());

// string url which you have to load into a web view
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url); // load the url on the web view
    }

    // custom web view client class who extends WebViewClient
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); // load the url
            return true;
        }
    }
}