package com.ultra.fast.charger.battery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ultra.fast.charger.battery.activity.BatterDetailActivity;
import com.ultra.fast.charger.battery.activity.BatterSaverActivity;
import com.ultra.fast.charger.battery.activity.FastChargerActivity;
import com.ultra.fast.charger.battery.activity.FeedActivity;
import com.ultra.fast.charger.battery.activity.TermsActivity;
import com.ultra.fast.charger.battery.base.BaseActivity;
import com.ultra.fast.charger.battery.bean.VersionBean;
import com.ultra.fast.charger.battery.constant.Config;
import com.ultra.fast.charger.battery.util.L;
import com.ultra.fast.charger.battery.util.OkHttpHelper;
import com.ultra.fast.charger.battery.util.PreferenceHelper;
import com.ultra.fast.charger.battery.util.ThreadManager;
import com.ultra.fast.charger.battery.util.Utils;
import com.ultra.fast.charger.battery.view.ArcProgress;
import com.ultra.fast.charger.battery.view.BackDialog;
import com.ultra.fast.charger.battery.view.UpdateDialog;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ArcProgress arc_progress;
    private int rotateValue;
    private int brightnessValue;
    private int timeoutValue;//timeoutValue
    private int audioValue;
    private AudioManager audioManager;
    private WifiManager wifiManager;
    private BluetoothAdapter defaultAdapter;
    private ImageView ivRoate, ivBrightness, ivMode, ivWifi, ivBluetooth, ivTimeout;
    private Window window;
    private TextView tvOptimize, tvTemperate, tvVoltage, tvLevel, tvAlert;
    private LinearLayout llOptimizeCount, llShare, llRateView;

    private Toolbar toolbar;
    private CardView arcCarView, toolsCardView, tempCardView, feedCardView, share_cardView;
    private Integer alertCount = Integer.valueOf(0);
    private DrawerLayout mDrawerLayout;
    private TextView tvLeftTitle;
    private RelativeLayout rlRecommandView, rlFeedback, rlUpdateView, rlPrivacyView;

    private Button btnFeedback, btnRateStar;
    private int versionCode;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (defaultAdapter != null) {
                        if (defaultAdapter.isEnabled()) {
                            ivBluetooth.setImageResource(R.drawable.bluetooth_btn_default);
                            defaultAdapter.disable();
                            return;
                        }
                        ivBluetooth.setImageResource(R.drawable.bluetooth_btn_active);
                        defaultAdapter.enable();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        window = getWindow();
        initView();
        initAlertCount();
        initBatteryData();
        initExitDialog();
        wifiReceiver = new WifiReceiver();
        registerWifiReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initValue();
        try {
//            this.brightnessValue = Settings.System.getInt(getContentResolver(), "screen_brightness");
//            Log.d("wwq", "rotateValue: " + rotateValue);
//            this.timeoutValue = Settings.System.getInt(getContentResolver(), "screen_off_timeout");
//            if (this.timeoutValue > 40000) {
//                resetTimeout(3);
//                this.timeoutValue = 40000;
//            }
            initData();
            initAlertCount();
            initBatteryData();
        } catch (Exception e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }
    }

    private void initData() {
        Log.d("wwq", "onResume");
        if (wifiManager.isWifiEnabled()) {
            this.ivWifi.setImageResource(R.drawable.wifi_btn_active);
        } else {
            this.ivWifi.setImageResource(R.drawable.wifi_btn_default);
        }
        Log.d("wwq", "rotateValue: " + rotateValue);
        if (this.rotateValue == 1) {
            this.ivRoate.setImageResource(R.drawable.rotate_btn_active);
        } else {
            this.ivRoate.setImageResource(R.drawable.rotate_btn_default);
        }

        if (this.defaultAdapter != null) {
            if (this.defaultAdapter.isEnabled()) {
                this.ivBluetooth.setImageResource(R.drawable.bluetooth_btn_active);
            } else {
                this.ivBluetooth.setImageResource(R.drawable.bluetooth_btn_default);
            }
        }
        if (this.brightnessValue > 20) {
            this.ivBrightness.setImageResource(R.drawable.brightness_btn_active);
        } else {
            this.ivBrightness.setImageResource(R.drawable.brightness_btn_default);
        }
        if (this.timeoutValue == 10000) {
            this.ivTimeout.setImageResource(R.drawable.ten_btn);
        } else if (this.timeoutValue == 20000) {
            this.ivTimeout.setImageResource(R.drawable.twenty_btn);
        } else if (this.timeoutValue == 30000) {
            this.ivTimeout.setImageResource(R.drawable.thirty);
        } else if (this.timeoutValue == 40000) {
            this.ivTimeout.setImageResource(R.drawable.forty_btn);
        } else {
            this.ivTimeout.setImageResource(R.drawable.forty_btn);
            resetTimeout(3);
            this.timeoutValue = 40000;
        }
        switch (this.audioManager.getRingerMode()) {
            case 0:
                this.ivMode.setImageResource(R.drawable.mode_btn_default);
                this.audioValue = Integer.valueOf(0);
                return;
            case 1:
                this.ivMode.setImageResource(R.drawable.mode_btn_active2);
                this.audioValue = Integer.valueOf(1);
                return;
            case 2:
                this.ivMode.setImageResource(R.drawable.mode_btn_active);
                this.audioValue = Integer.valueOf(2);
                return;
            default:
                return;
        }
    }


    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        String title = "<font color='#03aa5a'>" + "ULTRA" + "</font>" + " " +"FAST CHARGER";
        toolbar.setTitle(Html.fromHtml(title));
        toolbar.setNavigationIcon(R.drawable.menu_btn_w_46);
        setSupportActionBar(this.toolbar);
        arc_progress = (ArcProgress) findViewById(R.id.arc_progress);
        ivBrightness = (ImageView) findViewById(R.id.iv_brightness);
        ivMode = (ImageView) findViewById(R.id.iv_mode);
        ivRoate = (ImageView) findViewById(R.id.iv_rotate);
        ivWifi = (ImageView) findViewById(R.id.iv_wifi);
        ivBluetooth = (ImageView) findViewById(R.id.iv_bluetooth);
        ivTimeout = (ImageView) findViewById(R.id.iv_timeout);

        tvOptimize = (TextView) findViewById(R.id.tv_optimize);
        tvTemperate = (TextView) findViewById(R.id.tv_temperate);
        tvVoltage = (TextView) findViewById(R.id.tv_voltage);
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvAlert = (TextView) findViewById(R.id.tv_alert);


        tvLeftTitle = (TextView) findViewById(R.id.left_tv_title);
        String menu_title = getResources().getString(R.string.app_prename) + " " + "<font color='#03aa5a'>" + getResources().getString(R.string.app_centername) + "</font>";
        tvLeftTitle.setText(Html.fromHtml(menu_title));

        llOptimizeCount = (LinearLayout) findViewById(R.id.ll_optimize_count);
        llShare = (LinearLayout) findViewById(R.id.ll_share);

        rlRecommandView = (RelativeLayout) findViewById(R.id.rl_recomand);
        rlFeedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        llRateView = (LinearLayout) findViewById(R.id.ll_rate);
        rlUpdateView = (RelativeLayout) findViewById(R.id.rl_update);
        rlPrivacyView = (RelativeLayout) findViewById(R.id.rl_privacy);


        arcCarView = (CardView) findViewById(R.id.arc_cardView);
        toolsCardView = (CardView) findViewById(R.id.tools_cardView);
        tempCardView = (CardView) findViewById(R.id.temp_cardView);
        feedCardView = (CardView) findViewById(R.id.feed_cardView);
        share_cardView = (CardView) findViewById(R.id.share_cardView);


        btnFeedback = (Button) findViewById(R.id.btn_feedbak);
        btnRateStar = (Button) findViewById(R.id.btn_ratenow);


        arcCarView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        toolsCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        tempCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        feedCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        share_cardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        arcCarView.setCardElevation(0.0f);
        toolsCardView.setCardElevation(0.0f);
        tempCardView.setCardElevation(0.0f);
        feedCardView.setCardElevation(0.0f);
        share_cardView.setCardElevation(0.0f);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });
        initEvent();
    }

    private void initEvent() {
        ivBrightness.setOnClickListener(this);
        ivRoate.setOnClickListener(this);
        ivMode.setOnClickListener(this);
        ivWifi.setOnClickListener(this);
        ivBluetooth.setOnClickListener(this);

        tvOptimize.setOnClickListener(this);
        ivTimeout.setOnClickListener(this);
        tempCardView.setOnClickListener(this);

        llShare.setOnClickListener(this);
        llOptimizeCount.setOnClickListener(this);

        rlRecommandView.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        llRateView.setOnClickListener(this);
        rlUpdateView.setOnClickListener(this);
        rlPrivacyView.setOnClickListener(this);
        feedCardView.setOnClickListener(this);


        btnFeedback.setOnClickListener(this);
        btnRateStar.setOnClickListener(this);

        initValue();
    }

    private void initValue() {
        try {
            this.window = getWindow();
            wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            this.defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            this.brightnessValue = Settings.System.getInt(getContentResolver(), "screen_brightness");
            this.rotateValue = Settings.System.getInt(getContentResolver(), "accelerometer_rotation");
            this.timeoutValue = Settings.System.getInt(getContentResolver(), "screen_off_timeout");
        } catch (Settings.SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }
        try {
            //获取当前应用的版本号
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_wifi:
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    this.ivWifi.setImageResource(R.drawable.wifi_btn_default);
                    return;
                }
                wifiManager.setWifiEnabled(true);
                this.ivWifi.setImageResource(R.drawable.wifi_btn_active);
                break;
            case R.id.iv_bluetooth:
                if (defaultAdapter != null) {
                    if (defaultAdapter.isEnabled()) {
//                        this.ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_off);
                        defaultAdapter.disable();
                        return;
                    }
//                    this.ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_on);
                    defaultAdapter.enable();
                }
                break;
            case R.id.iv_brightness:
                WindowManager.LayoutParams attributes;
                if (this.brightnessValue > 20) {
                    Settings.System.putInt(getContentResolver(), "screen_brightness", 20);
                    attributes = this.window.getAttributes();
                    attributes.screenBrightness = 20.0f;
                    this.window.setAttributes(attributes);
                    this.brightnessValue = 20;
                    ivBrightness.setImageResource(R.drawable.brightness_btn_default);
                    return;
                }
                Settings.System.putInt(getContentResolver(), "screen_brightness", 254);
                attributes = this.window.getAttributes();
                attributes.screenBrightness = 254.0f;
                this.window.setAttributes(attributes);
                ivBrightness.setImageResource(R.drawable.brightness_btn_active);

                this.brightnessValue = 254;

                break;
            case R.id.iv_mode:
                switch (this.audioManager.getRingerMode()) {
                    case 2:
                        this.audioManager.setRingerMode(0);
                        ivMode.setImageResource(R.drawable.mode_btn_default);
                        return;
                    case 0:
                        this.audioManager.setRingerMode(1);
                        ivMode.setImageResource(R.drawable.mode_btn_active2);
                        return;
                    case 1:
                        this.audioManager.setRingerMode(2);
                        ivMode.setImageResource(R.drawable.mode_btn_active);
                        return;
                }
                break;
            case R.id.iv_rotate:
                Log.d("wwq", "rotateValue: " + rotateValue);
                if (rotateValue == 0) {
                    Settings.System.putInt(getContentResolver(), "accelerometer_rotation", 1);
                    ivRoate.setImageResource(R.drawable.rotate_btn_active);
                    rotateValue = 1;
                    return;
                }
                Settings.System.putInt(getContentResolver(), "accelerometer_rotation", 0);
                ivRoate.setImageResource(R.drawable.rotate_btn_default);
                rotateValue = 0;
                break;

            case R.id.iv_timeout:
                if (this.timeoutValue == 10000) {
                    this.ivTimeout.setImageResource(R.drawable.twenty_btn);
                    resetTimeout(1);
                    this.timeoutValue = 20000;
                    return;
                } else if (this.timeoutValue == 20000) {
                    this.ivTimeout.setImageResource(R.drawable.thirty);
                    resetTimeout(2);
                    this.timeoutValue = 30000;
                    return;
                } else if (this.timeoutValue == 30000) {
                    this.ivTimeout.setImageResource(R.drawable.forty_btn);
                    resetTimeout(3);
                    this.timeoutValue = 40000;
                    return;
                } else if (this.timeoutValue == 40000) {
                    this.ivTimeout.setImageResource(R.drawable.ten_btn);
                    resetTimeout(0);
                    this.timeoutValue = 10000;
                    return;
                } else {
                    this.ivTimeout.setImageResource(R.drawable.forty_btn);
                    resetTimeout(3);
                    this.timeoutValue = 40000;
                    return;
                }
                // System.putInt(getContentResolver(), "screen_off_timeout", i2);

            case R.id.tv_optimize:
                optimizeBattery();
                break;
            case R.id.ll_share:
                shareToFriend();
                break;
            case R.id.temp_cardView:
                startActivity(new Intent(this, BatterDetailActivity.class));
                break;
            case R.id.feed_cardView:
//                startActivity(new Intent(this, FastChargerActivity.class));
                break;
            case R.id.ll_optimize_count:
                Intent intent1 = new Intent(this, BatterSaverActivity.class);
                intent1.putExtra("SetValue", 0);
                startActivity(intent1);
                break;
            case R.id.rl_recomand:
                shareToFriend();
                break;
            case R.id.ll_rate:
                goRateStar();
                break;
            case R.id.rl_update:
                upadteVersion();
                closeDrawer();
                break;
            case R.id.rl_privacy:
                startActivity(new Intent(MainActivity.this, TermsActivity.class));
                break;
            case R.id.rl_feedback:
            case R.id.btn_feedbak:
                startActivity(new Intent(MainActivity.this, FeedActivity.class));
                break;
            case R.id.btn_ratenow:
                goRateStar();
                break;
        }

    }

    private void shareToFriend() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", "Checkout" + " " + getResources().getString(R.string.app_name) + ", " + "the free app for save your battery with" + " " + getResources().getString(R.string.app_name) + ". https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intent, "share" + " " + getResources().getString(R.string.app_name)));

    }

    private void goRateStar() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
            return;
        } catch (ActivityNotFoundException e2) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            return;
        }
    }

    private void resetTimeout(int i) {
        int i2;
        switch (i) {
            case 0:
                i2 = 10000;
                break;
            case 1:
                i2 = 20000;
                break;
            case 2:
                i2 = 30000;
                break;
            case 3:
                i2 = 40000;
                break;
            default:
                i2 = -1;
                break;
        }
        Settings.System.putInt(getContentResolver(), "screen_off_timeout", i2);
    }

    private void dismissAlpha(final View view) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0).setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                translateView();
                Toast.makeText(MainActivity.this, "Power consumption applications have been cleaned up", Toast.LENGTH_LONG).show();
            }
        });
        valueAnimator.start();
    }

    private void translateView() {
        float v = Utils.dp2px(getResources(), 3);
        ObjectAnimator animator = ObjectAnimator.ofFloat(llOptimizeCount, "translationY", v);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
//            case R.id.Setting:
//                Intent intent = new Intent(getApplicationContext(), SettingPrefrence.class);
//                startActivity(intent);
//                break;
//            default:
//                return super.onOptionsItemSelected(menuItem);
        }
        return false;
    }

    BroadcastReceiver batterReceiver;

    private void initBatteryData() {
        this.batterReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int j = -1;
                int k = intent.getIntExtra("level", -1);
                int m = intent.getIntExtra("scale", -1);
                int i = j;
                if (k >= 0) {
                    i = j;
                    if (m > 0) {
                        i = k * 100 / m;
                        Log.e("%", "" + i);
                    }
                }
                tvTemperate.setText((intent.getIntExtra("temperature", 0) / 10) + Character.toString('°') + " C");
                tvVoltage.setText((((float) intent.getIntExtra("voltage", 0)) / 1000.0f) + Character.toString('°') + " V");
                tvLevel.setText(Integer.toString(intent.getIntExtra("level", 0)));
                final Timer timer = new Timer();
                final int finalI = i;
                timer.schedule(new TimerTask() {
                                   public void run() {
                                       MainActivity.this.runOnUiThread(new Runnable() {
                                           public void run() {
                                               if (arc_progress.getProgress() == finalI) {
                                                   arc_progress.setProgress(finalI);
                                                   arc_progress.setBottomText(MainActivity.this.getResources().getString(R.string.main_battery));
                                                   timer.cancel();
                                                   return;
                                               }
                                               arc_progress.setProgress(arc_progress.getProgress() + 1);
                                               arc_progress.setBottomText(getResources().getString(R.string.main_battery));
                                           }
                                       });
                                   }
                               }
                        , 1000L, i);
            }
        };
        IntentFilter localIntentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.batterReceiver, localIntentFilter);
    }

    private AlertDialog.Builder builder;

    private void initExitDialog() {
        this.builder = new AlertDialog.Builder(this);
        this.builder.setTitle(getResources().getString(R.string.like_this_app));
        this.builder.setMessage(getResources().getString(R.string.main_rate_summary));
        this.builder.setPositiveButton(getResources().getString(R.string.rate_5_star), new PositiveListener(this));
        this.builder.setNegativeButton(getResources().getString(R.string.exit), new NegativeListener(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showBackDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showBackDialog() {
        final BackDialog backdialog = new BackDialog(this);
        backdialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backdialog.dismiss();
                finish();
            }
        });
        //退出的dialog
        backdialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backdialog.dismiss();
                goRateStar();
            }
        });
        backdialog.setCloseListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backdialog.dismiss();
            }
        });
        backdialog.getWindow().setLayout((int) getResources().getDimension(R.dimen.with),
                (int) getResources().getDimension(R.dimen.medium_hight));
        backdialog.show();
    }

    class PositiveListener implements DialogInterface.OnClickListener {
        final /* synthetic */ MainActivity activity;

        PositiveListener(MainActivity mainActivity) {
            this.activity = mainActivity;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            try {
                this.activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.activity.getPackageName())));
            } catch (ActivityNotFoundException e) {
                this.activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + this.activity.getPackageName())));
            }
        }
    }


    class NegativeListener implements DialogInterface.OnClickListener {
        final /* synthetic */ MainActivity activity;

        NegativeListener(MainActivity mainActivity) {
            this.activity = mainActivity;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            audioManager.setRingerMode(audioValue);
            resetTimeout(3);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.wifiReceiver);
        unregisterReceiver(this.batterReceiver);
    }

    private WifiReceiver wifiReceiver;

    private void registerWifiReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer();
    }

    private void closeDrawer() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
        }
    }

    private class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (!TextUtils.isEmpty(action) && action.equalsIgnoreCase(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                    switch (wifiState) {

                        case WifiManager.WIFI_STATE_DISABLING:
                            ivWifi.setImageResource(R.drawable.wifi_btn_default);
                            break;
                        case WifiManager.WIFI_STATE_ENABLING:
                            ivWifi.setImageResource(R.drawable.wifi_btn_active);
                            break;
                    }
//                    else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
//                        //已经关闭
//                    } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
//                        //已经打开
//                    }
                } else if (!TextUtils.isEmpty(action) && action.equalsIgnoreCase(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            ivBluetooth.setImageResource(R.drawable.bluetooth_btn_active);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            ivBluetooth.setImageResource(R.drawable.bluetooth_btn_default);
                            break;

                    }
                }

            }
        }
    }


    private void optimizeBattery() {
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(0);
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ApplicationInfo applicationInfo : installedApplications) {
            Log.e("wwq", applicationInfo.packageName + "");
            if ((applicationInfo.flags & 1) != 1) {
                activityManager.killBackgroundProcesses(applicationInfo.packageName);
            }
        }
//        ((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(false);
//        this.ivWifi.setImageResource(R.drawable.wifi_btn_default);
        if (this.defaultAdapter != null && this.defaultAdapter.isEnabled()) {
            this.defaultAdapter.disable();
        }
        if (this.brightnessValue > 20) {
            Settings.System.putInt(getContentResolver(), "screen_brightness", 20);
            WindowManager.LayoutParams attributes = this.window.getAttributes();
            attributes.screenBrightness = 20.0f;
            this.window.setAttributes(attributes);
            this.ivBrightness.setImageResource(R.drawable.brightness_btn_default);
            this.brightnessValue = 20;
        }
        this.ivTimeout.setImageResource(R.drawable.ten_btn);
        timeoutValue = 10000;
        Settings.System.putInt(getContentResolver(), "screen_off_timeout", 10000);
        Settings.System.putInt(getContentResolver(), "accelerometer_rotation", 0);
        this.ivRoate.setImageResource(R.drawable.rotate_btn_default);
        this.audioManager.setRingerMode(0);
        this.ivMode.setImageResource(R.drawable.mode_btn_default);
        dismissAlpha(tvOptimize);
    }

    public void initAlertCount() {
        this.alertCount = Integer.valueOf(0);
        if (Boolean.valueOf(((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps")).booleanValue()) {
            this.alertCount = Integer.valueOf(this.alertCount.intValue() + 1);
        }
        Log.d("wwq", "isAir:  " + !Utils.isAirPlaneOpend(getApplicationContext()));
        if (!Utils.isAirPlaneOpend(getApplicationContext())) {
            this.alertCount = Integer.valueOf(this.alertCount.intValue() + 1);
            if (!PreferenceHelper.getBoolean(Config.FIRST_IN, false)) {
                if (Utils.isMobleDataOpend().booleanValue()) {
                    this.alertCount = Integer.valueOf(this.alertCount.intValue() + 1);
                }
            }
        }
        this.tvAlert.setText(String.valueOf(this.alertCount));
        if (this.alertCount.intValue() == 0) {
            this.llOptimizeCount.setVisibility(View.GONE);
        }
    }


    private void upadteVersion() {
        Runnable updateRun = new Runnable() {
            @Override
            public void run() {
                OkHttpHelper.getInstance().getAsync(Config.url+getPackageName(), new OkHttpHelper.IResultCallBack() {
                    @Override
                    public void getResult(int code, String ret) {
                        if (code == OkHttpHelper.HTTP_FAILURE || TextUtils.isEmpty(ret)) {
                            Snackbar.make(getContatiner(), getResources().getString(R.string.netfailed), Snackbar.LENGTH_SHORT).show();
                        } else {
                            try {
                                L.d(ret + "version");
                                Gson gson = new Gson();
                                VersionBean bean = gson.fromJson(ret, VersionBean.class);
                                if (bean.ret != 200) {
                                    L.d("versionret:" + bean.ret);
                                    Snackbar make = Snackbar.make(getContatiner(), getResources().getString(R.string.netfailed), Snackbar.LENGTH_SHORT);
                                    View view = make.getView();
                                    TextView mesage = (TextView) view.findViewById(R.id.snackbar_text);
                                    mesage.setTextColor(getResources().getColor(R.color.white));
                                    mesage.setGravity(Gravity.CENTER);
                                    make.show();
                                    return;
                                }
                                L.d("version", "bean.msg:" + bean.msg);
                                compareallVersions(bean.msg);
                            } catch (Exception e) {
                            }
                        }
                    }
                });
            }
        };
        ThreadManager.execute(updateRun);
    }

    private void compareallVersions(int serverVersion) {
        L.d("version", "versionCode:" + versionCode);
        L.d("version", "serverVersion:" + serverVersion);
        if (versionCode >= serverVersion) {
            L.d("version", "versionCode1:" + versionCode);
            L.d("version", "serverVersion1:" + serverVersion);
            Snackbar make = Snackbar.make(getContatiner(), getResources().getString(R.string.your_current_version_is_up_to_date), Snackbar.LENGTH_SHORT);
            View view = make.getView();
            TextView mesage = (TextView) view.findViewById(R.id.snackbar_text);
            mesage.setTextColor(getResources().getColor(R.color.white));
            mesage.setGravity(Gravity.CENTER);
            make.show();
        } else {
            L.d("version", "versionCode2:" + versionCode);
            L.d("version", "serverVersion2:" + serverVersion);

            final UpdateDialog dialog = new UpdateDialog(this);
            dialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    goRateStar();
                }
            });
            dialog.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //访问网络下载更新
                    dialog.dismiss();

                }
            });
            dialog.getWindow().setLayout((int) getResources().getDimension(R.dimen.with), (int) getResources().getDimension(R.dimen.hight));
            dialog.show();
        }
    }
}
