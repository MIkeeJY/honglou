package com.lightsky.video.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * 推荐Fragment
 * Created by hackest on 2018-02-01.
 */

public class MainRecommendFragment extends BaseFragment{

    private Map<String, Integer> mTabs = new HashMap<>();

    private List<Integer> tabfilter = new ArrayList<>();

    private Handler mHandler = new Handler();

    /**
     * CategoryQueryNotify 接口切换fragment会回调2次所以加一个值判断
     */
    private boolean isInit = false;

    ImageView mSearch;

    ImageView mShadow;

    int currentPos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


    }




    private void showVideoFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_page_frame_layout, fragment);
        ft.commit();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
    }


}
