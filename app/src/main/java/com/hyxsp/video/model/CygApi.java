package com.hyxsp.video.model;

import com.hyxsp.video.bean.LoginResult;
import com.hyxsp.video.model.main.MainInfo;

import java.util.List;
import java.util.Map;

import cn.share.jack.cyghttp.BaseResponse;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jack on 2017/6/13
 */

public interface CygApi {

    @FormUrlEncoded
    @POST("MainServlet")
    Observable<BaseResponse<List<MainInfo>>> getArticle(@FieldMap Map<String, String> params);


    /**
     * 邮箱登录
     */
    @POST("v2/login/")
    Observable<BaseResponse<LoginResult>> login(@Body RequestBody body);
}