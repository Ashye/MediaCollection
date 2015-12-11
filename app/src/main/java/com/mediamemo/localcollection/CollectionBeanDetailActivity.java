package com.mediamemo.localcollection;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mediamemo.R;


public class CollectionBeanDetailActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener {

    private FloatingActionButton btnHistory;
    private WebView webView;
    private CollectionBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_bean_detail);

        getBundleData();

        initToolbar();

        initView();
    }

    private void getBundleData() {

        try {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                String data = bundle.getString("bean", null);
                bean = JSON.parseObject(data, CollectionBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "内部数据出错，请重试", Toast.LENGTH_LONG).show();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        /**
         * 这里要放在 setSupportActionBar 前面才有效
         */
        toolbar.setTitle(bean.getTitle());

        setSupportActionBar(toolbar);

        /**
         * 这两句要放在 setSupportActionBar 后面才有效
         */
        toolbar.setNavigationIcon(android.R.drawable.ic_input_delete);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {

        btnHistory = (FloatingActionButton) findViewById(R.id.btn_history);
        btnHistory.setOnClickListener(this);


        webView = (WebView) findViewById(R.id.detail_web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);

        /**
         * support auto scale web pages to fit the mobile screen size
         */
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    if (i == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.stopLoading();
                        goBackPage();
                        return true;
                    }else {
                        finish();
                    }
                }
                return false;
            }
        });

        if (bean != null) {
            webView.loadUrl(bean.getUrl());
        }
    }


    @Override
    public void onClick(View view) {
        goBackPage();
    }

    public void goBackPage() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }
}
