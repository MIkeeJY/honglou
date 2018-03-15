package com.hlsp.video.utils;


import com.hlsp.video.bean.CategoryBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jim on 2018/1/25.
 */

/**
 *  RetrofitUtils.apiService("http://lf.snssdk.com/").test()
         .observeOn(Schedulers.io())
         .subscribeOn(Schedulers.newThread())
         .subscribeWith(new DisposableSubscriber<Test>() {
                @Override
                public void onNext(Test test) {
                Log.d("MainActivity", "test.getData().size():" + test.getData().size());
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onComplete() {

                }
        });
 *
 */
public interface ApiService {
    //http://lf.snssdk.com/neihan/service/tabs/
//    @GET("neihan/service/tabs/")
//    Flowable<Test> test();


    //http://api.amemv.com/aweme/v1/find/?retry_type=no_retry&iid=23028350734&device_id=42386607829&aid=1128
//    @GET
//    Call<XbannerBean> getBanner(@Url String url);

    //http://api.amemv.com/aweme/v1/category/list/?cursor=0&count=10&retry_type=no_retry&iid=23028350734&device_id=42386607829&ac=wifi&channel=xiaomi&aid=1128&app_name=aweme&version_code=169&version_name=1.6.9&device_platform=android&ssmix=a&device_type=Redmi+Note+4&device_brand=Xiaomi&language=zh&os_api=23&os_version=6.0&uuid=863411038560129&openudid=87f33cdff2475c29&manifest_version_code=169&resolution=1080*1920&dpi=480&update_version_code=1692&_rticket=1515741876521&ts=1515741878&as=a195564586db1ac208&cp=61b2af5864845224e1
    @GET("aweme/v1/category/list/")
    Call<CategoryBean> getCategory(@Query("cursor") int cursor,
                                   @Query("count") int count
    );

    //评论
    //https://iu.snssdk.com/neihan/comments/?group_id=75630370024&item_id=75630370024&count=20&offset=0&enable_image_comment=1&iid=22991324588&device_id=42638038240&ac=wifi&channel=app_download&aid=7&app_name=joke_essay&version_code=672&version_name=6.7.2&device_platform=android&ssmix=a&device_type=PRA-AL00XC00B160&device_brand=PRA-AL00X&language=zh&os_api=19&os_version=4.4.4&uuid=863419663747853&openudid=1010046353365109&manifest_version_code=672&resolution=576*1024&dpi=191&update_version_code=6723&_rticket=1516277254616&ts=1516277254&as=a2c5c8d606808a1ec0&cp=8301a95b680a6ee3e2
//    @GET
//    Call<CommentBean> getCommentBean(@Url String url);




}
