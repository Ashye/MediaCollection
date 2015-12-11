package com.mediamemo;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mediamemo.localcollection.CollectionBeanDetailActivity;
import com.mediamemo.onlinelibrary.CollectionController;
import com.mediamemo.localcollection.CollectionBean;
import com.mediamemo.localcollection.LocalCollectionFragment;
import com.mediamemo.onlinelibrary.OnlineLibraryFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainFrameActivity extends AppCompatActivity implements LocalCollectionFragment.OnFragmentInteractionListener,
        OnlineLibraryFragment.OnFragmentInteractionListener,
        LocalCollectionFragment.OnLocalCollectionActionListener {

    private TabLayout frameTabLayout;
    private ViewPager fragmentViewPager;

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
        toolbar.setTitle("MediaMemo");
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
        fragmentViewPager = (ViewPager) findViewById(R.id.fragment_view_pager);

        fragmentVPAdapter = new FragmentVPAdapter(getSupportFragmentManager());
        fragmentViewPager.setAdapter(fragmentVPAdapter);

        frameTabLayout.setupWithViewPager(fragmentViewPager);
        frameTabLayout.setTabsFromPagerAdapter(fragmentVPAdapter);
        frameTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                fragmentViewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
                    fragment.setActionListener(MainFrameActivity.this);
                    return fragment;
            }
        }
    }

    private void initData() {
        collectionDataController = new CollectionController(this);
//        collectionDataController.setDataChangedListener();
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
                actionShoucang();
                return true;

            default:
                Log.e("ssss", "onOptionsItemSelected default..............");
                return super.onOptionsItemSelected(item);
        }
    }


    private static final String[] keys = new String[]{"entries", "topics"};
    private void actionShoucang() {
        int tabId = frameTabLayout.getSelectedTabPosition();
        if (tabId == 1) {
            OnlineLibraryFragment tab = (OnlineLibraryFragment) fragmentVPAdapter.getFragment(tabId);
            String url = tab.getPageUrl();
            String title = tab.getPageTitle();

            if (url.contains(keys[0])) {
                CollectionBean bean = new CollectionBean(title, url, null);
                if (collectionDataController.queryItem(bean)) {
                    SnackBarMessage("重复收藏");
                }else {
                    if (collectionDataController.addItem(bean)) {
                        SnackBarMessage("收藏成功");
                    }
                }
            }else {
                SnackBarMessage("该资源不支持收藏");
            }
        }
    }

    @Override
    public void onActionDelete(int position, final CollectionBean bean) {
        Snackbar.make(frameTabLayout, "删除 "+bean.getTitle(), Snackbar.LENGTH_LONG)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        collectionDataController.deleteItem(bean);
                        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    @Override
    public void onActionDetail(CollectionBean bean) {
//        SnackBarMessage("打开详情页面");
        Intent intent = new Intent(this, CollectionBeanDetailActivity.class);
        intent.putExtra("bean", JSON.toJSONString(bean));
        startActivity(intent);
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
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
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
