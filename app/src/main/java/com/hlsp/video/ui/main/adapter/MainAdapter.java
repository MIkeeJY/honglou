package com.hlsp.video.ui.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.hlsp.video.R;
import com.hlsp.video.bean.data.LevideoData;

import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.utils.CygView;

/**
 * Created by jack on 2017/6/14
 */

public class MainAdapter extends CygBaseRecyclerAdapter<LevideoData, MainViewHolder> {

    public MainAdapter(Context context, OnItemClickListener<MainViewHolder> listener) {
        super(context, listener);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(CygView.inflateLayout(getContext(), R.layout.item_nearby, parent, false));
    }
}
