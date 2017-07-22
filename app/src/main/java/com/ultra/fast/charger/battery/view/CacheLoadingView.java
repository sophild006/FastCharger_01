package com.ultra.fast.charger.battery.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ultra.fast.charger.battery.R;
import com.ultra.fast.charger.battery.util.UiUtils;


public class CacheLoadingView extends ImageView {

    private Rect mSrcRect, mDestRect;
    private Bitmap loadingBitmap = null;
    private int rotate = 0;
    private Paint paint;

    private boolean finishLoading = false;

    private int loadingBitmapWidth = 0;
    private int loadingBitmapHeight = 0;
    private int lightBitmapWidth = 0;
    private int lightBitmapHeight = 0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    setFinishLoading(true);
                    break;
            }
        }
    };

    public CacheLoadingView(Context context) {
        this(context, null);
    }

    public CacheLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CacheLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate_img_g_34);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDestRect = new Rect();
        mSrcRect = new Rect();
        loadingBitmapWidth = UiUtils.dip2px(22);
        loadingBitmapHeight = UiUtils.dip2px(22);

        lightBitmapWidth = UiUtils.dip2px(5);
        lightBitmapHeight = UiUtils.dip2px(7);

    }




    public void setFinishLoading(boolean finishLoading) {
        this.finishLoading = finishLoading;
    }

    public boolean getFinishLoading() {
        return finishLoading;
    }

    public void recycle() {
        if (loadingBitmap != null && !loadingBitmap.isRecycled()) {
            loadingBitmap.recycle();
            loadingBitmap = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (finishLoading) {
            super.onDraw(canvas);
            return;
        }
        rotate -= 5;
        if (rotate <= -360) {
            rotate = 0;
        }

        int width = getWidth();
        int height = getHeight();
        canvas.translate(width / 2, height / 2);
        mDestRect.set(-loadingBitmapWidth / 2, -loadingBitmapHeight / 2, loadingBitmapWidth / 2, loadingBitmapHeight / 2);
        canvas.save();
        canvas.rotate(rotate);
        canvas.drawBitmap(loadingBitmap, null, mDestRect, paint);
        canvas.restore();
        canvas.save();

        mSrcRect.set(-lightBitmapWidth / 2, -lightBitmapHeight / 2,
                lightBitmapWidth / 2, -lightBitmapHeight / 2 + ((lightBitmapHeight) * rotate / -360));
        mDestRect.set(-lightBitmapWidth / 2, -lightBitmapHeight / 2, lightBitmapWidth / 2, lightBitmapHeight / 2);
        canvas.clipRect(mSrcRect);
        canvas.restore();
        invalidate();
    }

    private onFinishedListener mListener;
    public void setOnFinishedListener(onFinishedListener listener){
        this.mListener=listener;
    }
    public interface onFinishedListener{
        void onFinishLoaded();
    }

}
