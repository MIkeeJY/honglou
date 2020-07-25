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
import com.hlsp.video.bean.EventEntity;
import com.hlsp.video.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * 发现fragment
 * Created by hackest on 2018-02-01.
 */

public class FollowingVideoFragment extends BaseFragment{

    private Map<String, Integer> mTabs = new HashMap<>();

    List<Integer> tabfilter = new ArrayList<>();

    private Handler mHandler = new Handler();
    ImageView mSearch;

    List<EventEntity> mEventList = new ArrayList<>();

    private boolean isInit = false;

    int currentPos;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!NetworkUtil.isNetworkActive(getActivity())) {
            return;
        }
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


    private void showVideoFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_video_frame_layout, fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isInit = false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
