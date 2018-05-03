package com.hlsp.video.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.dueeeke.videoplayer.player.VideoCacheManager;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.model.ConstantsValue;
import com.hlsp.video.model.event.ClearListEvent;
import com.hlsp.video.model.event.RefreshHistoryEvent;
import com.hlsp.video.ui.main.FootMarkActivity;
import com.hlsp.video.ui.main.HistoryDetailActivity;
import com.hlsp.video.ui.main.adapter.HistoryVideoAdapter;
import com.hlsp.video.ui.main.adapter.HistoryViewHolder;
import com.hlsp.video.utils.FileUtils;
import com.hlsp.video.utils.SpUtils;
import com.hlsp.video.utils.Utils;
import com.hlsp.video.view.CustomRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.share.jack.cyghttp.util.FRToast;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * 我的页面
 * Created by hackest on 2018-02-01.
 */

public class MineFragment extends BaseFragment implements CygBaseRecyclerAdapter.OnItemClickListener<HistoryViewHolder> {

    @BindView(R.id.ll_footmark) View mFootmark;
    @BindView(R.id.rv_history) CustomRecyclerView mRvHistory;
    @BindView(R.id.ll_my_favorite) View mMyFavorite;
    @BindView(R.id.ll_feedback) View mFeedback;
    @BindView(R.id.ll_push) View mPush;
    @BindView(R.id.ll_version) View mVersion;
    @BindView(R.id.tv_version) TextView mTvVersion;
    @BindView(R.id.tv_username) TextView mTvUser;
    @BindView(R.id.ll_clear_cache) View mClearCache;

    private int userNum;

    private List<VideoListItem> mList = new ArrayList<>();

    HistoryVideoAdapter mAdapter;

    LinearLayoutManager mLayoutManager;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);

        mTvVersion.setText(Utils.getVersion(App.getInstance()));

        if (SpUtils.getInt(ConstantsValue.USER_NAME, -1) == -1) {
            userNum = (int) ((Math.random() * 9 + 1) * 100000);
            SpUtils.put(ConstantsValue.USER_NAME, userNum);
        } else {
            userNum = SpUtils.getInt(ConstantsValue.USER_NAME, -1);
        }

        mTvUser.setText("用户" + userNum);

        mList = FileUtils.readParcelableList(App.getInstance(), ConstantsValue.HISTORY_VIDEO, VideoListItem.class);
        LogUtils.e(mList);

        if (mList != null && mList.size() > 0) {
            mRvHistory.setVisibility(View.VISIBLE);
        } else {
            mRvHistory.setVisibility(View.GONE);
        }

        mAdapter = new HistoryVideoAdapter(getActivity(), this);

        mLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false);
        mLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        mLayoutManager.setReverseLayout(true);//列表翻转
        mRvHistory.setLayoutManager(mLayoutManager);
        // Horizontal
        OverScrollDecoratorHelper.setUpOverScroll(mRvHistory, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        mRvHistory.setflingScale(0.75);

        mRvHistory.setAdapter(mAdapter);
        mAdapter.setDataList(mList);

    }


    @OnClick(R.id.ll_clear_cache)
    void clearCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (VideoCacheManager.clearAllCache(App.getInstance())) {
                    FRToast.showToastSafe("清除缓存成功");
                }
            }
        }).start();
    }

    @OnClick(R.id.ll_footmark)
    void footMarkClick() {
        startActivity(new Intent(getActivity(), FootMarkActivity.class));
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), HistoryDetailActivity.class);
        intent.putExtra("VideoListItem", mList.get(position));
        getActivity().startActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mList = FileUtils.readParcelableList(App.getInstance(), ConstantsValue.HISTORY_VIDEO, VideoListItem.class);

            if (mList != null && mList.size() > 0) {
                mRvHistory.setVisibility(View.VISIBLE);
            } else {
                mRvHistory.setVisibility(View.GONE);
            }

            mAdapter.setDataList(mList);

            if (mList != null && mList.size() > 0) {
                mRvHistory.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getHistory(RefreshHistoryEvent event) {
        mList = event.getList();

        mAdapter.setDataList(event.getList());

        if (mList != null && mList.size() > 0) {
            mRvHistory.setVisibility(View.VISIBLE);
        } else {
            mRvHistory.setVisibility(View.GONE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ClearHistory(ClearListEvent event) {
        mRvHistory.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
