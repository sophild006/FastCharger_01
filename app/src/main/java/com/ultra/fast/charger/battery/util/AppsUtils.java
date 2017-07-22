package com.ultra.fast.charger.battery.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ultra.fast.charger.battery.bean.AppEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wwq on 2017/7/22.
 */

public class AppsUtils {
    public static ArrayList<AppEntity> getAllAppInfoSize(int size) {
        synchronized (AppsUtils.class) {
            ArrayList<AppEntity> allApp = new ArrayList<AppEntity>();
            PackageManager pm = GlobalContext.getAppContext().getPackageManager();
            List<PackageInfo> packageInfos = getAllApps(GlobalContext.getAppContext());
            for (int i = 0; i < packageInfos.size(); i++) {
                if (i == size) {
                    break;
                }
                PackageInfo pinfo = packageInfos.get(i);
                boolean iscontain = false;
                for (AppEntity entity : allApp) {
                    if (entity.getAppName().equals(pm.getApplicationLabel(pinfo.applicationInfo).toString())) {
                        iscontain = true;
                        break;
                    }
                }
                if (iscontain) {
                    continue;
                }

                if (pinfo.packageName.equals(GlobalContext.getAppContext().getPackageName())) {
                    continue;
                }

                AppEntity entity = new AppEntity();
                entity.setAppIcon(pm.getApplicationIcon(pinfo.applicationInfo));//应用的图标
                entity.setAppName(pm.getApplicationLabel(pinfo.applicationInfo).toString());//应用的名称
                entity.setPackageName(pinfo.applicationInfo.packageName);//应用的包名
                allApp.add(entity);

            }
            return allApp;
        }
    }

    private static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }
}
