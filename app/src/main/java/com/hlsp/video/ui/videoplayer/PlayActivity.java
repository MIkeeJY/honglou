package com.hlsp.video.ui.videoplayer;

import com.hlsp.video.base.BaseActivity;

/**
 * 单独的视频播放页面
 * Created by shuyu on 2016/11/11.
 */
public abstract class PlayActivity extends BaseActivity {

//    public final static String IMG_TRANSITION = "IMG_TRANSITION";
//    public final static String TRANSITION = "TRANSITION";
//
//    @BindView(R.id.video_player)
//    SampleVideo videoPlayer;
//
//    OrientationUtils orientationUtils;
//
//
//    @Override
//    protected int layoutRes() {
//        return R.layout.activity_play;
//    }
//
//    @Override
//    protected void initView() {
//        init();
//    }
//
//    private void init() {
//        String url = getIntent().getStringExtra("videoUrl");
//
//        videoPlayer.setUp(url, true, "测试视频");
//
//        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        videoPlayer.setThumbImageView(imageView);
//
//        //增加title
//        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
//        //videoPlayer.setShowPauseCover(false);
//
//        //videoPlayer.setSpeed(2f);
//
//        //设置返回键
//        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
//
//        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
//        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                orientationUtils.resolveByClick();
//            }
//        });
//
//        //videoPlayer.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));
//        //videoPlayer.setDialogVolumeProgressBar(getResources().getDrawable(R.drawable.video_new_volume_progress_bg));
//        //videoPlayer.setDialogProgressBar(getResources().getDrawable(R.drawable.video_new_progress));
//        //videoPlayer.setBottomShowProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_seekbar_progress),
//                //getResources().getDrawable(R.drawable.video_new_seekbar_thumb));
//        //videoPlayer.setDialogProgressColor(getResources().getColor(R.color.colorAccent), -11);
//
//        //是否可以滑动调整
//        videoPlayer.setIsTouchWiget(true);
//
//        //设置返回按键功能
//        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
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
//        videoPlayer.onVideoResume();
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
