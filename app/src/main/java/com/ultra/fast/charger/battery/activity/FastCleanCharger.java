package com.ultra.fast.charger.battery.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ultra.fast.charger.battery.R;
import com.ultra.fast.charger.battery.base.BaseActivity;
import com.ultra.fast.charger.battery.bean.AppEntity;
import com.ultra.fast.charger.battery.util.AppsUtils;
import com.ultra.fast.charger.battery.util.UiUtils;
import com.ultra.fast.charger.battery.view.CacheLoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class FastCleanCharger extends BaseActivity {
    private LinearLayout llAppsLayout;
    private ImageView ivApps0, ivApps1, ivApps2, ivApps3, ivApps4;
    private List<ImageView> mIcons = new ArrayList<>();
    private LinearLayout llCheckView;
    private CacheLoadingView ivBlueCheck, ivDataCheck;
    private RelativeLayout rlCheckBlue,rlCheckData;
    private TextView tvCleanDesc;

    private RelativeLayout rlCLeanView,rlResulutView;
    private LinearLayout llBatteryLeft,llBatteryOptimize;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    startMoveAnim(rlCheckBlue,rlCheckData,ivBlueCheck,0);
                    break;
                case 1:
                    startMoveAnim(rlCheckData,null,ivDataCheck,1);
                    break;
                case 2:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fast_charger_clean);
        intiView();
    }

    private int size;
    private int count = 0;

    private void intiView() {

        llAppsLayout = (LinearLayout) findViewById(R.id.ll_clean_ram);
        llCheckView = (LinearLayout) findViewById(R.id.rl_check_state);
        ivBlueCheck = (CacheLoadingView) findViewById(R.id.iv_check_blueteeth);
        ivDataCheck = (CacheLoadingView) findViewById(R.id.iv_check_mobile_data);

        rlCheckBlue= (RelativeLayout) findViewById(R.id.rl_check_blueteeth);
        rlCheckData= (RelativeLayout) findViewById(R.id.rl_check_data);
        rlCLeanView= (RelativeLayout) findViewById(R.id.rl_clean_page);
        tvCleanDesc= (TextView) findViewById(R.id.tv_clean_desc);

        //result:
        rlResulutView= (RelativeLayout) findViewById(R.id.rl_result_page);
        llBatteryLeft= (LinearLayout) findViewById(R.id.ll_result_battery_left);
        llBatteryOptimize= (LinearLayout) findViewById(R.id.ll_result_battery_optimize);
        ivApps0 = (ImageView) findViewById(R.id.iv_app_0);
        ivApps1 = (ImageView) findViewById(R.id.iv_app_1);
        ivApps2 = (ImageView) findViewById(R.id.iv_app_2);
        ivApps3 = (ImageView) findViewById(R.id.iv_app_3);
        ivApps4 = (ImageView) findViewById(R.id.iv_app_4);
        mIcons.clear();
        mIcons.add(ivApps0);
        mIcons.add(ivApps1);
        mIcons.add(ivApps2);
        mIcons.add(ivApps3);
        mIcons.add(ivApps4);

        ArrayList<AppEntity> allAppInfoSize = AppsUtils.getAllAppInfoSize(-1);
        if (allAppInfoSize != null && allAppInfoSize.size() > 0) {
            if (allAppInfoSize.size() > 5) {
                size = 5;
            } else {
                size = allAppInfoSize.size();
            }
            for (int i = 0; i < size; i++) {
                mIcons.get(i).setImageDrawable(allAppInfoSize.get(i).getAppIcon());

            }
        }
        startAnim(0, 500);
    }

    private void startAnim(int num, long delay) {

        if (num > size - 1) {
            num = 0;
            for (int i = 0; i < size; i++) {
                mIcons.get(i).setVisibility(View.INVISIBLE);
            }
            startAnim(num, 500);
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(150);
        final int finalNum = num;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                mIcons.get(finalNum).setAlpha(alpha);
                mIcons.get(finalNum).setVisibility(View.VISIBLE);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (count > size*5) {
                    startCheck();
                    return;
                } else {
                    count++;
                }
                startAnim(finalNum + 1, 0);
            }
        });
        valueAnimator.setStartDelay(delay);
        valueAnimator.start();
    }

    private void startCheck() {
        llAppsLayout.setVisibility(View.GONE);
        llCheckView.setVisibility(View.VISIBLE);
        moveTopAnimation(rlCheckBlue,UiUtils.dip2px(80),-UiUtils.dip2px(10));
        moveTopAnimation(rlCheckData,UiUtils.dip2px(80),-UiUtils.dip2px(40));
//        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_check);
//        ivBlueCheck.setAnimation(animation);
//        ivDataCheck.setAnimation(animation);
        tvCleanDesc.setText("Adjust phone state...");
        handler.sendEmptyMessageDelayed(0,3000);
    }
    private void startMoveAnim(final RelativeLayout rlView,final RelativeLayout targetView, final CacheLoadingView ivView,final int flag) {
        ivView.setFinishLoading(true);
        ivView.setImageResource(R.drawable.tick_img_g_34);
        ValueAnimator moveAnimator=ValueAnimator.ofFloat(0, -UiUtils.getScreenWidth()).setDuration(500);
        moveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                rlView.setX(value);
            }
        });
        moveAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                switch (flag){
                    case 0:
                        if(targetView!=null){
                            moveTopAnimation(targetView,UiUtils.dip2px(40),-UiUtils.dip2px(10));
                            handler.sendEmptyMessageDelayed(1,2000);
                        }
                        break;
                    case 1:
                        rlCLeanView.setVisibility(View.GONE);
                        rlResulutView.setVisibility(View.VISIBLE);
                        startResultAnim(llBatteryLeft,UiUtils.dip2px(120),0);
                        startResultAnim(llBatteryOptimize,UiUtils.dip2px(200),UiUtils.dip2px(130));
                        break;
                }
            }
        });
        moveAnimator.setStartDelay(1000);
        moveAnimator.start();
    }

    private void startResultAnim(final View view, int startY,int endY) {
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(startY,endY).setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                view.setY(value);
            }
        });
        valueAnimator.start();

    }

    private void moveTopAnimation(final RelativeLayout rlView, int startY,int transY) {
        rlView.setVisibility(View.VISIBLE);


        ValueAnimator valueAnimator=ValueAnimator.ofFloat(startY,-transY).setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                Log.d("wwq","value: "+value);
                rlView.setY(value);
            }
        });
        valueAnimator.start();
//        rlView.setTranslationY(transY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        count = 0;
        ivDataCheck.clearAnimation();

    }
}
