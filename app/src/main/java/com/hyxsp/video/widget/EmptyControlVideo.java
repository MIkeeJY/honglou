package com.hyxsp.video.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.hyxsp.video.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


/**
 * 无任何控制ui的播放
 * Created by guoshuyu on 2017/8/6.
 */

public class EmptyControlVideo extends StandardGSYVideoPlayer {

    public EmptyControlVideo(Context context) {
        super(context);
    }

    public EmptyControlVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public int getLayoutId() {
        return R.layout.empty_control_video;
    }

}
