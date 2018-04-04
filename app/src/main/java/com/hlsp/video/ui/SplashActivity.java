package com.hlsp.video.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.baidu.mobstat.StatService;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.bean.CdnResponse;
import com.hlsp.video.model.ConstantsValue;
import com.hlsp.video.model.HttpBaseUrl;
import com.hlsp.video.okhttp.http.OkHttpClientManager;
import com.hlsp.video.ui.main.MainTabActivity;
import com.hlsp.video.utils.FileUtils;
import com.hlsp.video.utils.GsonUtil;
import com.hlsp.video.utils.SpUtils;
import com.hlsp.video.utils.ToastUtil;
import com.hlsp.video.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.share.jack.cyghttp.app.HttpServletAddress;
import okhttp3.Request;


/**
 * Created by hackest on 2017/6/14
 */

public class SplashActivity extends Activity {

    Handler handler = new Handler();
    MyThread thread;


    String cdnUrl1 = "https://public.dingyueads.com/dyxsp/com.hlsp.video.json";
    String cdnUrl2 = "https://public.qingoo.cn/dyxsp/com.hlsp.video.json";
    String cdnUrl3 = "https://public.lsread.cn/dyxsp/com.hlsp.video.json";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();

    }

    protected void initView() {
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowUtil.getScreenWidth(App.getInstance()) * 4 / 3);
//        ad_view.setLayoutParams(params);

        thread = new MyThread();
        handler.postDelayed(thread, 1200);

        /**
         * 获取外网Ip
         */
        if (Utils.isNetworkAvailable(App.getInstance())) {
            Map<String, String> map = new HashMap<>();
            OkHttpClientManager.postAsyn("http://pv.sohu.com/cityjson?ie=utf-8", new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    ToastUtil.showToast("网络连接失败");
                }

                @Override
                public void onResponse(String response) {
                    try {
                        String ip = response.substring(response.indexOf(":") + 1, response.indexOf(",")).trim();
                        SpUtils.put(ConstantsValue.REAL_IP, ip);
                    } catch (Exception e) {
                        e.printStackTrace();
                        SpUtils.put(ConstantsValue.REAL_IP, "");
                    }

                }
            }, map, "");

        }


//        PlatformSDK.adapp().dycmSplashAd(SplashActivity.this, "10-1", ad_view, new AbstractCallback() {
//            @Override
//            public void onResult(boolean adswitch, String jsonResult) {
//                LogUtils.e(adswitch);
//                LogUtils.json(jsonResult);
//                if (adswitch) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(jsonResult);
//                        if (jsonObject.has("state_code")) {
//                            switch (ResultCode.parser(jsonObject.getInt("state_code"))) {
//                                case AD_REQ_SUCCESS://广告请求成功
//                                    DyLogUtils.dd("AD_REQ_SUCCESS" + jsonResult);
//                                    break;
//                                case AD_REQ_FAILED://广告请求失败
//                                    DyLogUtils.dd("AD_REQ_FAILED" + jsonResult);
//                                    Intent intent2 = new Intent(SplashActivity.this, MainTabActivity.class);
//                                    startActivity(intent2);
//                                    finish();
//                                    break;
//                                case AD_DISMISSED_CODE://开屏页面关闭
//                                    Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                    break;
//                                case AD_ONCLICKED_CODE://开屏页面点击
//                                    DyLogUtils.dd("AD_ONCLICKED_CODE" + jsonResult);
//                                    break;
//                                case AD_ONTICK_CODE://剩余显示时间
//                                    DyLogUtils.dd("AD_ONTICK_CODE" + jsonResult);
//                                    break;
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    //执行广告开关关闭逻辑
//                    Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//            }
//        });

    }


    private class MyThread implements Runnable {

        @Override
        public void run() {
//            if (UserDao.getInstance().isHaveUser()) {
//                startActivity(new Intent(SplashActivity.this, MainTabActivity.class));
//                finish();
//            } else {
//                startActivity(new Intent(SplashActivity.this, MainTabActivity.class));
//                finish();
//            }
            String path = ConstantsValue.BASE_PATH + "/temp/";

            OkHttpClientManager.downloadAsyn(cdnUrl1, path, new OkHttpClientManager.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    LogUtils.e(e.getMessage());

                }

                @Override
                public void onResponse(String path) {
                    LogUtils.e(path);
                    String cndStr = FileUtils.readFile(path);

                    if (!TextUtils.isEmpty(cndStr)) {
                        CdnResponse response = GsonUtil.GsonToBean(cndStr, CdnResponse.class);
                        LogUtils.e(response);
                        if (!HttpBaseUrl.BASE_URL.equals(response.getBase_url())) {
                            HttpBaseUrl.BASE_URL = response.getBase_url();
                            HttpServletAddress.getInstance().setOnlineAddress(HttpBaseUrl.BASE_URL);
                        }

                    }


                }
            });

            startActivity(new Intent(SplashActivity.this, MainTabActivity.class));
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(thread);

    }


    @Override
    protected void onResume() {
        super.onResume();
        // 页面埋点，需要使用Activity的引用，以便代码能够统计到具体页面名
        StatService.onPageStart(this, getClass().getName());

        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 页面结束埋点，需要使用Activity的引用，以便代码能够统计到具体页面名
        StatService.onPageEnd(this, getClass().getName());

        StatService.onPause(this);
    }

}