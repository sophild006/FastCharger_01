package com.ultra.fast.charger.battery.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ultra.fast.charger.battery.R;
import com.ultra.fast.charger.battery.base.BaseActivity;
import com.ultra.fast.charger.battery.util.L;
import com.ultra.fast.charger.battery.util.Utils;
import com.ultra.fast.charger.battery.view.LoadingView;

public class TermsActivity extends BaseActivity {

    private WebView web;
    private RelativeLayout llnoConnect;
    private static final String url = "http://fastcharger.adsformob.com/privacy.html";
    private LoadingView tremLoading;
    private Button btnInternet;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trems);
        initview();
    }

    private void initview() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setTitleTextColor(-1);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.termsandprivacy));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tremLoading = (LoadingView) findViewById(R.id.trem_loading);
        llnoConnect = (RelativeLayout) findViewById(R.id.rl_trems);
        btnInternet = (Button) findViewById(R.id.btn_trem_internet);
        web = (WebView) findViewById(R.id.trems_web);
        boolean networkConnected = Utils.isNetConnect();
        if (networkConnected) {
            llnoConnect.setVisibility(View.GONE);
            setwebView();

        } else {
            web.setVisibility(View.GONE);
            llnoConnect.setVisibility(View.VISIBLE);
        }
    }

    private void setwebView() {
        tremLoading.setVisibility(View.VISIBLE);
        web.setVisibility(View.GONE);

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
//                intent.putExtra("url", url);
//                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                tremLoading.setVisibility(View.GONE);
                web.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                llnoConnect.setVisibility(View.VISIBLE);
                web.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
