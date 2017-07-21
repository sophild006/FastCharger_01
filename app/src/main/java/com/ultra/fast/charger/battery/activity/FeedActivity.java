package com.ultra.fast.charger.battery.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ultra.fast.charger.battery.R;
import com.google.gson.Gson;
import com.ultra.fast.charger.battery.base.BaseActivity;
import com.ultra.fast.charger.battery.bean.FeedBackBean;
import com.ultra.fast.charger.battery.bean.FeedBackResultBean;
import com.ultra.fast.charger.battery.constant.Config;
import com.ultra.fast.charger.battery.util.L;
import com.ultra.fast.charger.battery.util.OkHttpHelper;
import com.ultra.fast.charger.battery.util.ThreadManager;
import com.ultra.fast.charger.battery.util.Utils;

import java.util.Locale;

/**
 * Created by Administrator on 2017/7/10.
 */

public class FeedActivity extends BaseActivity {
    private Toolbar toolbar;
    private EditText evContent, evEmail;
    private TextView tvNumber;
    private int number;
    private RelativeLayout rlSend;
    private int brightnessValue;
    private WindowManager.LayoutParams attributes;
    private Window window;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                finish();
                showSendOk(getResources().getString(R.string.submitted_thanks_for_your_feedback));

            } else if (msg.what == 1) {
                showSnackBar(getResources().getString(R.string.submitted_error));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feedback);
        window = getWindow();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setTitleTextColor(-1);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.feedback));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        evContent = (EditText) findViewById(R.id.ev_content);
        evEmail = (EditText) findViewById(R.id.ev_email);
        rlSend = (RelativeLayout) findViewById(R.id.rl_send);

        tvNumber = (TextView) findViewById(R.id.tv_number);
        initEvent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            this.brightnessValue = Settings.System.getInt(getContentResolver(), "screen_brightness");
            L.d("wwq", "brightnessValue: " + brightnessValue);
            if (brightnessValue <= 20) {
                Settings.System.putInt(getContentResolver(), "screen_brightness", 130);
                attributes = this.window.getAttributes();
                attributes.screenBrightness = 130.0f;
                this.window.setAttributes(attributes);
            } else {

            }
        } catch (Settings.SettingNotFoundException e) {

        }
    }

    private void initEvent() {
        evContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                number = 400 - s.length();
                tvNumber.setText(number + " " + getResources().getString(R.string.sendwords));
//                L.d("ywc", "afterTextChanged:" + s.toString());
//                L.d("ywc", "afterTextChanged:" + s.length());
//                if (s.length() <= 0 || (TextUtils.isEmpty(evContent.getText().toString().trim()))) {
//                    rlSend.setBackgroundResource(R.drawable.shape_sendtext_bg_normal);
//                }
//                else {
//                    rlSend.setBackgroundResource(R.drawable.shape_sendtext_bg);
//                }


            }
        });

        rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedBook();
            }
        });
    }

    private void sendFeedBook() {
        if (!Utils.isNetConnect()) {
            showSnackBar(getResources().getString(R.string.submitted_error));
            return;
        }
        if (TextUtils.isEmpty(evContent.getText())) {
            showSnackBar(getResources().getString(R.string.input_content));
            return;
        }
        if (TextUtils.isEmpty(evEmail.getText())) {
            showSnackBar(getResources().getString(R.string.input_email));
            return;
        }
        if (!isEmailOk(evEmail.getText().toString().trim())) {
            showSnackBar(getResources().getString(R.string.input_correct_email));
            return;
        }
        ThreadManager.execute(new Runnable() {
            @Override
            public void run() {
                if (evContent != null && !TextUtils.isEmpty(evContent.getText().toString().trim())) {
                    try {
                        Gson gson = new Gson();
                        FeedBackBean sendBean = getSendBean();
                        String sendjson = gson.toJson(sendBean);
                        //TODO
                        String post = OkHttpHelper.getInstance().post(Config.FEEDBOOK_SEND_URL, sendjson);
                        FeedBackResultBean feedBackResultBean = gson.fromJson(post, FeedBackResultBean.class);
                        int ret = feedBackResultBean.ret;
                        if (ret == 0) {
                            handler.sendEmptyMessage(0);
                        } else {
                            handler.sendEmptyMessage(1);
                        }

                    } catch (Exception e) {
                        handler.sendEmptyMessage(1);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private FeedBackBean getSendBean() throws PackageManager.NameNotFoundException {
        FeedBackBean bean = new FeedBackBean();
        try {
            bean.model = Build.MODEL;
            bean.pack_name = getPackageName(); //app
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            bean.pack_ver = packageInfo.versionCode;
            bean.os = "Android";
            bean.os_ver = Build.VERSION.RELEASE;
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            bean.device_id = connectionInfo.getBSSID();
            bean.language = Locale.getDefault().getLanguage();   //系统语言

        } catch (Exception e) {
            e.printStackTrace();
        }
        bean.content = evContent.getText().toString().trim();    //反馈内容
        bean.email = evEmail.getText().toString().trim();
        L.d("ywc", "bean:" + bean.toString());
        return bean;
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

    private boolean isEmailOk(String test) {
        if (test.matches("\\w+@\\w+\\.\\w+")) {
            return true;
        }
        return false;
    }

    private void showSendOk(String text) {
        Toast toast = new Toast(this);
        View inflate = View.inflate(this, R.layout.layout_send_okview, null);
        TextView view = (TextView) inflate.findViewById(R.id.tv_text);
        view.setText(text);
        toast.setView(inflate);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showSnackBar(String msg) {
        Snackbar make = Snackbar.make(getContatiner(), msg, Snackbar.LENGTH_LONG);
        View view = make.getView();
        TextView mesage = (TextView) view.findViewById(R.id.snackbar_text);
        mesage.setTextColor(getResources().getColor(R.color.white));
        mesage.setGravity(Gravity.CENTER);
        make.show();
    }
}
