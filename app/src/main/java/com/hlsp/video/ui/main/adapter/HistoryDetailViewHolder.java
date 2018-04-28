package com.hlsp.video.ui.main.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.utils.CommonUtils;
import com.hlsp.video.utils.DateUtil;
import com.hlsp.video.utils.GlideUtils;
import com.hlsp.video.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerViewHolder;


/**
 * Created by hackest on 2018-04-09
 */

public class HistoryDetailViewHolder extends CygBaseRecyclerViewHolder<VideoListItem> {
    @BindView(R.id.iv_detail_cover) ImageView mIvDetailCover;
    @BindView(R.id.tv_detail_title) TextView mTvDetailTitle;
    @BindView(R.id.tv_detail_count) TextView mTvDetailCount;
    @BindView(R.id.tv_detail_time) TextView mTvDetailTime;
    @BindView(R.id.ijk_controls_size) TextView mTvControlSize;

    Context mContext;


    public HistoryDetailViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        mContext = view.getContext();
    }

    @Override
    protected void onItemDataUpdated(@Nullable final VideoListItem data) {
        if (data != null) {
            GlideUtils.loadImage(App.getInstance(), data.getVideo_coverURL(), mIvDetailCover, null, R.color.white, R.color.transparent);
            mTvDetailTitle.setText(data.getVideo_name());

            mTvDetailCount.setText(Utils.formatNumber(data.getVideo_count_play()) + "次观看");

            mTvControlSize.setText(CommonUtils.getTime(data.getVideo_duration()));

            try {
                mTvDetailTime.setText(DateUtil.formatDate(Long.parseLong(data.getVideo_pubtime()) * 1000));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
    }


}
