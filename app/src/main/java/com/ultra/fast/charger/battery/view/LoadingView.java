package com.ultra.fast.charger.battery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.ultra.fast.charger.battery.R;
import com.ultra.fast.charger.battery.util.L;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    : mist
 * Author     : scotch
 * Date       : 2016/7/15 10:48
 */
public class LoadingView extends RelativeLayout {
    private Context context;
    private LoadingIndicator mIndicator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View loadingview = View.inflate(context, R.layout.layout_loading, null);
        mIndicator = (LoadingIndicator) loadingview.findViewById(R.id.loading_indicator);
        L.d("loadingView");
        this.addView(loadingview);
    }

}
