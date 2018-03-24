package com.hlsp.video.model.data;

import android.content.Context;

import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

import chuangyuan.ycj.videolibrary.listener.DataSourceListener;

/**
 * Created by hackest on 2018/3/24.
 */

public class Data2Source implements DataSourceListener {
    public static final String TAG = "OfficeDataSource";

    private CacheDataSource.EventListener eventListener;

    private Context context;

    public Data2Source(Context context, CacheDataSource.EventListener eventListener) {
        this.context = context;
        this.eventListener = eventListener;
    }

    @Override
    public com.google.android.exoplayer2.upstream.DataSource.Factory getDataSourceFactory() {
        // OkHttpClient okHttpClient = new OkHttpClient();
        // OkHttpDataSourceFactory OkHttpDataSourceFactory=    new OkHttpDataSourceFactory(okHttpClient, Util.getUserAgent(context, context.getApplicationContext().getPackageName()),new DefaultBandwidthMeter() );
        //使用OkHttpClient 数据源工厂
        //   return  OkHttpDataSourceFactory;
        //默认数据源工厂
//        return new DefaultHttpDataSourceFactory(context.getPackageName(), null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
//                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
        //自定义配置
        LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(100000000);
        SimpleCache simpleCache = new SimpleCache
                //设置你缓存目录
                (new File(context.getExternalCacheDir(), "media"),
                        //缓存驱逐器
                        evictor,
                        // 缓存文件加密,那么在使用AES / CBC的文件系统中缓存密钥将被加密  密钥必须是16字节长
                        //可以为空
                        "1234567887654321".getBytes());

        //使用缓存数据源工厂类
        return new CacheDataSourceFactory(simpleCache,
                //设置下载数据加载工厂类
                new DefaultHttpDataSourceFactory(context.getPackageName(), null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                        DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true),
                //设置缓存标记
                0,
                //最大缓存文件大小,不填写 默认2m
                4 * 1024 * 1024);

    }
}
