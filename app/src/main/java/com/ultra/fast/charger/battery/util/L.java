package com.ultra.fast.charger.battery.util;

import android.util.Log;

import com.ultra.fast.charger.battery.BuildConfig;

/**
 * Created by csc on 16/6/6.
 */
public class L {
    private static final String DEFAULT_TAG = "wwq";

    public static void d(String tag, Object o) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, String.valueOf(o));
        }
    }

    public static void d(Object o) {
        if (BuildConfig.DEBUG) {
            Log.d(DEFAULT_TAG, String.valueOf(o));
        }
    }

    public static void e(String tag, Object o) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, String.valueOf(o));
        }
    }
}
