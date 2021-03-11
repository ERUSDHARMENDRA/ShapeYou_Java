package com.app.shapeyou;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //Create variable
    private WebView webView;
    private String webUrl = "https://shop4shapeyou.com";
    ProgressBar progressBarWeb;
    ProgressDialog progressDialog;

    //declaring relative layout and button for no connection
    RelativeLayout relativeLayout;
    Button btnNoInternetConnection;
    //Endof relative layout and button for no connection


    //Error 403 disallowed useragent
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";
    //End of Error 403 disallowed useragent


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign variable
        webView = (WebView) findViewById(R.id.webView);
        //endof Assign Variable

        //progressbar
        progressBarWeb = (ProgressBar) findViewById(R.id.progressBar);
        //endof progressbar

        //intialize progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please Wait");
        //endof progressDialog

        //Initialising button for noInternetConnection
        btnNoInternetConnection = (Button) findViewById(R.id.btnNoconnection);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        //Error 403 resolve code
        webView.getSettings().setUserAgentString(USER_AGENT);
        //End of Error 403 resolve
        WebSettings settings = webView.getSettings();

        //Enable JS
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        //Error 403 resolve code
        webView.getSettings().setUserAgentString(USER_AGENT);
        //End of Error 403 resolve

        //Improve Loading
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSavePassword(true);
        settings.setSaveFormData(true);
        settings.setEnableSmoothTransition(true);
        settings.setLoadsImagesAutomatically(true);
        //Endof Improve Loading

        checkConnection();


        //Open backlinks in app only
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //unknown_url_scheme_err
                if (url.startsWith("tel:") || url.startsWith("whatsapp:") || url.startsWith("mailto:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Intent intent1 = intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        //new method for progressbar
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarWeb.setVisibility(View.VISIBLE);
                progressBarWeb.setProgress(newProgress);
                setTitle("Loading....");
                progressDialog.show();

                if (newProgress == 100) {
                    progressBarWeb.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                    progressDialog.dismiss();
                }

                super.onProgressChanged(view, newProgress);
            }
        });
        //endof new method for progressbar

        btnNoInternetConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();

            }
        });




    }

    //Onbackpressed
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            //Alert for exiting application
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Exit?")
                    .setNegativeButton("No",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    }).show();
            //End of Alert for exiting application
        }
    }




    //Condition for opening app on available network
    public void checkConnection(){

        //Enable Connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager)  this.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Endof Connectvity manager

        //Get active network info
          NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
          NetworkInfo network = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //Endof get active network info

        //Condition for opening app on available network
        if (wifi.isConnectedOrConnecting()){
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
        else if(network.isConnected()){
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }
        else{
            webView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(this, "You don't have any active internet connection", Toast.LENGTH_SHORT).show();
        }
    }
    //Endof Condition for opening app on available network
}



