package com.ultra.fast.charger.battery.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * Created by caosc oContext context, n 2017/2/4.
 */

public class UiUtils {

    public static int px2dip(int pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static float sp2px(float sp) {
        final float scale = GlobalContext.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    public static int getScreenWidthInDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dpScreenWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return dpScreenWidth;
    }

    public static int getScreenHeightInDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dpScreenWidth = (int) (displayMetrics.heightPixels / displayMetrics.density);
        return dpScreenWidth;
    }

    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = GlobalContext.getAppContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = GlobalContext.getAppContext().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static float getDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
