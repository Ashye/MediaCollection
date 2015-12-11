package com.mediamemo.localcollection;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
    private SwipeRefreshLayout refreshLayout;
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

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.detail_refresh);
        refreshLayout.setColorSchemeColors(Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE, Color.BLACK);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

        webView = (WebView) findViewById(R.id.detail_web_view);
        WebSettings settings = webView.getSettings();
        settings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
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

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                refreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshLayout.setRefreshing(false);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shoucang:
//                actionViewPointMode(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onClick(View view) {
        goBackPage();
    }

    public void goBackPage() {
        if (webView.canGoBack()) {
            webView.goBack();
        }else {
            Snackbar.make(webView, "已经到顶了", Snackbar.LENGTH_SHORT).show();
        }
    }
}
