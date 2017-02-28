package com.chengshicheng.project;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合

    ArrayList<Fragment> fragmentsList = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String NAVIG_ITEM = "choosenNavigationItem";
    private Toolbar mToolbar;
    private MenuItem searchItem;
    private TabFragmentPagerAdapter fragmentAdapter;


    private int requestCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
//        refreshToolBar(null);

        refreshTabLayout();
    }


    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(getNavigationItem()).setChecked(true);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        fragmentAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList);

        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(fragmentsList.size());//可避免切换时重复执行onCreatView()
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    private int getNavigationItem() {
        return (int) SPUtil.get(NAVIG_ITEM, 0);
    }

    private void refreshTabLayout() {
        Bundle bundle = new Bundle();
        mTitleList.clear();
        fragmentsList.clear();
        int item = getNavigationItem();
        switch (item) {
            case 0:
                mTitleList.add("全部");
                Fragment fragment1 = KDFragment1.newInstance(
                        MainActivity.this, bundle);
                fragmentsList.add(fragment1);
                mTitleList.add("已签收");
                Fragment fragment2 = KDFragment2.newInstance(
                        MainActivity.this, bundle);
                fragmentsList.add(fragment2);
                mTitleList.add("未签收");
                Fragment fragment3 = KDFragment3.newInstance(
                        MainActivity.this, bundle);
                fragmentsList.add(fragment3);
                break;
            case 1:
                mTitleList.add("galllery");
                Fragment fragmentg = GalleryDemoFragment.newInstance(
                        MainActivity.this, bundle);
                fragmentsList.add(fragmentg);
                break;
            case 2:
                mTitleList.add("Slideshow");
                Fragment fragmentg2 = GalleryDemoFragment.newInstance(
                        MainActivity.this, bundle);
                fragmentsList.add(fragmentg2);
                break;
            case 3:
                break;
            default:
                break;
        }
        fragmentAdapter.notifyDataSetChanged();

        //将ViewPager和TabLayout绑定
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

//
//    private void refreshToolBar(Menu menu) {
//        int item = getNavigationItem();
//        mToolbar.getMenu().clear();
//        invalidateOptionsMenu();
//        switch (item) {
//            case 0:
//                getSupportActionBar().setTitle("快递鸟");
//                mToolbar.inflateMenu(R.menu.kd_fragment);//加载
//                break;
//            case 1:
//                getSupportActionBar().setTitle("Gally");
//                mToolbar.inflateMenu(R.menu.main);//加载
//                break;
//            case 2:
//                getSupportActionBar().setTitle("Slideshow");
//                mToolbar.inflateMenu(R.menu.main);//加载
//                break;
//            case 3:
//                getSupportActionBar().setTitle("Tool");
//                mToolbar.inflateMenu(R.menu.main);//加载
//                break;
//            default:
//                break;
//        }
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            SPUtil.put(NAVIG_ITEM, 0);
        } else if (id == R.id.nav_gallery) {
            SPUtil.put(NAVIG_ITEM, 1);
        } else if (id == R.id.nav_slideshow) {
            SPUtil.put(NAVIG_ITEM, 2);
        } else if (id == R.id.nav_manage) {
            SPUtil.put(NAVIG_ITEM, 3);
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
//        refreshToolBar(null);
        refreshTabLayout();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * FragmentStatePagerAdapter替换FragmentPagerAdapter
     * 并重写getItemPosition()
     */
    private class TabFragmentPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> mFragmentsList;

        private TabFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList) {
            super(fm);
            mFragmentsList = fragmentsList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }
}
