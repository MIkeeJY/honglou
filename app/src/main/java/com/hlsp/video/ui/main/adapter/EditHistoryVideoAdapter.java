package com.hlsp.video.ui.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.hlsp.video.R;
import com.hlsp.video.bean.VideoListItem;

import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.utils.CygView;

/**
 * 编辑浏览足迹
 */
public class EditHistoryVideoAdapter extends CygBaseRecyclerAdapter<VideoListItem, EditHistoryViewHolder> {

    public EditHistoryVideoAdapter(Context context, OnItemClickListener<EditHistoryViewHolder> listener) {
        super(context, listener);
    }

    @Override
    public EditHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditHistoryViewHolder(CygView.inflateLayout(getContext(), R.layout.item_edit_history_list, parent, false));
    }


}
