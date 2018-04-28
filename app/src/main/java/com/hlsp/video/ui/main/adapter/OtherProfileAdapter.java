package com.hlsp.video.ui.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.hlsp.video.R;
import com.hlsp.video.bean.AuthorVideo;

import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.utils.CygView;

/**
 * Created by hackest on 2018-04-27
 */

public class OtherProfileAdapter extends CygBaseRecyclerAdapter<AuthorVideo, OtherProfileViewHolder> {

    public OtherProfileAdapter(Context context, OnItemClickListener<OtherProfileViewHolder> listener) {
        super(context, listener);
    }

    @Override
    public OtherProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OtherProfileViewHolder(CygView.inflateLayout(getContext(), R.layout.item_other_profile, parent, false));
    }
}
