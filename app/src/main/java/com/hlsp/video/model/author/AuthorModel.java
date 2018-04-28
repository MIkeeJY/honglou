package com.hlsp.video.model.author;


import com.hlsp.video.base.BaseModel;
import com.hlsp.video.bean.AuthorInfoResponse;
import com.hlsp.video.bean.AuthorVideoResponse;
import com.hlsp.video.utils.GsonUtil;

import cn.share.jack.cyghttp.HttpFunction;
import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.RequestBody;

/**
 * Created by hackest on 2018-04-26
 */

public class AuthorModel extends BaseModel {

    public static AuthorModel getInstance() {
        return getPresent(AuthorModel.class);
    }


    public void executeAuthorInfo(String authorId, Observer<AuthorInfoResponse> observer) {
        addParams("authorId", authorId);
        RequestBody requestBody = RequestBody.create(JSON, GsonUtil.mapToJson(mParams));
        Observable observable = mServletApi.getAuthorInfo(requestBody).map(new HttpFunction());
        toSubscribe(observable, observer);
    }

    public void executeAuthorVideoList(String authorId, String page, String size, Observer<AuthorVideoResponse> observer) {
        addParams("authorId", authorId);
        addParams("page", page);
        addParams("size", size);
        RequestBody requestBody = RequestBody.create(JSON, GsonUtil.mapToJson(mParams));
        Observable observable = mServletApi.getAuthorVideo(requestBody).map(new HttpFunction());
        toSubscribe(observable, observer);
    }


}
