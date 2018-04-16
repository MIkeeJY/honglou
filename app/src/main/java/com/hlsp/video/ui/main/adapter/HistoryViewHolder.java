package com.hlsp.video.ui.main.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerViewHolder;


/**
 * Created by hackest on 2018-04-09
 */

public class HistoryViewHolder extends CygBaseRecyclerViewHolder<VideoListItem> {
    @BindView(R.id.iv_cover) ImageView mIvCover;
    @BindView(R.id.tv_video_title) TextView mTvVideoTitle;


    public HistoryViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

    }

    @Override
    protected void onItemDataUpdated(@Nullable final VideoListItem data) {
        if (data != null) {
            GlideUtils.loadImage(App.getInstance(), data.getVideo_coverURL(), mIvCover, null, R.color.white, R.color.transparent);
            mTvVideoTitle.setText(data.getVideo_name());

        }
    }


}
