package com.hlsp.video.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseActivity;
import com.hlsp.video.bean.data.LevideoData;
import com.hlsp.video.model.data.Data2Source;
import com.hlsp.video.ui.fragment.VerticalVideoItemFragment;
import com.hlsp.video.utils.GlideUtils;
import com.hlsp.video.view.VerticalViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import chuangyuan.ycj.videolibrary.listener.LoadModelType;
import chuangyuan.ycj.videolibrary.listener.VideoInfoListener;
import chuangyuan.ycj.videolibrary.video.ManualPlayer;
import chuangyuan.ycj.videolibrary.whole.WholeMediaSource;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;


/**
 * Created by hackest on 2018/3/5.
 */

public class VerticalVideoActivity extends BaseActivity {

    @BindView(R.id.verticalviewpager) VerticalViewPager mVerticalViewpager;

    private List<LevideoData> mList = new ArrayList<>();

    private int mCurrentItem;

    private FragmentManager mFragmentManager;

    private View mRoomContainer;
    private View mFragmentContainer;

    private VideoPlayerView videoPlayer;
    private ManualPlayer exoPlayerManager;
    WholeMediaSource wholeMediaSource;

    private ImageView mPlay;
    private View mRootView;
    private ImageView mCover;
    private TextView mTvVideoTitle;

    private VerticalVideoItemFragment mItemFragment = VerticalVideoItemFragment.newInstance();

    private boolean mInit = false;

    private int mRoomId = -1;

    private boolean isStop = false;

    private boolean isSelected = false;

    /**
     * 记录当前播放位置
     */
    private long mCurrentPos;

    int position;


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

        mFragmentManager = getSupportFragmentManager();

        mCurrentItem = position;

        mRoomContainer = LayoutInflater.from(this).inflate(R.layout.view_video_container, null);
        mFragmentContainer = mRoomContainer.findViewById(R.id.fragment_container);
        mPlay = mRoomContainer.findViewById(R.id.iv_play);
        videoPlayer = mRoomContainer.findViewById(R.id.video_player);
        mRootView = mRoomContainer.findViewById(R.id.view_play);
        mCover = mRoomContainer.findViewById(R.id.cover_img);
        mTvVideoTitle = mRoomContainer.findViewById(R.id.tv_video_title);


        wholeMediaSource = new WholeMediaSource(this, new Data2Source(getApplication(), new CacheDataSource.EventListener() {
            @Override
            public void onCachedBytesRead(long cacheSizeBytes, long cachedBytesRead) {

            }
        }));
        exoPlayerManager = new ManualPlayer(this, wholeMediaSource, videoPlayer);

    }

    private void loadVideo(ViewGroup viewGroup, int mCurrentItem) {
        LevideoData data = mList.get(mCurrentItem);

        //聊天室的fragment只加载一次，以后复用
        if (!mInit) {
            mFragmentManager.beginTransaction().add(mFragmentContainer.getId(), mItemFragment).commitAllowingStateLoss();
            mInit = true;
        }
        mItemFragment.setmData(data);
        mItemFragment.initdata();


        isSelected = false;
        isStop = false;
        mPlay.setVisibility(View.GONE);
        mCover.setVisibility(View.VISIBLE);

        GlideUtils.loadImage(App.getInstance(), mList.get(mCurrentItem).getCoverImgUrl(), mCover, null, R.color.black, R.color.black);
        mTvVideoTitle.setText(data.getTitle());

        LogUtils.e(data.getVideoPlayUrl());

        exoPlayerManager.setLoadModel(LoadModelType.PERCENR);
        wholeMediaSource.setMediaUri(Uri.parse(data.getVideoPlayUrl()));
        exoPlayerManager.setLooping(Integer.MAX_VALUE);
        exoPlayerManager.startPlayer();//开始播放


        exoPlayerManager.setVideoInfoListener(new VideoInfoListener() {
            @Override
            public void onPlayStart() {

                //开始播放
                mCover.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCover.setVisibility(View.GONE);
                    }
                }, 200);
            }

            @Override
            public void onLoadingChanged() {
                //加载变化
            }

            @Override
            public void onPlayerError(ExoPlaybackException e) {
                //加载错误
            }

            @Override
            public void onPlayEnd() {
                //播放结束

            }

            @Override
            public void isPlaying(boolean playWhenReady) {

            }

        });


        viewGroup.addView(mRoomContainer);

        mRoomId = mCurrentItem;
    }


    class MyPageAdapter extends PagerAdapter {
        private List<LevideoData> mList = new ArrayList<>();

        public MyPageAdapter(List<LevideoData> mList) {
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Log.e("***********", "instantiateItem");
            View view = View.inflate(VerticalVideoActivity.this, R.layout.view_video_item, null);
            ImageView imageView = view.findViewById(R.id.cover_img);
            GlideUtils.loadImage(App.getInstance(), mList.get(position).getCoverImgUrl(), imageView, null);

            view.setId(position);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isStop) {
            mCurrentPos = exoPlayerManager.getCurrentPosition();
            exoPlayerManager.setStartOrPause(false);
        } else {
            exoPlayerManager.setStartOrPause(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (exoPlayerManager != null) {
            if (isStop) {
                exoPlayerManager.seekTo(mCurrentPos);
                exoPlayerManager.setStartOrPause(true);
                isSelected = false;
                mPlay.setVisibility(View.GONE);
                isStop = false;
            } else {
                exoPlayerManager.onResume();
                exoPlayerManager.setStartOrPause(true);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayerManager.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getImageData(List<LevideoData> mList) {
        this.mList = mList;

        MyPageAdapter myAdapter = new MyPageAdapter(mList);
        mVerticalViewpager.setAdapter(myAdapter);

        if (position != -1) {
            mVerticalViewpager.setCurrentItem(position);
        }

        mVerticalViewpager.setOffscreenPageLimit(3);

        mVerticalViewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
            }

        });


        mVerticalViewpager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                ViewGroup viewGroup = (ViewGroup) page;
//                Log.e(TAG, "page.id == " + page.getId() + ", position == " + position);

                if ((position < 0 && viewGroup.getId() != mCurrentItem)) {
                    View roomContainer = viewGroup.findViewById(R.id.room_container);
                    if (roomContainer != null && roomContainer.getParent() != null && roomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (roomContainer.getParent())).removeView(roomContainer);
                    }
                }
                // 满足此种条件，表明需要加载直播视频，以及聊天室了
                if (viewGroup.getId() == mCurrentItem && position == 0 && mCurrentItem != mRoomId) {
                    if (mRoomContainer.getParent() != null && mRoomContainer.getParent() instanceof ViewGroup) {
                        ((ViewGroup) (mRoomContainer.getParent())).removeView(mRoomContainer);
                    }
                    loadVideo(viewGroup, mCurrentItem);
                }
            }
        });


        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    exoPlayerManager.setStartOrPause(true);
                    isStop = false;

                } else {
                    exoPlayerManager.setStartOrPause(false);
                    isStop = true;
                }


                if (exoPlayerManager.isPlaying()) {
                    mPlay.setVisibility(View.GONE);
                } else {
                    mPlay.setVisibility(View.VISIBLE);
                    mPlay.setSelected(false);
                }

                isSelected = !isSelected;
            }
        });


    }
}