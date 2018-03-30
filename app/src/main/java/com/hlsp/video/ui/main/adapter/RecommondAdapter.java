package com.hlsp.video.ui.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.hlsp.video.R;
import com.hlsp.video.bean.VideoListItem;

import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.utils.CygView;

/**
 * 推荐列表
 */
public class RecommondAdapter extends CygBaseRecyclerAdapter<VideoListItem, RecommondViewHolder> {

    public RecommondAdapter(Context context, OnItemClickListener<RecommondViewHolder> listener) {
        super(context, listener);
    }

    @Override
    public RecommondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommondViewHolder(CygView.inflateLayout(getContext(), R.layout.item_recommond_video, parent, false));
    }


}
