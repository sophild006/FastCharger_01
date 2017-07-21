package com.ultra.fast.charger.battery.activity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings.System;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ultra.fast.charger.battery.R;
import com.ultra.fast.charger.battery.base.BaseActivity;
import com.ultra.fast.charger.battery.constant.Config;
import com.ultra.fast.charger.battery.util.L;
import com.ultra.fast.charger.battery.util.PreferenceHelper;
import com.ultra.fast.charger.battery.util.Utils;

public class BatterSaverActivity extends BaseActivity implements OnClickListener {
    CardView cdLocationView;
    CardView cdAirplaneView;//AroplaneCardView
    CardView cdMobelDataView;//MoblieDataCardView
    Toolbar toolbar;
    TextView btnTurnOff;//LocationTurnOff
    TextView btnAirplaneTurnOn;//AroplaneTurnOn
    TextView btnMobleTurnOff;//MoblieDataTurnOff
    LocationManager locationManager;
    Boolean isGpsOpened;
    TextView tvNoIssue;
    Integer SetValue;
    TextView tvLoacationTitle;//TxtTitleLocation
    TextView tvLocationDesc;//txtDicLocation
    TextView tvAirplaneTitle;//TxtTitleAroplane
    TextView tvAirplaneDesc;//txtDicAroplane
    TextView tvMobleDataTitle;//TxtTitleMobileData
    TextView tvMobleDataDesc;//txtDicMobileData

//    private static boolean m9340a(Context context) {
//        return System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
//    }

    private void initCardView() {
        this.cdLocationView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdAirplaneView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdMobelDataView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdLocationView.setCardElevation(0.0f);
        this.cdAirplaneView.setCardElevation(0.0f);
        this.cdMobelDataView.setCardElevation(0.0f);
    }

    private void initAlertCount() {
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.isGpsOpened = Boolean.valueOf(this.locationManager.isProviderEnabled("gps"));
        if (!this.isGpsOpened.booleanValue()) {
            this.cdLocationView.setVisibility(View.GONE);
        }
        L.d("wwq", "!Utils.isMobleDataOpend().booleanValue()： " + !Utils.isMobleDataOpend().booleanValue());
        L.d("wwq","PreferenceHelper.getBoolean(Config.FIRST_IN, false): "+PreferenceHelper.getBoolean(Config.FIRST_IN, false));
        if (!PreferenceHelper.getBoolean(Config.FIRST_IN, false)) {
            this.cdMobelDataView.setVisibility(View.VISIBLE);
        } else {
            this.cdMobelDataView.setVisibility(View.GONE);
        }
        if (Utils.isAirPlaneOpend(getApplicationContext())) {
            this.cdAirplaneView.setVisibility(View.GONE);
            this.cdMobelDataView.setVisibility(View.GONE);
        } else {
            if (!Utils.isMobleDataOpend().booleanValue()) {
                this.cdMobelDataView.setVisibility(View.GONE);
            }
        }
        if (this.cdLocationView.getVisibility() == View.GONE && this.cdAirplaneView.getVisibility() == View.GONE && this.cdMobelDataView.getVisibility() == View.GONE) {
            this.tvNoIssue.setVisibility(View.VISIBLE);
        }
    }

    private void startSeeting() {
        Intent intent = new Intent("android.settings.SETTINGS");
        intent.addFlags(268435456);
        startActivity(intent);
    }

    private void startMobleSet() {
        startActivity(new Intent("android.settings.AIRPLANE_MODE_SETTINGS"));
    }

    private void startLocationSet() {
        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
    }

    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.LocationCardView:
//                startLocationSet();
//                return;
            case R.id.LocationTurnOff:
                startLocationSet();
                return;
//            case R.id.AroplaneCardView:
//                startMobleSet();
//                return;
            case R.id.AroplaneTurnOn:
                startMobleSet();
                return;
//            case R.id.MoblieDataCardView:
//                startSeeting();
//                return;
            case R.id.MoblieDataTurnOff:
                startSeeting();
                return;
            default:
                return;
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_charge_way);
        this.toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setTitleTextColor(-1);
        setSupportActionBar(this.toolbar);
        if (getIntent() != null) {
            SetValue = getIntent().getIntExtra("SetValue", 0);
            if (this.SetValue.intValue() == 0) {
                getSupportActionBar().setTitle(getResources().getString(R.string.close_tool_title_saver));
            } else {
                getSupportActionBar().setTitle(getResources().getString(R.string.close_tool_title_charger));
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.cdLocationView = (CardView) findViewById(R.id.LocationCardView);
        this.cdAirplaneView = (CardView) findViewById(R.id.AroplaneCardView);
        this.cdMobelDataView = (CardView) findViewById(R.id.MoblieDataCardView);
        this.btnTurnOff = (TextView) findViewById(R.id.LocationTurnOff);
        this.btnAirplaneTurnOn = (TextView) findViewById(R.id.AroplaneTurnOn);
        this.btnMobleTurnOff = (TextView) findViewById(R.id.MoblieDataTurnOff);
        this.tvNoIssue = (TextView) findViewById(R.id.txtNoMoreIssue);
        this.tvLoacationTitle = (TextView) findViewById(R.id.TxtTitleLocation);
        this.tvLocationDesc = (TextView) findViewById(R.id.txtDicLocation);
        this.tvAirplaneTitle = (TextView) findViewById(R.id.TxtTitleAroplane);
        this.tvAirplaneDesc = (TextView) findViewById(R.id.txtDicAroplane);
        this.tvMobleDataTitle = (TextView) findViewById(R.id.TxtTitleMobileData);
        this.tvMobleDataDesc = (TextView) findViewById(R.id.txtDicMobileData);
        this.cdLocationView.setOnClickListener(this);
        this.cdAirplaneView.setOnClickListener(this);
        this.cdMobelDataView.setOnClickListener(this);
        this.btnTurnOff.setOnClickListener(this);
        this.btnAirplaneTurnOn.setOnClickListener(this);
        this.btnMobleTurnOff.setOnClickListener(this);
        initCardView();
        initAlertCount();
        if (this.SetValue != null && SetValue.intValue() == 0) {
            this.tvLoacationTitle.setText(getResources().getString(R.string.close_tool_saver_location_off));
            this.tvLocationDesc.setText(getResources().getString(R.string.close_tool_saver_location_off_desc));
            this.tvAirplaneTitle.setText(getResources().getString(R.string.close_tool_saver_airplane_on));
            this.tvAirplaneDesc.setText(getResources().getString(R.string.close_tool_saver_airplane_on_desc));
            this.tvMobleDataTitle.setText(getResources().getString(R.string.close_tool_saver_internet_off));
            this.tvMobleDataDesc.setText(getResources().getString(R.string.close_tool_saver_internet_off_desc));
            return;
        }
        this.tvLoacationTitle.setText(getResources().getString(R.string.close_tool_location_off));
        this.tvLocationDesc.setText(getResources().getString(R.string.close_tool_location_off_desc));
        this.tvAirplaneTitle.setText(getResources().getString(R.string.close_tool_airplane_on));
        this.tvAirplaneDesc.setText(getResources().getString(R.string.close_tool_airplane_on_desc));
        this.tvMobleDataTitle.setText(getResources().getString(R.string.close_tool_internet_off));
        this.tvMobleDataDesc.setText(getResources().getString(R.string.close_tool_internet_off_desc));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return false;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceHelper.setBoolean(Config.FIRST_IN, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        initAlertCount();
    }
}