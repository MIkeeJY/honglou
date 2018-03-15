package com.hlsp.video.bean.data;

import android.text.TextUtils;

import com.hlsp.video.utils.Utils;

import org.json.JSONObject;


public class DouyinVideoData extends LevideoData {

    public static DouyinVideoData fromJSONData(String jsonStr) {
        DouyinVideoData data = new DouyinVideoData();
        if (TextUtils.isEmpty(jsonStr)) {
            return data;
        }
        try {
            JSONObject json = new JSONObject(jsonStr);

            //video标签中的信息
            /**
             * video
             * 	-play_addr
             *  	-url_list
             *  -download_addr
             *  	-url_list
             *  -origin_cover
             *  	-url_list
             *  -cover
             *  	-width
             *  	-height
             *
             * author
             * 	-avatar_thumb
             * 		-url_list
             * 	-nickname
             *
             * statistics
             * 	-play_count
             *
             * music
             * 	-cover_thumb
             * 		-url_list
             * 	-title
             * 	-author
             *
             * desc
             *
             * create_time
             */

            //视频内容信息
            JSONObject videoJson = json.getJSONObject("video");
            JSONObject playAddJson = videoJson.getJSONObject("play_addr");
            data.videoPlayUrl = playAddJson.getJSONArray("url_list").getString(0);
            JSONObject downloadJson = videoJson.getJSONObject("download_addr");
            data.videoDownloadUrl = downloadJson.getJSONArray("url_list").getString(0);
            JSONObject coverJson = videoJson.getJSONObject("origin_cover");
            data.coverImgUrl = coverJson.getJSONArray("url_list").getString(0);

            JSONObject dynamicCoverJson = videoJson.getJSONObject("dynamic_cover");
            data.dynamicCover = dynamicCoverJson.getJSONArray("url_list").getString(0);

            JSONObject sizeJson = videoJson.getJSONObject("cover");
            data.videoWidth = sizeJson.optInt("width");
            data.videoHeight = sizeJson.optInt("height");
            data.title = json.optString("desc");
            data.createTime = json.optLong("create_time") * 1000;

            data.playCount = json.getJSONObject("statistics").optInt("play_count");
            data.likeCount = json.getJSONObject("statistics").optInt("digg_count");

            //视频作者信息
            JSONObject authorJson = json.getJSONObject("author");
            JSONObject thumbJson = authorJson.getJSONObject("avatar_thumb");
            data.authorImgUrl = thumbJson.getJSONArray("url_list").getString(0);
            data.authorName = authorJson.optString("nickname");

            //视频音乐信息
            JSONObject musicJson = json.getJSONObject("music");
            JSONObject musicThumbJson = musicJson.getJSONObject("cover_thumb");
            data.musicImgUrl = musicThumbJson.getJSONArray("url_list").getString(0);
            data.musicName = musicJson.optString("title");
            data.musicAuthorName = musicJson.optString("author");

            data.formatTimeStr = Utils.formatTimeStr(data.createTime);
            data.formatPlayCountStr = Utils.formatNumber(data.playCount);

            if (!TextUtils.isEmpty(data.musicAuthorName) && !TextUtils.isEmpty(data.musicName)) {
                if (data.musicName.contains("@")) {
                    data.filterMusicNameStr = data.musicAuthorName + data.musicName;
                } else {
                    data.filterMusicNameStr = data.musicAuthorName + "@" + data.musicName;
                }
            } else if (!TextUtils.isEmpty(data.musicAuthorName)) {
                data.filterMusicNameStr = data.musicAuthorName;
            } else if (!TextUtils.isEmpty(data.musicName)) {
                data.filterMusicNameStr = data.musicName;
            }

            if (!TextUtils.isEmpty(data.title)) {
                if (data.title.length() > 30) {
                    data.filterTitleStr = Utils.filterStrBlank(data.title.substring(0, 30) + "...");
                } else {
                    data.filterTitleStr = Utils.filterStrBlank(data.title);
                }
            }
            if (!TextUtils.isEmpty(data.authorName)) {
                if (data.authorName.length() > 7) {
                    data.filterUserNameStr = Utils.filterStrBlank(data.authorName.substring(0, 7) + "...");
                } else {
                    data.filterUserNameStr = Utils.filterStrBlank(data.authorName);
                }
            }
        } catch (Exception e) {
        }

        return data;
    }

    @Override
    public String toString() {
        return "videotitle=" + title + ",videoplayurl=" + videoPlayUrl + ",videodownloadurl=" + videoDownloadUrl + ",width=" + videoWidth + ",height=" + videoHeight
                + ",coverimgurl=" + coverImgUrl + ",musicname=" + musicName + ",musicimgurl=" + musicImgUrl + ",musicauthorname=" + musicAuthorName + ",authorname="
                + authorName + ",authorimgurl=" + authorImgUrl + ",playcount=" + playCount;
    }

}
