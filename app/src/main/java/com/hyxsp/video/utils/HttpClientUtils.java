package com.hyxsp.video.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Headers.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClientUtils {

    private OkHttpClient client;
    private Handler mDelivery;

    private volatile static HttpClientUtils instance; //声明成 volatile

    private HttpClientUtils() {
        client = new OkHttpClient.Builder()
                //设置超时，不设置可能会报异常
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .writeTimeout(20000L, TimeUnit.MILLISECONDS)
                .build();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static HttpClientUtils getInstance() {
        if (instance == null) {
            synchronized (HttpClientUtils.class) {
                if (instance == null) {
                    instance = new HttpClientUtils();
                }
            }
        }
        return instance;
    }


    public interface ResponseCallback {
        void responseFinish(String result);

        void responseFinish(byte[] result);

        void reponseFail();
    }


    private String url;
    private ResponseCallback mCallback = null;
    private Map<String, String> paramsMap = null;
    private boolean isResponseByte = false;
    private Headers mHeader = null;

    public HttpClientUtils setUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpClientUtils setHeaderParams(Map<String, String> params) {
        Builder builder = new Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        mHeader = builder.build();
        return this;
    }

    public HttpClientUtils setResponseCallback(ResponseCallback callBack) {
        this.mCallback = callBack;
        return this;
    }

    public HttpClientUtils setResponseByte(boolean isResponseByte) {
        this.isResponseByte = isResponseByte;
        return this;
    }

    public HttpClientUtils setParams(Map<String, String> map) {
        this.paramsMap = map;
        return this;
    }

    public void execGet() {
        Request.Builder reqBuilder = new Request.Builder();
        if (mHeader != null) {
            reqBuilder.headers(mHeader);
        }
        Request request = reqBuilder.url(url).build();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (mCallback != null) {
                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.reponseFail();
                            }
                        });

                    }
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (mCallback != null) {
                        mDelivery.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isResponseByte) {
                                    try {
                                        mCallback.responseFinish(response.body().bytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        mCallback.responseFinish(response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                    }
                }
            });
        } catch (Exception e) {
            Log.i("jw", "get data err:" + Log.getStackTraceString(e));
            if (mCallback != null) {
                mCallback.reponseFail();
            }
        }
    }

    public void execPost() {
        if (paramsMap == null || paramsMap.size() == 0) {
            if (mCallback != null) {
                mCallback.reponseFail();
            }
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder = builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();
            Request.Builder reqBuilder = new Request.Builder();
            if (mHeader != null) {
                reqBuilder.headers(mHeader);
            }
            Request request = reqBuilder.url(url).post(formBody).build();
            try {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (mCallback != null) {
                            mCallback.reponseFail();
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (isResponseByte) {
                            mCallback.responseFinish(response.body().bytes());
                        } else {
                            mCallback.responseFinish(response.body().string());
                        }
                    }
                });
            } catch (Exception e) {
                if (mCallback != null) {
                    mCallback.reponseFail();
                }
            }
        }
    }

}
