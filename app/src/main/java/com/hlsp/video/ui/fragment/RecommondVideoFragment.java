package com.hlsp.video.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dueeeke.videoplayer.player.VideoViewManager;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;
import com.hlsp.video.base.BaseLoadFragment;
import com.hlsp.video.bean.ChannelListItem;
import com.hlsp.video.utils.NoDoubleClickUtils;
import com.hlsp.video.view.LoadFrameLayout;
import com.hlsp.video.widget.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 新版推荐Fragment
 * Created by hackest on 2018-02-01.
 */

public class RecommondVideoFragment extends BaseFragment {

    @BindView(R.id.stl_home) SlidingTabLayout mTabLayout;
    @BindView(R.id.tab_search) ImageView mTabSearch;
    @BindView(R.id.vp_home) ViewPager mViewPager;

    @BindView(R.id.load_frameLayout) LoadFrameLayout loadFrameLayout;

    private String[] titles = {"推荐", "搞笑", "娱乐"};

    private List<ChannelListItem> channelList = new ArrayList<>();

    private Map<String, Integer> mTabs = new HashMap<>();

    Handler mHandler = new Handler();

    TextView mRetry;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_recommond_video_new;
    }


    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);

        mRetry = loadFrameLayout.findViewById(R.id.tv_retry);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NoDoubleClickUtils.isDoubleClick()) {
                }
            }
        });


        mViewPager.setAdapter(new ItemPageAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setViewPager(mViewPager); //初始化的绑定


    }





    class ItemPageAdapter extends FragmentStatePagerAdapter {
        SparseArray<BaseLoadFragment> mFragments = new SparseArray<>();

        public ItemPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt("position", position);
            args.putString("title", titles[position]);
            mFragments.put(position, RecommondChildFragment.newInstance(args));

            return mFragments.get(position);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            VideoViewManager.instance().releaseVideoPlayer();
        }
    }

}
