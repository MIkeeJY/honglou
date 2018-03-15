package com.hlsp.video.ui.videoplayer;

import com.hlsp.video.R;
import com.hlsp.video.base.BaseActivity;

/**
 * 单独的视频播放页面
 * Created by shuyu on 2016/11/11.
 */
public class PlayEmptyControlActivity extends BaseActivity {
//    @BindView(R.id.video_player) EmptyControlVideo videoPlayer;
//    @BindView(R.id.view_play) View mRootView;
//    @BindView(R.id.iv_play) ImageView mPlay;

    boolean isSelected = false;

    boolean isStop;

    @Override
    protected int layoutRes() {
        return R.layout.activity_play_empty_control;
    }

    @Override
    protected void initView() {
//        init();
    }

//    private void init() {
//        String url = getIntent().getStringExtra("videoUrl");
//
//        videoPlayer.startWindowFullscreen(this, true, false);
//
//        videoPlayer.setUp(url, true, "");
//        videoPlayer.setLooping(true);
//
//        mRootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isSelected) {
//                    isStop = false;
//                    LogUtils.e("播放");
//                    videoPlayer.onClick(videoPlayer.getStartButton());
//                    mPlay.setVisibility(View.GONE);
//
//                } else {
//
//                    isStop = true;
//                    videoPlayer.onClick(videoPlayer.getStartButton());
//                    mPlay.setVisibility(View.VISIBLE);
//                    mPlay.setSelected(false);
//
//                    LogUtils.e("暂停");
//                }
//
//                isSelected = !isSelected;
//            }
//        });
//
//
//        videoPlayer.startPlayLogic();
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        videoPlayer.onVideoPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (!isStop) {
//            videoPlayer.onClick(videoPlayer.getStartButton());
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        videoPlayer.release();
//        videoPlayer.setVideoAllCallBack(null);
//        GSYVideoManager.releaseAllVideos();
//    }


}
