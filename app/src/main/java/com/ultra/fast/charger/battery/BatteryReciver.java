package com.ultra.fast.charger.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BatteryReciver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent intent2 = new Intent();
        intent2.setClassName(context.getPackageName(), context.getPackageName() + ".activity.FastChargerActivity");
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);
    }
}