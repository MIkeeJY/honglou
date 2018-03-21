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
import android.widget.RelativeLayout;

import com.apkfuns.logutils.LogUtils;
import com.baidu.mobstat.StatService;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;
import com.hlsp.video.utils.DensityUtil;
import com.lightsky.video.VideoHelper;
import com.lightsky.video.datamanager.category.CategoryQueryNotify;
import com.lightsky.video.sdk.CategoryInfoBase;
import com.lightsky.video.sdk.VideoOption;
import com.lightsky.video.sdk.VideoSwitcher;
import com.lightsky.video.sdk.VideoTabFragement;
import com.lightsky.video.sdk.VideoTypesLoader;
import com.lightsky.video.widget.PagerSlidingTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * 推荐Fragment
 * Created by hackest on 2018-02-01.
 */

public class MainRecommendFragment extends BaseFragment implements CategoryQueryNotify {

    private VideoTypesLoader mTabLoader;
    private Map<String, Integer> mTabs = new HashMap<>();
    private List<CategoryInfoBase> mTabinfos = new ArrayList<>();

    private List<Integer> tabfilter = new ArrayList<>();

    private VideoTabFragement mVideoFragment;
    private Handler mHandler = new Handler();

    /**
     * CategoryQueryNotify 接口切换fragment会回调2次所以加一个值判断
     */
    private boolean isInit = false;

    ImageView mSearch;


    PagerSlidingTab mPagerSlidingTab;
    ImageView mShadow;

    int currentPos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabLoader = new VideoTypesLoader();
        mTabLoader.Init(this);
        InitSdk();

    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_main_page;
    }


    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        StatService.onEvent(getActivity(), "recommond", "推荐");


        tabfilter.add(0);
        tabfilter.add(2);
        tabfilter.add(1);

        LogUtils.e(tabfilter);
        VideoHelper.get().SetVideoTabFilter(tabfilter);

        mVideoFragment = new VideoTabFragement();
        if (isAdded()) {
            showVideoFragment(mVideoFragment);

            /**
             * 由于打点需求，强行新建个包加载这里,╮(╯_╰)╭
             */
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPagerSlidingTab = mVideoFragment.mRoot.findViewById(R.id.tabs);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(DensityUtil.dip2px(App.getInstance(), 90), 0, DensityUtil.dip2px(App.getInstance(), 50), 0);
                    mPagerSlidingTab.setLayoutParams(layoutParams);

                    mPagerSlidingTab.setSelectedTextColor(Color.parseColor("#212832"));
                    mPagerSlidingTab.setTextColor(Color.parseColor("#5D646E"));

                    mShadow = mVideoFragment.mRoot.findViewById(R.id.tab_float);
                    mShadow.setVisibility(View.GONE);

                    onStatistics();

                }
            });

        }

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
        LogUtils.e(isInit);

    }


    private void showVideoFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_page_frame_layout, fragment);
        ft.commit();
    }


    /**
     * 打点相关造的方法
     */
    private void onStatistics() {
        mVideoFragment.mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                try {
                    if (position == 0) {
                        StatService.onEvent(getActivity(), "recommond", "推荐");
                    } else if (position == 1) {
                        StatService.onEvent(getActivity(), "social", "搞笑");
                    } else {
                        StatService.onEvent(getActivity(), "entertainment", "娱乐");
                    }
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
        isInit = false;

        VideoHelper.get().unInit();
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
