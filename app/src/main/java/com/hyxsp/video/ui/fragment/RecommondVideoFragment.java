package com.hyxsp.video.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.hyxsp.video.R;
import com.hyxsp.video.base.BaseFragment;
import com.lightsky.video.VideoHelper;
import com.lightsky.video.VideoSetting;
import com.lightsky.video.datamanager.category.CategoryQueryNotify;
import com.lightsky.video.sdk.CategoryInfoBase;
import com.lightsky.video.sdk.VideoOption;
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

    VideoTabFragement mVideoFragment;


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

    }


    private void InitSdk() {
        VideoSetting setting = new VideoSetting();
        setting.Debugmodel = false;
        setting.UseNbPlayer = true;
        setting.UseFileLog = false;
        setting.UseLogCatLog = false;
        setting.UseShareLayout = false;
        VideoOption option = new VideoOption();

        InitVideoHelper(setting, option);

    }


    private void InitVideoHelper(VideoSetting setting, VideoOption opt) {
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
        mTabs.clear();
        mTabinfos.clear();
        for (CategoryInfoBase item : infos) {
            mTabs.put(item.name, item.mId);
            mTabinfos.add(item);
        }


        mTabs.remove("推荐");
        mTabs.remove("搞笑");

        for (String key : mTabs.keySet()) {
            int value = mTabs.get(key);
            tabfilter.add(value);
        }


        LogUtils.e(tabfilter);

        VideoHelper.get().SetVideoTabFilter(tabfilter);

        mVideoFragment = new VideoTabFragement();
        if (isAdded()) {
            showVideoFragment(mVideoFragment);
        }
    }


    private void showVideoFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_video_frame_layout, fragment);
        ft.commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        VideoHelper.get().unInit();
    }
}
