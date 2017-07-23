package com.ultra.fast.charger.battery.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;
import com.ultra.fast.charger.battery.R;
import com.ultra.fast.charger.battery.base.BaseActivity;
import com.ultra.fast.charger.battery.util.Rotatable;

/**
 * Created by wwq on 2017/7/23.
 */

public class FastUnplugActivity extends BaseActivity {
    private LinearLayout llRestoreView;
    private RelativeLayout rlFront,rlBack,rlCardRoot;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_battery_out_pop);
        initView();
        setCameraDistance();
        initData();
    }
    /**
     * 设置数据
     */
    public void initData() {
        rlBack.setVisibility(View.VISIBLE);
        rlFront.setVisibility(View.INVISIBLE);
    }
    private void initView() {
        rlBack= (RelativeLayout) findViewById(R.id.rl_backview);
        rlFront= (RelativeLayout) findViewById(R.id.rl_front_view);
        rlCardRoot= (RelativeLayout) findViewById(R.id.rl_card_root);
//        llRestoreView= (LinearLayout) findViewById(R.id.ll_restore_view);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
            }
        });
        rlFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
            }
        });
    }
    /**
     * 改变视角距离, 贴近屏幕
     */
    private void setCameraDistance() {
        int distance = 20000;
        float scale = getResources().getDisplayMetrics().density * distance;
        rlCardRoot.setCameraDistance(scale);
    }
    private ScaleAnimation sato0 = new ScaleAnimation(1,0,1,1,
            Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);

    private ScaleAnimation sato1 = new ScaleAnimation(0,1,1,1,
            Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
    private void startAnim() {


//        if (View.VISIBLE == rlBack.getVisibility()) {
            rlFront.setRotationY(180f);
//            ViewHelper.setRotationY(rlFront, 180f);//先翻转180，转回来时就不是反转的了
            Rotatable rotatable = new Rotatable.Builder(rlCardRoot)
                    .sides(R.id.rl_backview, R.id.rl_front_view)
                    .direction(Rotatable.ROTATE_Y)
                    .rotationCount(0)
                    .build();
            rotatable.setTouchEnable(false);
            rotatable.rotate(Rotatable.ROTATE_Y, -180, 3000);
//
//        }
//        else if (View.VISIBLE == rlFront.getVisibility()) {
//            Rotatable rotatable = new Rotatable.Builder(rlCardRoot)
//                    .sides(R.id.rl_backview, R.id.rl_front_view)
//                    .direction(Rotatable.ROTATE_Y)
//                    .rotationCount(1)
//                    .listener(new Rotatable.RotationListener() {
//                        @Override
//                        public void onRotationChanged(float newRotationX, float newRotationY) {
//                            rlFront.setVisibility(View.GONE);
//                            rlFront.setVisibility(View.VISIBLE);
//                        }
//                    })
//                    .build();
//            rotatable.setTouchEnable(false);
//            rotatable.rotate(Rotatable.ROTATE_Y, 0, 3000);
//        }


    }
}
