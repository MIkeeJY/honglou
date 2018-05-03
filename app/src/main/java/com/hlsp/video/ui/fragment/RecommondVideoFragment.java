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

import com.apkfuns.logutils.LogUtils;
import com.baidu.mobstat.StatService;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;
import com.hlsp.video.base.BaseLoadFragment;
import com.hlsp.video.bean.ChannelListItem;
import com.hlsp.video.bean.data.ChannelListData;
import com.hlsp.video.model.main.MainModel;
import com.hlsp.video.utils.NoDoubleClickUtils;
import com.hlsp.video.view.LoadFrameLayout;
import com.hlsp.video.widget.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.share.jack.cyghttp.callback.CygBaseObserver;


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
                    getChannelData();
                }
            }
        });

        getChannelData();

    }

    @OnClick(R.id.tab_search)
    void onSearchClick() {
//        Intent intent = new Intent(getActivity(), SearchActivity.class);
//        getActivity().startActivity(intent);
    }


    private void getChannelData() {
        MainModel.getInstance().executeChannelList(new CygBaseObserver<ChannelListData>(context, "加载中,请稍后....") {
            @Override
            protected void onBaseError(Throwable t) {
                super.onBaseError(t);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadFrameLayout.showErrorView();
                    }
                });
            }

            @Override
            protected void onBaseNext(ChannelListData data) {
                loadFrameLayout.showContentView();

                channelList = data.getChannelList();

                for (ChannelListItem item : channelList) {
                    mTabs.put(item.getName(), item.getId());
                }

                LogUtils.e(mTabs);

                mViewPager.setAdapter(new ItemPageAdapter(getChildFragmentManager()));
                mViewPager.setOffscreenPageLimit(3);
                mTabLayout.setViewPager(mViewPager); //初始化的绑定

                mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        VideoViewManager.instance().releaseVideoPlayer();

                        if (position == 0) {
                            StatService.onEvent(getActivity(), "recommond", "推荐");
                        } else if (position == 1) {
                            StatService.onEvent(getActivity(), "social", "搞笑");
                        } else {
                            StatService.onEvent(getActivity(), "entertainment", "娱乐");
                        }

                    }

                });

            }

        });
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
            args.putInt("id", mTabs.get(titles[position]));
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
