package com.ultra.fast.charger.battery.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ultra.fast.charger.battery.R;


/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : mist
 * Author     : scotch
 * Date       : 2016/7/8 16:32
 */
public class UpdateDialog extends Dialog {

    private TextView positiveButton, negativeButton;

    public UpdateDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_update_dialog, null);
        positiveButton = (TextView) mView.findViewById(R.id.tv_update);
        negativeButton = (TextView) mView.findViewById(R.id.tv_cancel);
        super.setContentView(mView);

    }


    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);
    }

    /**
     * 取消键监听器
     *
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }
}
