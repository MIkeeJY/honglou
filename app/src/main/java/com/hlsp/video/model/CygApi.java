package com.hlsp.video.model;

import com.hlsp.video.bean.AuthorInfoResponse;
import com.hlsp.video.bean.AuthorVideoResponse;
import com.hlsp.video.bean.LoginResult;
import com.hlsp.video.bean.data.ChannelListData;
import com.hlsp.video.bean.data.VideoListData;
import com.hlsp.video.bean.data.VideoUrlData;
import com.hlsp.video.model.main.MainInfo;

import java.util.List;
import java.util.Map;

import cn.share.jack.cyghttp.BaseResponse;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jack on 2017/6/13
 */

public interface CygApi {

    @FormUrlEncoded
    @POST("MainServlet")
    Observable<BaseResponse<List<MainInfo>>> getArticle(@FieldMap Map<String, String> params);

    /**
     * 获取频道列表
     */
    @POST("v1/search/channelOf360")
    Observable<BaseResponse<ChannelListData>> getChannel(@Body RequestBody body);

    /**
     * 获取频道的视频列表
     */
    @POST("v1/search/videoListOf360")
    Observable<BaseResponse<VideoListData>> getVideoList(@Body RequestBody body);


    /**
     * 解密视频
     */
    @POST("v1/search/videoUrlOf360")
    Observable<BaseResponse<VideoUrlData>> getVideoUrl(@Body RequestBody body);

    /**
     * 获取作者信息
     */
    @POST("v1/search/getLocalAuthor")
    Observable<BaseResponse<AuthorInfoResponse>> getAuthorInfo(@Body RequestBody body);

    /**
     * 获取作者作品
     */
    @POST("v1/search/getLocalVideo")
    Observable<BaseResponse<AuthorVideoResponse>> getAuthorVideo(@Body RequestBody body);

    /**
     * 邮箱登录
     */
    @POST("v2/login/")
    Observable<BaseResponse<LoginResult>> login(@Body RequestBody body);

    /**
     * 版本更新
     */
    @GET("check/")
    Observable<BaseResponse<LoginResult>> updateVersion(@Query("type") String type);
}