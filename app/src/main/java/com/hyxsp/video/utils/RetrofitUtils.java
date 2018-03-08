package com.hyxsp.video.utils;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jim on 2018/1/25.
 */

public class RetrofitUtils {


    private static Retrofit retrofit;
    private static ApiService apiService;

    private static Retrofit getRetrofit(String baseUrl) {
        if (retrofit == null) {
            synchronized (RetrofitUtils.class) {
                if (retrofit == null) {
//                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(
                            new HttpLoggingInterceptor.Logger() {
                                @Override
                                public void log(String message) {
                                    if (TextUtils.isEmpty(message)) return;
                                    String s = message.substring(0, 1);
                                    //如果收到想响应是json才打印
                                    if ("{".equals(s) || "[".equals(s)) {
                                        LogUtils.json(message);
                                    } else if (message.contains("http://")) {
                                        LogUtils.d(message);
                                    } else if (message.contains("Exception")) {
                                        LogUtils.d(message);
                                    }

                                }
                            });

                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .addInterceptor(httpLoggingInterceptor)
                            .addInterceptor(new MyInterceptor())
                            .build();


                    retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * 对外方法
     */
    public static ApiService apiService(String baseUrl) {
        if (apiService == null) {
            synchronized (ApiService.class) {
                if (apiService == null) {
                    apiService = getRetrofit(baseUrl).create(ApiService.class);
                }
            }
        }
        return apiService;
    }

    /**
     * 拦截器
     */
    private static class MyInterceptor implements Interceptor {

        /**
         * @param chain
         * @return
         * @throws IOException
         */
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            HttpUrl url = request
                    .url()
                    .newBuilder()
                    .addQueryParameter("source", "android")
                    .build();
            Request build = request
                    .newBuilder()
                    .url(url)
                    .build();

            return chain.proceed(build);
        }
    }

}
