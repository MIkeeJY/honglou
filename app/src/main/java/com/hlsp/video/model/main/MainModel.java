package com.hlsp.video.model.main;


import com.hlsp.video.base.BaseModel;
import com.hlsp.video.bean.data.ChannelListData;
import com.hlsp.video.bean.data.VideoListData;
import com.hlsp.video.bean.data.VideoUrlData;
import com.hlsp.video.utils.GsonUtil;

import cn.share.jack.cyghttp.HttpFunction;
import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.RequestBody;

/**
 * Created by jack on 2017/6/14
 */

public class MainModel extends BaseModel {

    public static MainModel getInstance() {
        return getPresent(MainModel.class);
    }


    public void executeChannelList(Observer<ChannelListData> observer) {
        addParams(getCommonMap());
        RequestBody requestBody = RequestBody.create(JSON, GsonUtil.mapToJson(mParams));
        Observable observable = mServletApi.getChannel(requestBody).map(new HttpFunction());
        toSubscribe(observable, observer);
    }


    public void executeVideoList(String videoChannelId, String backdata, String direction, Observer<VideoListData> observer) {
        addParams(getCommonMap());
        addParams("videoChannelId", videoChannelId);
        addParams("backdata", backdata);
        addParams("direction", direction);

        RequestBody requestBody = RequestBody.create(JSON, GsonUtil.mapToJson(mParams));

        Observable observable = mServletApi.getVideoList(requestBody).map(new HttpFunction());
        toSubscribe(observable, observer);
    }

    public void executeVideoUrl(String videoId, String extdata, Observer<VideoUrlData> observer) {
        addParams(getCommonMap());
        addParams("videoId", videoId);
        addParams("extdata", extdata);

        RequestBody requestBody = RequestBody.create(JSON, GsonUtil.mapToJson(mParams));
        Observable observable = mServletApi.getVideoUrl(requestBody).map(new HttpFunction());
        toSubscribe(observable, observer);
    }
}
