package com.mediamemo;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.ashye.rest.BaseApi;
import com.ashye.rest.demo.GitService;
import com.ashye.rest.demo.SearchService;
import com.ashye.restSecond.ApiService;
import com.ashye.restSecond.ServiceProvider;
import com.mediamemo.html.HtmlJsoupHelper;
import com.mediamemo.datacontroller.CollectionController;
import com.mediamemo.localcollection.CollectionBean;
import com.mediamemo.localcollection.LocalCollectionFragment;
import com.mediamemo.onlinelibrary.OnlineLibraryFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainFrameActivity extends AppCompatActivity implements LocalCollectionFragment.OnFragmentInteractionListener,
        OnlineLibraryFragment.OnFragmentInteractionListener {

    private TabLayout frameTabLayout;
    private FrameTabViewPager fragmentViewPager;

    private CollectionController collectionDataController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        initToolbar();
        initData();
        setupTabs();




    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Demo");
        setSupportActionBar(toolbar);
    }

    private void setupTabs() {
        initTabs();
        initViewPager();
    }

    private final String[] tabName = new String[]{"收藏", "影视库"};
    private final int tabsSize = tabName.length;
    private void initTabs() {
        frameTabLayout = (TabLayout) findViewById(R.id.frame_tab_layout);
        TabLayout.Tab tab = null;

        for (int i=0; i<tabsSize; i++) {
            tab = frameTabLayout.newTab();
            tab.setText(tabName[i]);
            frameTabLayout.addTab(tab, i==0);
        }
    }

    private FragmentVPAdapter fragmentVPAdapter;
    private void initViewPager() {
        fragmentViewPager = (FrameTabViewPager) findViewById(R.id.fragment_view_pager);
        fragmentViewPager.setScrollable(false);

        fragmentVPAdapter = new FragmentVPAdapter(getSupportFragmentManager());
        fragmentViewPager.setAdapter(fragmentVPAdapter);

        frameTabLayout.setupWithViewPager(fragmentViewPager);
        frameTabLayout.setTabsFromPagerAdapter(fragmentVPAdapter);
    }

    private class FragmentVPAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public FragmentVPAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>(2);
            init();
        }

        private void init() {
            fragments.add(0, createFragment(0));
            fragments.add(1, createFragment(1));
        }

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public int getCount() {
            return tabsSize;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabName[position];
        }

        private Fragment getFragment(int id) {
            Fragment fragment = null;
            try {
                fragment = fragments.get(id);
            } catch (IndexOutOfBoundsException e) {
                fragment = createFragment(id);
            }

            return fragment;
        }

        private Fragment createFragment(int id) {
            switch (id) {
                case 1:
                    return OnlineLibraryFragment.newInstance(null, null);

                default:
                    LocalCollectionFragment fragment = LocalCollectionFragment.newInstance(null, null);
                    fragment.setDataController(collectionDataController);
                    return fragment;
            }
        }
    }

    private void initData() {
        collectionDataController = CollectionController.newInstance(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shoucang:


                // TODO: 2015/12/16 test
//                SearchService searchService = new SearchService();
//                searchService.search("demo", new SearchService.ResultListener<String>() {
//                    @Override
//                    public void onSuccess(String string) {
//                        Log.e("ssss", "onSuccess:"+string);
//                    }
//
//                    @Override
//                    public void onFailure(String error) {
//                        Log.e("ssss", "onFailure:"+error);
//                    }
//                });


//                GitService gitService = new GitService();
//                Map<String, Object> map = new HashMap<>();
//                map.put("aa", "1111111111");
//                map.put("b", 123);
//                gitService.post(new BaseApi.ResultListener<JSONObject>() {
//                    @Override
//                    public void onSuccess(JSONObject data) {
//                        Log.d("sssss", ""+data.toJSONString());
////                        Log.d("sssss", ""+data.getCurrent_user_url());
//                    }
//
//                    @Override
//                    public void onFailure(String error) {
//                        Log.d("sssss", "onFailure: "+error);
//                    }
//                }, map);


//                gitService.listApis(new BaseApi.ResultListener<JSONObject>() {
//                    @Override
//                    public void onSuccess(JSONObject data) {
//                        Log.e("sss", "onSuccess:"+data.toJSONString());
//                    }
//
//                    @Override
//                    public void onFailure(String error) {
//                        Log.e("sss", "onFailure error:"+error);
//                    }
//                });

                ServiceProvider.BaiduSearch search = new ApiService().getService(ServiceProvider.BaiduSearch.class);
                search.search("demo", "http://www.baidu.com");







//                checkShouCang();
                return true;

            default:
                Log.e("ssss", "onOptionsItemSelected default..............");
                return super.onOptionsItemSelected(item);
        }
    }


    private static final String[] keys = new String[]{"entries", "topics"};
    private void checkShouCang() {
        int tabId = frameTabLayout.getSelectedTabPosition();
        if (tabId == 1) {
            OnlineLibraryFragment tab = (OnlineLibraryFragment) fragmentVPAdapter.getFragment(tabId);
            String url = tab.getPageUrl();

            if (url.contains(keys[0])) {
                if (collectionDataController.queryItem(url)) {
                    SnackBarMessage("重复收藏");
                }else {
                    actionAddShouCang(url);
                }
            }else {
                SnackBarMessage("该资源不支持收藏");
            }
        }else {
            SnackBarMessage("本页面不支持该操作");
        }
    }

    private HtmlJsoupHelper jsoupHelper;
    private void actionAddShouCang(final String url) {
        if (jsoupHelper == null) {
            jsoupHelper = new HtmlJsoupHelper();
        }
        Toast.makeText(getApplicationContext(), "解析数据中...", Toast.LENGTH_LONG).show();
         new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        jsoupHelper.parseHtmlFromUrl(url, new HtmlJsoupHelper.OnHtmlPageLoadListener() {
                            @Override
                            public void onHtmlPageLoadedFinished(HtmlJsoupHelper jsoupHelper) {
                                String title = jsoupHelper.getTitle();
                                String iconUrl = jsoupHelper.getIconUrl();
                                String latest = jsoupHelper.getLatest();
//                                Log.e("title", "page title:" + title);
//                                Log.e("icon", "page iconUrl:" + iconUrl);
//                                Log.e("latest", "page latest:" + latest);
                                if (collectionDataController.addItem(new CollectionBean(title, url, iconUrl, latest))) {
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


    private void SnackBarMessage(String message) {
        Snackbar.make(frameTabLayout, "".concat(message), Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            back2TwiceExitApp();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean isSecondExit = false;
    private void back2TwiceExitApp() {
        if (isSecondExit) {
            finish();
            System.exit(0);
        }else {
            isSecondExit = true;
            SnackBarMessage("再按一次退出");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isSecondExit = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
