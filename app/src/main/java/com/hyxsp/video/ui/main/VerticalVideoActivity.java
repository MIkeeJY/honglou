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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by hackest on 2018/3/5.
 */

public class VerticalVideoActivity extends BaseActivity {

    @BindView(R.id.verticalviewpager) VerticalViewPager mVerticalViewpager;

    List<LevideoData> mList = new ArrayList<>();

    int mCurrentItem;

    private FragmentManager mFragmentManager;

    private View mRoomContainer;
    private View mFragmentContainer;
    EmptyControlVideo videoPlayer;
    ImageView mPlay;
    View mRootView;
    ImageView mCover;

    private VerticalVideoItemFragment mItemFragment = VerticalVideoItemFragment.newInstance();

    private boolean mInit = false;

    private int mRoomId = -1;

    private boolean isStop = false;

    boolean isSelected = false;


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


        mVerticalViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        //聊天室的fragment只加载一次，以后复用
        if (!mInit) {
            mFragmentManager.beginTransaction().add(mFragmentContainer.getId(), mItemFragment).commitAllowingStateLoss();
            mInit = true;
        }


        isSelected = false;
        isStop = false;
        mPlay.setVisibility(View.GONE);
        mCover.setVisibility(View.VISIBLE);

        GlideUtils.loadImage(App.getInstance(), mList.get(mCurrentItem).getCoverImgUrl(), mCover, null);

//        SimpleDraweeView imageView = new SimpleDraweeView(this);
//        imageView.setImageURI(Uri.parse(mList.get(mCurrentItem).getCoverImgUrl()));
//        videoPlayer.setThumbImageView(imageView);

        videoPlayer.setUp(mList.get(mCurrentItem).getVideoPlayUrl(), true, "");
        videoPlayer.setLooping(true);
//        videoPlayer.onClick(videoPlayer.getStartButton());
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
                LogUtils.e(url, objects[0]);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                if (mCover.getVisibility() == View.VISIBLE) {
                    mCover.setVisibility(View.GONE);
                }

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isStop) {
            videoPlayer.onClick(videoPlayer.getStartButton());
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
