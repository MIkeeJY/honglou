package com.hlsp.video.model.data;

import android.content.Context;

import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import chuangyuan.ycj.videolibrary.listener.DataSourceListener;

/**
 * 不带缓存的数据源
 * Created by hackest on 2018/3/24.
 */

public class DataHttpSource implements DataSourceListener {

    private Context context;

    public DataHttpSource(Context context) {
        this.context = context;
    }

    @Override
    public com.google.android.exoplayer2.upstream.DataSource.Factory getDataSourceFactory() {
        return new DefaultHttpDataSourceFactory(context.getPackageName(), null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);

    }
}
