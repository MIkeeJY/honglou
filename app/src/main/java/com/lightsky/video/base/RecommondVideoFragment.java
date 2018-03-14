package com.lightsky.video.base;

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
import com.hyxsp.video.R;
import com.hyxsp.video.base.BaseFragment;
import com.hyxsp.video.bean.EventEntity;
import com.hyxsp.video.ui.main.MainTabActivity;
import com.lightsky.video.VideoHelper;
import com.lightsky.video.datamanager.category.CategoryQueryNotify;
import com.lightsky.video.sdk.CategoryInfoBase;
import com.lightsky.video.sdk.VideoOption;
import com.lightsky.video.sdk.VideoSwitcher;
import com.lightsky.video.sdk.VideoTabFragement;
import com.lightsky.video.sdk.VideoTypesLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * Created by hackest on 2018-02-01.
 */

public class RecommondVideoFragment extends BaseFragment implements CategoryQueryNotify {

    private VideoTypesLoader mTabLoader;
    private Map<String, Integer> mTabs = new HashMap<>();
    private List<CategoryInfoBase> mTabinfos = new ArrayList<>();

    List<Integer> tabfilter = new ArrayList<>();

    private VideoTabFragement mVideoFragment;
    private Handler mHandler = new Handler();
    ImageView mSearch;

    private boolean isFirstPage = true;
    private boolean isDragPage = false;

    List<EventEntity> mEventList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        StatService.onEvent(getActivity(), "funny", "搞笑");

    }

    private void generateEvent() {
        EventEntity entity1 = new EventEntity("funny", "搞笑");
        EventEntity entity2 = new EventEntity("funny", "搞笑");
        EventEntity entity3 = new EventEntity("music", "音乐");
        EventEntity entity4 = new EventEntity("film", "影视");
        EventEntity entity5 = new EventEntity("life", "生活");
        EventEntity entity6 = new EventEntity("military", "军事");
        EventEntity entity7 = new EventEntity("entertainment", "娱乐");
        EventEntity entity8 = new EventEntity("technology", "科技");
        EventEntity entity9 = new EventEntity("game", "游戏");
        EventEntity entity10 = new EventEntity("sports", "体育");

        mEventList.add(entity1);
        mEventList.add(entity2);
        mEventList.add(entity3);
        mEventList.add(entity4);
        mEventList.add(entity5);
        mEventList.add(entity6);
        mEventList.add(entity7);
        mEventList.add(entity8);
        mEventList.add(entity9);
        mEventList.add(entity10);

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
        LogUtils.e(infos);


        mTabs.clear();
        mTabinfos.clear();
        for (CategoryInfoBase item : infos) {
            mTabs.put(item.name, item.mId);
            mTabinfos.add(item);
        }

        mTabs.remove("推荐");
        mTabs.remove("社会");
        if (mTabs.containsKey("街舞")) {
            mTabs.remove("街舞");
        }
        if (mTabs.containsKey("正能量")) {
            mTabs.remove("正能量");
        }

        for (String key : mTabs.keySet()) {
            int value = mTabs.get(key);
            tabfilter.add(value);
        }


        LogUtils.e(tabfilter);

        VideoHelper.get().SetVideoTabFilter(tabfilter);

        mVideoFragment = new VideoTabFragement();
        if (isAdded()) {
            showVideoFragment(mVideoFragment);
            generateEvent();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onStatistics();

                }
            });

        }
    }


    private void showVideoFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_video_frame_layout, fragment);
        ft.commit();
    }

    /**
     * 打点相关造的方法
     */
    private void onStatistics() {
        mVideoFragment.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (isFirstPage && isDragPage && positionOffsetPixels == 0) {   //当前页是第一页，并且是拖动状态，并且像素偏移量为0
                    ((MainTabActivity) getActivity()).getTabHost().setCurrentTab(1);
                }

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    StatService.onEvent(getActivity(), mEventList.get(position).getEventId(), mEventList.get(position).getEventName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isFirstPage = position == 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isDragPage = state == 1;
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
    }
}
