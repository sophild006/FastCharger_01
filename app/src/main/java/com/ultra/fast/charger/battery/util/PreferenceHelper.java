package com.ultra.fast.charger.battery.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by csc on 16/6/13.
 */
public class PreferenceHelper {
    private static String PREF_NAME = "fast_charger";

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getString(String key, String defValue) {
        return getSharedPreference(GlobalContext.getAppContext()).getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return getSharedPreference(GlobalContext.getAppContext()).getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return getSharedPreference(GlobalContext.getAppContext()).getLong(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreference(GlobalContext.getAppContext()).getBoolean(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return getSharedPreference(GlobalContext.getAppContext()).getFloat(key, defValue);
    }

    public static void setString(String key, String value) {
        getSharedPreference(GlobalContext.getAppContext()).edit().putString(key, value).apply();
    }

    public static void setInt(String key, int value) {
        getSharedPreference(GlobalContext.getAppContext()).edit().putInt(key, value).apply();
    }

    public static void setLong(String key, long value) {
        getSharedPreference(GlobalContext.getAppContext()).edit().putLong(key, value).apply();
    }

    public static void setFloat(String key, float value) {
        getSharedPreference(GlobalContext.getAppContext()).edit().putFloat(key, value).apply();
    }

    public static void setBoolean(String key, boolean value) {
        getSharedPreference(GlobalContext.getAppContext()).edit().putBoolean(key, value).apply();
    }

    public static void setStringSet(String key, Set<String> value) {
        getSharedPreference(GlobalContext.getAppContext()).edit().putStringSet(key, value).apply();
    }

    public static Set<String> getStringSet(String key, Set<String> value) {
        return getSharedPreference(GlobalContext.getAppContext()).getStringSet(key, value);
    }
}
