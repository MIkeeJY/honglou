package com.hlsp.video.bean.data;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DouyinVideoListData {

    //这里需要注意这两个字段是进行分页请求功能，大致规则如下：
    /**
     * 第一次请求，这两个字段都是0
     * 第二次请求取第一次请求返回的json数据中的min_cursor字段，max_cursor不需要携带。
     * 第三次以及后面所有的请求都只带max_cursor字段，值为第一次请求返回的json数据中的max_cursor字段。
     */
    public long maxCursor;//最大时间戳
    public long minCursor;//最小时间戳
    public List<LevideoData> videoDataList = null;

    public static DouyinVideoListData fromJSONData(String str) {
        DouyinVideoListData data = new DouyinVideoListData();
        if (TextUtils.isEmpty(str)) {
            return data;
        }
        try {
            JSONObject json = new JSONObject(str);
            data.maxCursor = json.optLong("max_cursor");
            data.minCursor = json.optLong("min_cursor");
            JSONArray videoAry = json.getJSONArray("aweme_list");
            data.videoDataList = new ArrayList<LevideoData>(videoAry.length());
            for (int i = 0; i < videoAry.length(); i++) {
                data.videoDataList.add(DouyinVideoData.fromJSONData(videoAry.getJSONObject(i).toString()));
            }
        } catch (Exception e) {
        }

        return data;
    }

    @Override
    public String toString() {
        return "maxcursor=" + maxCursor + ",mincursor=" + minCursor + ",videolist:" + videoDataList;
    }

    public long getMaxCursor() {
        return maxCursor;
    }

    public long getMinCursor() {
        return minCursor;
    }

    public List<LevideoData> getVideoDataList() {
        return videoDataList;
    }
}
