package com.mediamemo;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mediamemo.localcollection.LocalCollectionFragment;
import com.mediamemo.onlinelibrary.OnlineLibraryFragment;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

public class MainFrameActivity extends AppCompatActivity implements LocalCollectionFragment.OnFragmentInteractionListener,
        OnlineLibraryFragment.OnFragmentInteractionListener {

    private TabLayout frameTabLayout;
    private ViewPager fragmentViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);

        initToolbar();
        setupTabs();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("MediaMemo");
        toolbar.setSubtitle("test");
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(android.R.drawable.ic_delete);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
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
                    return new OnlineLibraryFragment();

                default:
                    return new LocalCollectionFragment();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
