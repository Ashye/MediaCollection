package com.mediamemo.onlinelibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mediamemo.R;
import com.mediamemo.datacontroller.CollectionController;
import com.mediamemo.html.HtmlJsoupHelper;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link OnlineLibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnlineLibraryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnlineLibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnlineLibraryFragment newInstance(String param1, String param2) {
        OnlineLibraryFragment fragment = new OnlineLibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OnlineLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    private WebView webView;
    private SwipeRefreshLayout refreshLayout;

//    private CollectionController collectionController;
//
//    public void setCollectionController(CollectionController collectionController) {
//        this.collectionController = collectionController;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_online_library, container, false);
        webView = (WebView) v.findViewById(R.id.online_web_view);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_view);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });


        initWebView();

        return v;
    }

    private void initWebView() {
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
                    }
                }
                return false;
            }
        });


        webView.loadUrl("http://www.verycd.com/");
//        webView.loadUrl("http://m.verycd.com/");
    }

    public void goBackPage() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    public String getPageTitle() {
        return webView.getTitle();
    }

    public String getPageUrl() {
        return webView.getUrl();
    }






}
