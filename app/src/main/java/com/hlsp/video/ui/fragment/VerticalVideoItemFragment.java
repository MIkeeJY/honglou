package com.hlsp.video.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;
import com.hlsp.video.bean.data.LevideoData;
import com.hlsp.video.utils.GlideUtils;
import com.hlsp.video.utils.Utils;
import com.hlsp.video.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hackest on 2018/3/5.
 */

public class VerticalVideoItemFragment extends BaseFragment {

    LevideoData mData;

    @BindView(R.id.iv_user_avatar) CircleImageView mIvUserAvatar;
    @BindView(R.id.tv_username) TextView mTvUsername;
    @BindView(R.id.tv_like_count) TextView mTvLikeCount;
    @BindView(R.id.tv_play_count) TextView mTvPlayCount;


    public static VerticalVideoItemFragment newInstance() {
        Bundle args = new Bundle();
        VerticalVideoItemFragment fragment = new VerticalVideoItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_room;
    }

    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);
//        initdata();
    }

    public void initdata() {
        if (mIvUserAvatar != null) {
            GlideUtils.loadImage(App.getInstance(), mData.getAuthorImgUrl(), mIvUserAvatar, null);
        }

        if (mTvUsername != null) {
            mTvUsername.setText(mData.getAuthorName());
        }

        if (mTvPlayCount != null) {
            mTvPlayCount.setText(Utils.formatNumber(mData.getPlayCount()) + "播放");

        }

        if (mTvLikeCount != null) {
            mTvLikeCount.setText(Utils.formatNumber(mData.getLikeCount()) + "赞");
        }

    }


    public void setmData(LevideoData mData) {
        this.mData = mData;
    }
}
