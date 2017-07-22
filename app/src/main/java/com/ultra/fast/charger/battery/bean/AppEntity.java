package com.ultra.fast.charger.battery.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by ZOOMY on 2017/2/6
 */
public class AppEntity {
    private Drawable appIcon;
    private String appName;
    private String packageName;
    private long cacheSize;
    private boolean isAd;
    private String adUrl;
//    private BaseUnionAd ad;
    private String battery;
    private int ram;

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

//    public BaseUnionAd getAd() {
//        return ad;
//    }
//
//    public void setAd(BaseUnionAd ad) {
//        this.ad = ad;
//    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setIsAd(boolean isAd) {
        this.isAd = isAd;
    }



    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public long getCacheSize() {
        return cacheSize;
    }
    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

}
