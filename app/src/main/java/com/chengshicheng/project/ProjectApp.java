package com.chengshicheng.project;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Menu;

import com.chengshicheng.project.greendao.GreenDaoHelper;

/**
 * Created by chengshicheng on 2017/2/26.
 */

public class ProjectApp extends Application {
    private static Context context;
    public static Menu menu;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(this);
        GreenDaoHelper.initDatabase();

    }

    public static Context getContext() {
        return context;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
