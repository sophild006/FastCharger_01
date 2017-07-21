package com.ultra.fast.charger.battery;

import android.app.Application;

import com.ultra.fast.charger.battery.util.GlobalContext;

/**
 * Created by Administrator on 2017/7/7.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalContext.setAppContext(this);
    }
}
