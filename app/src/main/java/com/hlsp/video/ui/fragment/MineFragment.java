package com.hlsp.video.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dueeeke.videoplayer.player.VideoCacheManager;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;
import com.hlsp.video.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.share.jack.cyghttp.util.FRToast;


/**
 * Created by hackest on 2018-02-01.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.ll_footmark) View mFootmark;
    @BindView(R.id.rv_history) RecyclerView mRvHistory;
    @BindView(R.id.ll_my_favorite) View mMyFavorite;
    @BindView(R.id.ll_feedback) View mFeedback;
    @BindView(R.id.ll_push) View mPush;
    @BindView(R.id.ll_version) View mVersion;
    @BindView(R.id.tv_version) TextView mTvVersion;
    @BindView(R.id.ll_clear_cache) View mClearCache;


    @Override
    protected int layoutRes() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);

        mTvVersion.setText(Utils.getVersion(App.getInstance()));

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


}
