package com.hyxsp.video.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.hyxsp.video.R;
import com.hyxsp.video.base.BaseLoadFragment;

import butterknife.ButterKnife;

/**
 * Created by hackest on 2018/3/5.
 */

public class VerticalVideoFragment extends BaseLoadFragment {
//    @BindView(R.id.video_player) EmptyControlVideo videoPlayer;
//    @BindView(R.id.view_play) View mRootView;
//    @BindView(R.id.iv_play) ImageView mPlay;

    String url;

    boolean isSelected;

    boolean isStop;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static VerticalVideoFragment newInstance(Bundle args) {
        VerticalVideoFragment fragment = new VerticalVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_play_empty_control;
    }


    @Override
    protected void onViewReallyCreated(View view) {
        ButterKnife.bind(this, view);

        LogUtils.e("onViewReallyCreated");

        isSelected = false;
        isStop = false;


//        videoPlayer.startWindowFullscreen(getActivity(), true, false);
//
//        videoPlayer.setUp(url, true, "");
//        videoPlayer.setLooping(true);
//
////        mRootView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (isSelected) {
////                    isStop = false;
////                    LogUtils.e("播放");
////                    videoPlayer.onClick(videoPlayer.getStartButton());
////                    mPlay.setVisibility(View.GONE);
////
////                } else {
////
////                    isStop = true;
////                    videoPlayer.onClick(videoPlayer.getStartButton());
////                    mPlay.setVisibility(View.VISIBLE);
////                    mPlay.setSelected(false);
////
////                    LogUtils.e("暂停");
////                }
////
////                isSelected = !isSelected;
////            }
////        });
//
//
//        videoPlayer.startPlayLogic();


    }


//    @Override
//    public void onPause() {
//        super.onPause();
//        videoPlayer.onVideoPause();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!isStop) {
//            videoPlayer.onClick(videoPlayer.getStartButton());
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        videoPlayer.release();
//        videoPlayer.setVideoAllCallBack(null);
//        GSYVideoManager.releaseAllVideos();
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        videoPlayer.release();
//        videoPlayer.setVideoAllCallBack(null);
//        GSYVideoManager.releaseAllVideos();
    }

    @Override
    protected void lazyLoad() {
        LogUtils.e("lazyLoad");
        Bundle mBundle = getArguments();
        url = mBundle.getString("videoUrl");

//        initData(url);

    }

    public void initData(String url) {
//        videoPlayer.release();
//        GSYVideoManager.releaseAllVideos();

//        videoPlayer.startWindowFullscreen(getActivity(), true, false);
//
//        videoPlayer.setUp(url, true, "");
//        videoPlayer.setLooping(true);
//        videoPlayer.onClick(videoPlayer.getStartButton());
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


//        videoPlayer.startPlayLogic();
    }

    public void onReleaseData() {
//        videoPlayer.release();
//        videoPlayer.setVideoAllCallBack(null);
//        GSYVideoManager.releaseAllVideos();

    }


}
