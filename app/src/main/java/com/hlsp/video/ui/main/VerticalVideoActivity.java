package com.hlsp.video.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseActivity;
import com.hlsp.video.bean.data.LevideoData;
import com.hlsp.video.ui.main.adapter.DouYinAdapter;
import com.hlsp.video.utils.GlideUtils;
import com.hlsp.video.utils.Utils;
import com.hlsp.video.view.CircleImageView;
import com.hlsp.video.view.TextImageView;
import com.hlsp.video.view.VerticalViewPager;
import com.hlsp.video.widget.DouYinController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by hackest on 2018/3/5.
 */

public class VerticalVideoActivity extends BaseActivity {

    @BindView(R.id.verticalviewpager) VerticalViewPager mVerticalViewpager;

    private List<LevideoData> mList = new ArrayList<>();

    private int mCurrentItem;

    private IjkVideoView mIjkVideoView;
    private DouYinController mDouYinController;
    private DouYinAdapter mDouYinAdapter;
    private List<View> mViews = new ArrayList<>();

    private TextView mTvVideoTitle;

    private CircleImageView mIvUserAvatar;
    private TextView mTvUsername;
    private TextImageView mTvLikeCount;
    private TextImageView mTvPlayCount;

    ImageView mCover;

    private int mPlayingPosition;
    private int position;


    @OnClick(R.id.iv_back)
    void back() {
        onBackPressed();
    }


    @Override
    protected int layoutRes() {
        return R.layout.activity_vertical_video;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
//        mList = getIntent().getParcelableArrayListExtra("videoUrlList");
        position = getIntent().getIntExtra("position", -1);

        mCurrentItem = position;

        mIjkVideoView = new IjkVideoView(this);
        PlayerConfig config = new PlayerConfig.Builder().setLooping().build();
        mIjkVideoView.setPlayerConfig(config);
        mDouYinController = new DouYinController(this);
        mIjkVideoView.setVideoController(mDouYinController);


    }

    private void startPlay() {
        View view = mViews.get(mCurrentItem);
        FrameLayout frameLayout = view.findViewById(R.id.container);
        mCover = view.findViewById(R.id.cover_img);

        mDouYinController.setSelect(false);

        if (mCover.getDrawable() != null) {
            mDouYinController.getThumb().setImageDrawable(mCover.getDrawable());
        }

        ViewGroup parent = (ViewGroup) mIjkVideoView.getParent();

        if (parent != null) {
            parent.removeAllViews();
        }

        frameLayout.addView(mIjkVideoView);
        mIjkVideoView.setUrl(mList.get(mCurrentItem).getVideoPlayUrl());
        mIjkVideoView.setScreenScale(IjkVideoView.SCREEN_SCALE_DEFAULT);
        mIjkVideoView.start();

        mPlayingPosition = mCurrentItem;
    }


    @Override
    protected void onPause() {
        super.onPause();
        mIjkVideoView.pause();
        if (mDouYinController != null) {
            mDouYinController.getIvPlay().setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mIjkVideoView.resume();

        if (mDouYinController != null) {
            mDouYinController.setSelect(false);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIjkVideoView.release();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getImageData(List<LevideoData> mList) {
        this.mList = mList;

        for (LevideoData item : mList) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_video_item, null);
            mCover = view.findViewById(R.id.cover_img);

            mIvUserAvatar = (CircleImageView) view.findViewById(R.id.iv_user_avatar);
            mTvUsername = (TextView) view.findViewById(R.id.tv_username);
            mTvLikeCount = (TextImageView) view.findViewById(R.id.tv_like_count);
            mTvPlayCount = (TextImageView) view.findViewById(R.id.tv_play_count);
            mTvVideoTitle = view.findViewById(R.id.tv_video_title);

            Glide.with(App.getInstance()).load(item.getCoverImgUrl()).dontAnimate().into(mCover);

            GlideUtils.loadImage(App.getInstance(), item.getAuthorImgUrl(), mIvUserAvatar, null);

            mTvVideoTitle.setText(item.getTitle());

            mTvUsername.setText(item.getAuthorName());

            mTvPlayCount.setText(Utils.formatNumber(item.getPlayCount()) + "播放");

            mTvLikeCount.setText(Utils.formatNumber(item.getLikeCount()) + "赞");

            mViews.add(view);
        }

        mDouYinAdapter = new DouYinAdapter(mViews);
        mVerticalViewpager.setAdapter(mDouYinAdapter);


        if (position != -1) {
            mVerticalViewpager.setCurrentItem(position);
        }


        mVerticalViewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
                mIjkVideoView.pause();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPlayingPosition == mCurrentItem) return;
                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mIjkVideoView.release();
                    ViewParent parent = mIjkVideoView.getParent();
                    if (parent != null && parent instanceof FrameLayout) {
                        ((FrameLayout) parent).removeView(mIjkVideoView);
                    }
                    startPlay();
                }

            }

        });


        mVerticalViewpager.post(new Runnable() {
            @Override
            public void run() {
                startPlay();
            }
        });


    }
}