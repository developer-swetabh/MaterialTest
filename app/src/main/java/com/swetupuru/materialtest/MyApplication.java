package com.swetupuru.materialtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by SWETABH on 12/19/2015.
 */
public class MyApplication extends Application {
    public static final String API_KEY_ROTTEN_TOMATOES = "54wzfswsa4qmjg8hjwa64d4c";
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }

    public static MyApplication getsInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
