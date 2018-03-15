package com.hlsp.video.bean.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotsoonVideoListData {

    //这里需要注意这两个字段是进行分页请求功能，大致规则如下：
    /**
     * 第一次请求，这两个字段都是0
     * 第二次请求取第一次请求返回的json数据中的min_cursor字段，max_cursor不需要携带。
     * 第三次以及后面所有的请求都只带max_cursor字段，值为第一次请求返回的json数据中的max_cursor字段。
     */
    public long maxTime;//最大时间戳
    public long minTime;//最小时间戳
    public List<LevideoData> videoDataList = null;

    public static HotsoonVideoListData fromJSONData(String str) {
        HotsoonVideoListData data = new HotsoonVideoListData();
        if (TextUtils.isEmpty(str)) {
            return data;
        }
        try {
            JSONObject json = new JSONObject(str);
            JSONObject extraJson = json.getJSONObject("extra");
            data.maxTime = extraJson.optLong("max_time");
            data.minTime = extraJson.optLong("min_time");
            JSONArray videoAry = json.getJSONArray("data");
            data.videoDataList = new ArrayList<LevideoData>(videoAry.length());
            for (int i = 0; i < videoAry.length(); i++) {
                data.videoDataList.add(HotsoonVideoData.fromJSONData(videoAry.getJSONObject(i).toString()));
            }
        } catch (Exception e) {
        }

        return data;
    }

    @Override
    public String toString() {
        return "maxtime=" + maxTime + ",mintime=" + minTime + ",videolist:" + videoDataList;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getMinTime() {
        return minTime;
    }

    public List<LevideoData> getVideoDataList() {
        return videoDataList;
    }
}
