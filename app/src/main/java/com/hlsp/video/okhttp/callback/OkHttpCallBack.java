package com.hlsp.video.okhttp.callback;

import android.os.Handler;
import android.os.Looper;

import com.apkfuns.logutils.LogUtils;

import java.io.IOException;

import cn.share.jack.cyghttp.util.FRToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求回调
 * Created by Sunshine on 2016/5/12.
 */
public abstract class OkHttpCallBack<T> implements Callback {
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private OkBaseParser<T> mParser;

    public OkHttpCallBack(OkBaseParser<T> mParser) {
        if (mParser == null) {
            throw new IllegalArgumentException("Parser can't be null");
        }
        this.mParser = mParser;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onFailure(call.request(), e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        onResponse(response);
    }

    public void onFailure(Request request, final IOException e) {
        LogUtils.e(e.getMessage());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                FRToast.showToastSafe("网络连接失败");
                onFailure(e);
            }
        });

    }

    public void onResponse(Response response) throws IOException {
        final int code = mParser.getCode();
        try {
            final T t = mParser.parseResponse(response);
            if (response.isSuccessful() && t != null) {
//                final BaseApiResponse baseApiResponse;
//                baseApiResponse = (BaseApiResponse) t;
//                if (baseApiResponse.getCode() == HttpBaseUrl.STATUS_SUCCESS) {
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            onSuccess(code, t);
//                        }
//                    });
//                } else {
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            onError(baseApiResponse.getCode(), baseApiResponse.getMsg());
//                        }
//                    });
//
//                }
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onFailure(new Exception(Integer.toString(code)));
                    }
                });
            }
        } catch (final Exception e) {
            LogUtils.e(e.getMessage());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(e);
                }
            });
        }
    }

    public abstract void onSuccess(int code, T t);

    public abstract void onFailure(Throwable e);

    // 请求数据错误
    public abstract void onError(int code, String message);


    public void onStart() {
    }
}
