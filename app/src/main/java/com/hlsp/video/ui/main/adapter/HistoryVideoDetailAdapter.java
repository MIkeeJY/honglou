package com.hlsp.video.ui.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.hlsp.video.R;
import com.hlsp.video.bean.VideoListItem;

import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.utils.CygView;

/**
 * 播放历史详情页
 */
public class HistoryVideoDetailAdapter extends CygBaseRecyclerAdapter<VideoListItem, HistoryDetailViewHolder> {

    public HistoryVideoDetailAdapter(Context context, OnItemClickListener<HistoryDetailViewHolder> listener) {
        super(context, listener);
    }

    @Override
    public HistoryDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryDetailViewHolder(CygView.inflateLayout(getContext(), R.layout.item_history_detail_list, parent, false));
    }


}
