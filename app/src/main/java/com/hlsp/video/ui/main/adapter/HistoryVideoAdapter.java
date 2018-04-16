package com.hlsp.video.ui.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.hlsp.video.R;
import com.hlsp.video.bean.VideoListItem;

import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.utils.CygView;

/**
 * 播放历史
 */
public class HistoryVideoAdapter extends CygBaseRecyclerAdapter<VideoListItem, HistoryViewHolder> {

    public HistoryVideoAdapter(Context context, OnItemClickListener<HistoryViewHolder> listener) {
        super(context, listener);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(CygView.inflateLayout(getContext(), R.layout.item_history_video, parent, false));
    }


}
