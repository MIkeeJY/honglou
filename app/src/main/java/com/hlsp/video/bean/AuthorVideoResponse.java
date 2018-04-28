package com.hlsp.video.bean;

import java.util.List;

/**
 * Created by hackest on 2018/4/27.
 */

public class AuthorVideoResponse {

    /**
     * hasMore : false
     * List : [{"video_name":"你们看到的是什么？","video_coverURL":"http://p3.pstatp.com/large/6e0e0001f838e80b2285.jpeg","period":"14600","law":"null","video_author_name":"陈立农的小仙女","Bitrate":"100083","video_pubtime":"null","video_like_count":"1151646","video_count_share":"181995","video_show_mode":"1","video_playURL":"http://youqu-video.oss-cn-shenzhen.aliyuncs.com/5acc6e835e14d41440e1b885?Expires=1524829935&OSSAccessKeyId=LTAI0KtApRo67","video_author_id":"douyin_61222759089","video_count_play":"50830057","height_width":"height:1024,width576","video_source":"dingyue","video_count_comment":"65329","ratio":"720p","video_id":"5acc6e835e14d41440e1b885","video_desc":"你们看到的是什么？"}]
     */

    private String hasMore;
    private List<AuthorVideo> List;

    public String getHasMore() {
        return hasMore;
    }

    public java.util.List<AuthorVideo> getList() {
        return List;
    }
}
