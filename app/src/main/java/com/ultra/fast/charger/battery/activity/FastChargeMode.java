package com.ultra.fast.charger.battery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ultra.fast.charger.battery.R;
import com.ultra.fast.charger.battery.base.BaseActivity;

/**
 * Created by wwq on 2017/7/23.
 */

public class FastChargeMode extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl2xMode, rl3xMode, rl5xMode, rlCustomMode;
    private LinearLayout ll2xExpand, ll3xExpand, ll5xExpand, llCustomExpand;

    private ImageView iv2xArrow, iv3xArrow, iv5xArrow, ivCustomArrow;
    private ImageView iv2xChoose, iv3xChoose, iv5xChoose, ivCustomChoose;
    private boolean isExpand2x, isExpand3x, isExpand5x, isExpandCustom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fast_charge_mode);
        initView();
    }

    private void initView() {

        rl2xMode = (RelativeLayout) findViewById(R.id.rl_2x_mode);
        rl3xMode = (RelativeLayout) findViewById(R.id.rl_3x_mode);
        rl5xMode = (RelativeLayout) findViewById(R.id.rl_5x_mode);
        rlCustomMode = (RelativeLayout) findViewById(R.id.rl_custom_mode);

        ll2xExpand = (LinearLayout) findViewById(R.id.ll_2x_expand);
        ll3xExpand = (LinearLayout) findViewById(R.id.ll_3x_expand);
        ll5xExpand = (LinearLayout) findViewById(R.id.ll_5x_expand);
        llCustomExpand = (LinearLayout) findViewById(R.id.ll_custom_expand);

        initEvent();

    }

    private void initEvent() {
        rl2xMode.setOnClickListener(this);
        rl3xMode.setOnClickListener(this);
        rl5xMode.setOnClickListener(this);
        rlCustomMode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_2x_mode:
                if (!isExpand2x) {
                    isExpand2x = true;
                    ll2xExpand.setVisibility(View.VISIBLE);
                } else {
                    isExpand2x = false;
                    ll2xExpand.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_3x_mode:
                if (!isExpand3x) {
                    isExpand3x = true;
                    ll3xExpand.setVisibility(View.VISIBLE);
                } else {
                    isExpand3x = false;
                    ll3xExpand.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_5x_mode:
                if (!isExpand5x) {
                    isExpand5x = true;
                    ll5xExpand.setVisibility(View.VISIBLE);
                } else {
                    isExpand5x = false;
                    ll5xExpand.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_custom_mode:
                if (!isExpandCustom) {
                    isExpandCustom = true;
                    llCustomExpand.setVisibility(View.VISIBLE);
                } else {
                    isExpandCustom = false;
                    llCustomExpand.setVisibility(View.GONE);
                }
                break;
        }
    }
}
