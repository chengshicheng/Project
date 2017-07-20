package com.chengshicheng.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, Subject {


    private List<String> mTitleList = new ArrayList<>();//页卡标题集合

    ArrayList<Fragment> fragmentsList = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String NAVIG_ITEM = "choosenNavigationItem";
    private Toolbar mToolbar;
    private TabFragmentPagerAdapter fragmentAdapter;
    private List<Observer> observersList = new ArrayList<Observer>();

    private int requestCode = 100;

    @Override
    public void registerObserver(Observer observer) {

        observersList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observersList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observersList) {
            observer.update();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        refreshTabLayout();
//        registerRefreshBroadcast();
        EventBus.getDefault().register(this);
    }


    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //实现侧滑菜单状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


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
        mViewPager.setOffscreenPageLimit(fragmentsList.size());//可避免切换时重复执行onCreatView()？?
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
                KDTabBaseFragment fragment1 = KDAllFragment.newInstance(
                        this, bundle);
                fragmentsList.add(fragment1);
                mTitleList.add("已签收");
                KDTabBaseFragment fragment2 = KDDoneFragment.newInstance(
                        this, bundle);
                fragmentsList.add(fragment2);
                mTitleList.add("未签收");
                KDTabBaseFragment fragment3 = KDUndoneFragment.newInstance(
                        this, bundle);
                fragmentsList.add(fragment3);

                //为3个KDTabBaseFragment注册观察者模式
                registerObserver(fragment1);
                registerObserver(fragment2);
                registerObserver(fragment3);
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int selectId = item.getItemId();

        if (selectId == R.id.nav_camera) {
            // Handle the camera action
            SPUtil.put(NAVIG_ITEM, 0);
        } else if (selectId == R.id.nav_gallery) {
            SPUtil.put(NAVIG_ITEM, 1);
        } else if (selectId == R.id.nav_slideshow) {
            SPUtil.put(NAVIG_ITEM, 2);
        } else if (selectId == R.id.nav_manage) {
            SPUtil.put(NAVIG_ITEM, 3);
        } else if (selectId == R.id.nav_share) {
        } else if (selectId == R.id.nav_send) {
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


//    /**
//     * 接受到广播后，通知个界面刷新
//     */
//    private class MyLocalBroadCastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            LogUtils.PrintDebug("MyLocalBroadCastReceiver onReceive()");
//            if (StringUtils.refreshAction.equals(intent.getAction())) {
//                notifyObservers();
//            }
//        }
//    }

//    /**
//     * 注册广播接收器
//     */
//    public void registerRefreshBroadcast() {
//        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(StringUtils.refreshAction);
//        MyLocalBroadCastReceiver mRerfreshReceiver = new MyLocalBroadCastReceiver();
//        broadcastManager.registerReceiver(mRerfreshReceiver, intentFilter);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("REFRESH")) {
            notifyObservers();
            LogUtils.PrintDebug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>notifyObservers");

        }
    }
}
