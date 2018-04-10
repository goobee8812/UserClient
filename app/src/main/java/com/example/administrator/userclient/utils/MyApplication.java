package com.example.administrator.userclient.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * 全局获取context
 * Created by Goobee_yuer on 2018/3/7.
 */

public class MyApplication extends Application {
    private static Context sContext;

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate() {
        sContext = getApplicationContext();
        LitePalApplication.initialize(sContext);
    }
    public static Context getContext(){
        return sContext;
    }
}
