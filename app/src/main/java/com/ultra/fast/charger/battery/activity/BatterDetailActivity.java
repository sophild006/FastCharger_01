package com.ultra.fast.charger.battery.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.ultra.fast.charger.battery.R;
import com.ultra.fast.charger.battery.base.BaseActivity;

/**
 * Created by Administrator on 2017/7/6.
 */

public class BatterDetailActivity extends BaseActivity {
    String[] categoryNameList;
    String[] categoryValueList = new String[6];
    Integer[] categoryIconList;
    BatterDetailsAdapter mAdapter;
    ListView listView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_battery_detail);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setTitleTextColor(-1);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.batter_information));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.listView = (ListView) findViewById(R.id.detailList);
        categoryNameList = new String[]{getResources().getString(R.string.main_temperature), getResources().getString(R.string.main_voltage), getResources().getString(R.string.main_level), getResources().getString(R.string.technology), getResources().getString(R.string.health)};
        categoryIconList = new Integer[]{Integer.valueOf(R.drawable.temperature_ico_g_58), Integer.valueOf(R.drawable.voltage_ico_g_58), Integer.valueOf(R.drawable.level_ico_g_58), Integer.valueOf(R.drawable.tech_ico_g_58), Integer.valueOf(R.drawable.health_ico_g_58)};
        registerReceiver(this.f6136g, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.f6136g);
    }

    public BroadcastReceiver f6136g = new C20941(this);

    class C20941 extends BroadcastReceiver {
        BatterDetailActivity activity;

        C20941(BatterDetailActivity batteryDetailActivity) {
            this.activity = batteryDetailActivity;
        }

        public void onReceive(Context context, Intent intent) {
            categoryValueList[0] = (intent.getIntExtra("temperature", 0) / 10) + Character.toString('°') + " C";
            categoryValueList[1] = (((float) intent.getIntExtra("voltage", 0)) / 1000.0f) + Character.toString('°') + " V";
            categoryValueList[2] = Integer.toString(intent.getIntExtra("level", 0));
            categoryValueList[3] = intent.getExtras().getString("technology");
            if (categoryValueList[3].equalsIgnoreCase("")) {
                categoryValueList[3] = "-";
            }
            switch (intent.getIntExtra("health", 0)) {
                case 1:
                    categoryValueList[4] = this.activity.getResources().getString(R.string.unknown);
                    break;
                case 2:
                    categoryValueList[4] = this.activity.getResources().getString(R.string.battery_health_good);
                    break;
                case 3:
                    categoryValueList[4] = this.activity.getResources().getString(R.string.battery_health_overheat);
                    break;
                case 4:
                    categoryValueList[4] = this.activity.getResources().getString(R.string.battery_health_dead);
                    break;
                case 5:
                    categoryValueList[4] = this.activity.getResources().getString(R.string.battery_health_over_voltage);
                    break;
                case 6:
                    categoryValueList[4] = this.activity.getResources().getString(R.string.battery_health_unspecified_failure);
                    break;
                case 7:
                    categoryValueList[4] = this.activity.getResources().getString(R.string.battery_health_cold);
                    break;
            }
            mAdapter = new BatterDetailsAdapter(BatterDetailActivity.this, categoryNameList, categoryValueList, categoryIconList);
            listView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
