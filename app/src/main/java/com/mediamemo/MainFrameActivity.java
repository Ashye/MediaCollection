package com.mediamemo;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import com.mediamemo.localcollection.LocalCollectionFragment;
import com.mediamemo.onlinelibrary.OnlineLibraryFragment;

import java.util.ArrayList;
import java.util.List;

public class MainFrameActivity extends AppCompatActivity implements LocalCollectionFragment.OnFragmentInteractionListener,
        OnlineLibraryFragment.OnFragmentInteractionListener,
        FloatingActionButton.OnClickListener {

    private TabLayout frameTabLayout;
    private ViewPager fragmentViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        initToolbar();
        setupTabs();
//        initBottom();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("MediaMemo");
//        toolbar.setSubtitle("test");
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(android.R.drawable.ic_delete);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
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

                if (tab.getPosition() == 1) {
                    if (btnHistory == null) {
                        initButton();
                        showButtons();
                    }else {
                        showButtons();
                    }

                }else {
                    if (btnHistory != null) {
                        hideButton();
                    }
                }

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
                    return LocalCollectionFragment.newInstance(null, null);
            }
        }
    }


    private FloatingActionButton btnHistory;
    private FloatingActionButton btnShouCang;
    private void initButton() {
        btnHistory = (FloatingActionButton) findViewById(R.id.web_view_history_btn);
        btnHistory.setOnClickListener(this);
//        btnHistory.setEnabled(false);
        btnShouCang = (FloatingActionButton) findViewById(R.id.web_view_shou_cang_btn);
        btnShouCang.setOnClickListener(this);
    }

    private void showButtons() {
        btnHistory.setVisibility(View.VISIBLE);
        btnShouCang.setVisibility(View.VISIBLE);
    }

    private void hideButton() {
        btnShouCang.setVisibility(View.GONE);
        btnHistory.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int tabId = frameTabLayout.getSelectedTabPosition();

        if (1 == tabId) {
            OnlineLibraryFragment f = (OnlineLibraryFragment) fragmentVPAdapter.getFragment(tabId);
            switch (view.getId()) {
                case R.id.web_view_history_btn:
                    f.goBackPage();
                    return;
                case R.id.web_view_shou_cang_btn:
                    Snackbar.make(fragmentViewPager, "收藏成功, " + f.getPageTitle() + "\n" + f.getPageUrl(), Snackbar.LENGTH_SHORT).show();
                    return;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
