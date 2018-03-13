package com.hyxsp.video.ui.main;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.hyxsp.video.App;
import com.hyxsp.video.R;
import com.hyxsp.video.base.BaseActivity;
import com.hyxsp.video.bean.data.LevideoData;
import com.hyxsp.video.ui.fragment.VerticalVideoItemFragment;
import com.hyxsp.video.utils.GlideUtils;
import com.hyxsp.video.view.VerticalViewPager;
import com.hyxsp.video.widget.EmptyControlVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


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

    private EmptyControlVideo videoPlayer;
    private ImageView mPlay;
    private View mRootView;
    private ImageView mCover;

    private VerticalVideoItemFragment mItemFragment = VerticalVideoItemFragment.newInstance();

    private boolean mInit = false;

    private int mRoomId = -1;

    private boolean isStop = false;

    private boolean isSelected = false;

    /**
     * 记录当前播放位置
     */
    private int mCurrentPos;


    @OnClick(R.id.iv_back)
    void back() {
        onBackPressed();
    }


    @Override
    protected int layoutRes() {
        return R.layout.activity_vertical_video;
    }

    @Override
    protected void initView() {

        mList = getIntent().getParcelableArrayListExtra("videoUrlList");
        int position = getIntent().getIntExtra("position", -1);

        mFragmentManager = getSupportFragmentManager();

        mCurrentItem = position;

        final MyPageAdapter myAdapter = new MyPageAdapter(mList);
        mVerticalViewpager.setAdapter(myAdapter);

        if (position != -1) {
            mVerticalViewpager.setCurrentItem(position);
        }


        mVerticalViewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
            }

        });


        mRoomContainer = LayoutInflater.from(this).inflate(R.layout.view_video_container, null);
        mFragmentContainer = mRoomContainer.findViewById(R.id.fragment_container);
        mPlay = mRoomContainer.findViewById(R.id.iv_play);
        videoPlayer = mRoomContainer.findViewById(R.id.video_player);
        mRootView = mRoomContainer.findViewById(R.id.view_play);
        mCover = mRoomContainer.findViewById(R.id.cover_img);


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
                    isStop = false;
                    videoPlayer.onClick(videoPlayer.getStartButton());
                    mPlay.setVisibility(View.GONE);

                } else {
                    isStop = true;
                    videoPlayer.onClick(videoPlayer.getStartButton());
                    mPlay.setVisibility(View.VISIBLE);
                    mPlay.setSelected(false);

                }

                isSelected = !isSelected;
            }
        });


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

        GlideUtils.loadImage(App.getInstance(), mList.get(mCurrentItem).getCoverImgUrl(), mCover, null);


        videoPlayer.setUp(mList.get(mCurrentItem).getVideoPlayUrl(), true, "");
        videoPlayer.setLooping(true);

        /**
         * 某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，视频压缩过于厉害，seek只支持关键帧，
         * 出现这个情况就是原始的视频文件中i 帧比较少，
         * 可开启以下来解决：
         */
        VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);

        videoPlayer.startPlayLogic();

        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {

            @Override
            public void onPrepared(String url, Object... objects) {
                LogUtils.e(url);

                mCover.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCover.setVisibility(View.GONE);
                    }
                }, 200);

            }

            @Override
            public void onPlayError(String url, Object... objects) {
                try {
                    LogUtils.e(url, objects[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                mCover.setVisibility(View.GONE);

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
        videoPlayer.onVideoPause();
        mCurrentPos = videoPlayer.getCurrentPositionWhenPlaying();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isStop) {
            videoPlayer.onVideoResume();
        } else {
            videoPlayer.seekTo(mCurrentPos);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayer.release();
        videoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
    }


}
