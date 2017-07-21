package com.ultra.fast.charger.battery.view;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.List;

/**
 * Created by csc on 15/11/9.
 */
public abstract class BaseIndicatorController {

    private View mTarget;


    public void setTarget(View target) {
        this.mTarget = target;
    }

    public View getTarget() {
        return mTarget;
    }


    public int getWidth() {
        return mTarget.getWidth();
    }

    public int getHeight() {
        return mTarget.getHeight();
    }

    public void postInvalidate() {
        mTarget.postInvalidate();
    }

    public abstract List<Animator> createAnimation();

    public abstract void draw(Canvas canvas, Paint paint);
}
