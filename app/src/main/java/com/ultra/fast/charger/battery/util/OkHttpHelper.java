package com.ultra.fast.charger.battery.util;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by csc on 16/7/4.
 */
public class OkHttpHelper {

    public final static int HTTP_SUCCESS = 1; // 成功
    public final static int HTTP_FAILURE = 0; // 失败

    private static final OkHttpClient client = new OkHttpClient();
    private Call call;
    private Request request;
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private Handler mDelivery;

    static {
        client.newBuilder().connectTimeout(30, TimeUnit.SECONDS);
    }

    private OkHttpHelper() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private static OkHttpHelper instance = new OkHttpHelper();

    public static OkHttpHelper getInstance() {
        return instance;
    }

    /**
     * post 异步方法
     */
    public void postAsync(String url, String body, final IResultCallBack iResultCallBack) {
//        String body = AesHelper.getEncryptBody(JsonHelper.getInstance().toJson(object));
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, body);
        request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noStore().build())
                .addHeader("x-cipher-spec", "1")
                .post(requestBody)
                .url(url)
                .build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.d("onFailure" + e.toString());
                sendFailedStringCallback(iResultCallBack, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String data = AesHelper.getDecryptBody(response.body().string());
                if (response.code() == 200) {
                    String data = response.body().string();
                    L.d("===" + data);
                    sendSuccessResultCallback(iResultCallBack, data);
                } else {
                    sendFailedStringCallback(iResultCallBack, new Exception("network error" + response.code()));
                }
            }
        });
    }

    /**
     * post 同步方法
     */
    public String post(String url, String body) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, body);
        request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noStore().build())
                .addHeader("x-cipher-spec", "1")
                .post(requestBody)
                .url(url)
                .build();
        call = client.newCall(request);
        Response response;
        try {
            response = client.newCall(request).execute();
            if (response != null && response.code() == 200) {
                return response.body().string();
            }
        } catch (IOException e) {
            L.d(e);
            return "";
        }
        return "";
    }

    /**
     * get 异步方法
     */
    public void getAsync(String url, final IResultCallBack iResultCallBack) {
        request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noStore().build())
                .get()
                .url(url)
                .build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(iResultCallBack, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String data = AesHelper.getDecryptBody(response.body().string());
                if (response.code() == 200) {
                    String data = response.body().string();
                    L.d(data);
                    sendSuccessResultCallback(iResultCallBack, data);
                }
            }
        });
    }


    /**
     * get 同步方法
     */
    public String get(String url) {
        request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noStore().build())
                .get()
                .url(url)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            if (response != null && response.code() == 200) {
                return response.body().string();
            }
        } catch (IOException e) {
            L.d(e);
            return "";
        }
        return "";
    }

    private void sendFailedStringCallback(final IResultCallBack iResultCallBack, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (iResultCallBack != null) {
                    iResultCallBack.getResult(HTTP_FAILURE, "request failure");
                }
            }
        });
    }

    private void sendSuccessResultCallback(final IResultCallBack iResultCallBack, final String ret) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                //bugly 9131
                if (iResultCallBack != null && ret != null) {
                    iResultCallBack.getResult(HTTP_SUCCESS, ret);
                }
            }
        });
    }

    public interface IResultCallBack {
        void getResult(int code, String ret);
    }
}
