package com.lightsky.video.base;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.baidu.mobstat.StatService;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;
import com.hlsp.video.bean.EventEntity;
import com.hlsp.video.utils.NetworkUtil;
import com.lightsky.video.VideoHelper;
import com.lightsky.video.datamanager.category.CategoryQueryNotify;
import com.lightsky.video.sdk.CategoryInfoBase;
import com.lightsky.video.sdk.VideoOption;
import com.lightsky.video.sdk.VideoSwitcher;
import com.lightsky.video.sdk.VideoTabFragement;
import com.lightsky.video.sdk.VideoTypesLoader;
import com.lightsky.video.sdk.listener.PlayerControler;
import com.lightsky.video.widget.PagerSlidingTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * 发现fragment
 * Created by hackest on 2018-02-01.
 */

public class FollowingVideoFragment extends BaseFragment implements CategoryQueryNotify {

    private VideoTypesLoader mTabLoader;
    private Map<String, Integer> mTabs = new HashMap<>();
    private List<CategoryInfoBase> mTabinfos = new ArrayList<>();

    List<Integer> tabfilter = new ArrayList<>();

    private VideoTabFragement mVideoFragment;
    private Handler mHandler = new Handler();
    ImageView mSearch;

    List<EventEntity> mEventList = new ArrayList<>();

    PagerSlidingTab mPagerSlidingTab;

    private boolean isInit = false;

    private PlayerControler mplayctrl;

    int currentPos;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!NetworkUtil.isNetworkActive(getActivity())) {
            return;
        }
        mTabLoader = new VideoTypesLoader();
        mTabLoader.Init(this);
        InitSdk();
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_recommond_video;
    }


    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        StatService.onEvent(getActivity(), "music", "音乐");

    }

    private void generateEvent() {
//        EventEntity entity1 = new EventEntity("funny", "搞笑");
//        EventEntity entity2 = new EventEntity("funny", "搞笑");
//        EventEntity entity3 = new EventEntity("music", "音乐");
//        EventEntity entity4 = new EventEntity("film", "影视");
//        EventEntity entity5 = new EventEntity("life", "生活");
//        EventEntity entity6 = new EventEntity("military", "军事");
//        EventEntity entity7 = new EventEntity("entertainment", "娱乐");
//        EventEntity entity8 = new EventEntity("technology", "科技");
//        EventEntity entity9 = new EventEntity("game", "游戏");
//        EventEntity entity10 = new EventEntity("sports", "体育");

        EventEntity entity1 = new EventEntity("music", "音乐");
        EventEntity entity2 = new EventEntity("social", "社会");
        EventEntity entity3 = new EventEntity("film", "影视");
        EventEntity entity4 = new EventEntity("life", "生活");
        EventEntity entity5 = new EventEntity("technology", "科技");
        EventEntity entity6 = new EventEntity("game", "游戏");
        EventEntity entity7 = new EventEntity("sports", "体育");
        EventEntity entity8 = new EventEntity("military", "军事");
        EventEntity entity9 = new EventEntity("car", "汽车");

        mEventList.add(entity1);
        mEventList.add(entity2);
        mEventList.add(entity3);
        mEventList.add(entity4);
        mEventList.add(entity5);
        mEventList.add(entity6);
        mEventList.add(entity7);
        mEventList.add(entity8);
        mEventList.add(entity9);

    }


    private void InitSdk() {
        VideoSwitcher setting = new VideoSwitcher();
        setting.Debugmodel = false;
        setting.UseNbPlayer = true;
        setting.UseFileLog = false;
        setting.UseLogCatLog = false;
        setting.UseShareLayout = false;
        VideoOption option = new VideoOption();

        InitVideoHelper(setting, option);

    }


    private void InitVideoHelper(VideoSwitcher setting, VideoOption opt) {
        VideoHelper.get().Init(getActivity(), setting, opt);
        mTabLoader.loadData();
    }

    /**
     * 查询频道列表
     * 娱乐 -> 1
     * 生活 -> 18
     * 汽车 -> 23
     * 音乐 -> 13
     * 体育 -> 12
     * 搞笑 -> 2
     * 科技 -> 7
     * 军事 -> 19
     * 影视 -> 8
     * 社会 -> 3
     * 推荐 -> 0
     */
    @Override
    public void onCategoryQueryFinish(boolean bSuccess, List<CategoryInfoBase> infos) {
        LogUtils.e("onCategoryQueryFinish");
        LogUtils.e(isInit);

        mTabs.clear();
        mTabinfos.clear();
        for (CategoryInfoBase item : infos) {
            mTabs.put(item.name, item.mId);
            mTabinfos.add(item);
        }

        if (isInit) {
            return;
        }

        tabfilter.add(mTabs.get("音乐"));
        tabfilter.add(mTabs.get("社会"));
        tabfilter.add(mTabs.get("影视"));
        tabfilter.add(mTabs.get("生活"));
        tabfilter.add(mTabs.get("科技"));
        tabfilter.add(mTabs.get("游戏"));
        tabfilter.add(mTabs.get("体育"));
        tabfilter.add(mTabs.get("军事"));
        tabfilter.add(mTabs.get("汽车"));

        LogUtils.e(tabfilter);

        VideoHelper.get().SetVideoTabFilter(tabfilter);

        mVideoFragment = new VideoTabFragement();
        if (isAdded()) {
            if (!NetworkUtil.isNetworkActive(getActivity())) {
                return;
            }

            showVideoFragment(mVideoFragment);
            generateEvent();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPagerSlidingTab = mVideoFragment.mRoot.findViewById(R.id.tabs);
                    mPagerSlidingTab.setSelectedTextColor(Color.parseColor("#212832"));
                    mPagerSlidingTab.setTextColor(Color.parseColor("#5D646E"));

                    onStatistics();

                }
            });

        }

        isInit = true;
    }


    private void showVideoFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_video_frame_layout, fragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 打点相关造的方法
     */
    private void onStatistics() {
        mVideoFragment.mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                try {
                    StatService.onEvent(getActivity(), mEventList.get(position).getEventId(), mEventList.get(position).getEventName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        mSearch = mVideoFragment.mRoot.findViewById(R.id.tab_search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatService.onEvent(getActivity(), "search", "搜索");
                mVideoFragment.onClick(v);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        VideoHelper.get().unInit();
        isInit = false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (mVideoFragment != null && mVideoFragment.mViewPager != null) {
                currentPos = mVideoFragment.mViewPager.getCurrentItem();

                if (currentPos == 0) {
                    mVideoFragment.mViewPager.setCurrentItem(1);
                } else {
                    mVideoFragment.mViewPager.setCurrentItem(0);
                }
            }

        } else {
            if (mVideoFragment != null && mVideoFragment.mViewPager != null) {
                mVideoFragment.mViewPager.setCurrentItem(currentPos, false);

            }
        }
    }
}
