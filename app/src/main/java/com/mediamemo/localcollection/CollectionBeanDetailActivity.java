package com.mediamemo.localcollection;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.mediamemo.datacontroller.CollectionController;
import com.mediamemo.html.HtmlJsoupHelper;

import java.io.IOException;


public class CollectionBeanDetailActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener {

    private FloatingActionButton btnHistory;
    private WebView webView;
    private SwipeRefreshLayout refreshLayout;
    private CollectionBean bean;

    private CollectionController collectionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_bean_detail);

        collectionController = CollectionController.newInstance(this);

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
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
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
                if (collectionController.queryItem(url)) {
                    if (shouCang != null) {
                        shouCang.setIcon(R.drawable.shou_cang_yes);
                    }
                }else {
                    if (shouCang != null) {
                        shouCang.setIcon(R.drawable.shou_cang_no);
                    }
                }
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

    private MenuItem shouCang;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        shouCang = menu.findItem(R.id.action_shoucang);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shoucang:
                checkShouCang();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkShouCang() {
        if (collectionController.queryItem(bean.getUrl())) {
            collectionController.deleteItem(bean.getUrl());
            shouCang.setIcon(R.drawable.shou_cang_no);
            SnackBarMessage("删除收藏成功");

        }else {
            actionAddShouCang(bean.getUrl());
            shouCang.setIcon(R.drawable.shou_cang_yes);
        }
    }

    private HtmlJsoupHelper jsoupHelper;
    private void actionAddShouCang(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
            try {
                jsoupHelper = new HtmlJsoupHelper();
                jsoupHelper.parseHtmlFromUrl(url, new HtmlJsoupHelper.OnHtmlPageLoadListener() {
                    @Override
                    public void onHtmlPageLoadedFinished(HtmlJsoupHelper jsoupHelper) {
                        String title = jsoupHelper.getTitle();
                        String iconUrl = jsoupHelper.getIconUrl();
                        String latest = jsoupHelper.getLatest();
//                            Log.e("title", "page title:" + title);
//                            Log.e("icon", "page iconUrl:" + iconUrl);
//                            Log.e("latest", "page latest:" + latest);
                        if (collectionController.addItem(new CollectionBean(title, bean.getUrl(), iconUrl, latest))) {
//                            shouCang.setIcon(R.drawable.shou_cang_yes);
                            SnackBarMessage("收藏成功");
                        }

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        goBackPage();
    }

    public void goBackPage() {
        if (webView.canGoBack()) {
            webView.goBack();
        }else {
            SnackBarMessage("已经到顶了");
        }
    }

    private void SnackBarMessage(String message) {
        Snackbar.make(webView, "".concat(message), Snackbar.LENGTH_SHORT).show();
    }
}
