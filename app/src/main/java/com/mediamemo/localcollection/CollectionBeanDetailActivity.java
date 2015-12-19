package com.mediamemo.localcollection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
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
    private boolean isCollected = true;
    private boolean isUpdated = false;
    private boolean needUpdate = false;
    private int index = -1;


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
                index = bundle.getInt("idx");
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
                onFinished();
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

        webView.addJavascriptInterface(new InJSLoadingHtml(), "local_obj");
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
                setCollectionIcon();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshLayout.setRefreshing(false);
                if (!isUpdated && url.equals(bean.getUrl())) {
                    view.loadUrl("javascript:window.local_obj.showHtml(document.getElementsByTagName('html')[0].innerHTML);");
//                    isUpdated = true;
                }
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
                    } else {
                        onFinished();
                    }
                }
                return false;
            }
        });

        webView.loadUrl(bean.getUrl());
    }

    private MenuItem shouCang;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        shouCang = menu.findItem(R.id.action_shoucang);
        setCollectionIcon();
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
        if (isCollected) {
            isCollected = false;
            shouCang.setIcon(R.drawable.shou_cang_no);
            SnackBarMessage("删除收藏成功");
        }else {
            isCollected = true;
            shouCang.setIcon(R.drawable.shou_cang_yes);
            SnackBarMessage("收藏成功");
        }
    }

    private void setCollectionIcon() {
        if (isCollected) {
            if (shouCang != null) {
                shouCang.setIcon(R.drawable.shou_cang_yes);
            }
        } else {
            if (shouCang != null) {
                shouCang.setIcon(R.drawable.shou_cang_no);
            }
        }
    }

    final class InJSLoadingHtml {
        @JavascriptInterface
        public void showHtml(String html) {
            checkLatest(getCollectionBeanFromHtml(html));
        }
    }

    private CollectionBean getCollectionBeanFromHtml(String html) {
        HtmlJsoupHelper jsoupHelper = new HtmlJsoupHelper();
        jsoupHelper.parseHtmlFromString(html);
        CollectionBean bean = new CollectionBean();
        bean.setIconUrl(jsoupHelper.getIconUrl());
        bean.setUrl(this.bean.getUrl());
        bean.setTitle(jsoupHelper.getTitle());
        bean.setLatest(jsoupHelper.getLatest());
        return bean;
    }

    private void checkLatest(CollectionBean bean) {
        if (!TextUtils.isEmpty(this.bean.getLatest())) {
            if (bean != null) {
                if (!this.bean.getLatest().equals(bean.getLatest())) {
                    this.bean = bean;
                    needUpdate = true;
                }
                isUpdated = true;
            }
        }else {
            isUpdated = true;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnHistory.getId()) {
            goBackPage();
        }
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

    public static enum  DetailBack {
        NORMAL,
        DELETE,
        UPDATED
    }
    private void onFinished() {
        Intent update = new Intent();

        if (isCollected) {
            if (needUpdate) {
                update.putExtra("bean", JSON.toJSONString(bean));
                update.putExtra("idx", index);
                setResult(DetailBack.UPDATED.ordinal(), update);
            }else {
                setResult(DetailBack.NORMAL.ordinal());
            }
        }else {
            update.putExtra("idx", index);
            setResult(DetailBack.DELETE.ordinal(), update);
        }

        finish();
    }
}
