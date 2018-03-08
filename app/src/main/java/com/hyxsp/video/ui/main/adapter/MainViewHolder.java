package com.hyxsp.video.ui.main.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyxsp.video.R;
import com.hyxsp.video.bean.data.LevideoData;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerViewHolder;


/**
 * Created by jack on 2017/6/14
 */

public class MainViewHolder extends CygBaseRecyclerViewHolder<LevideoData> {

    @BindView(R.id.nearby_img) SimpleDraweeView mNearbyImg;

    public MainViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

    }

    @Override
    protected void onItemDataUpdated(@Nullable LevideoData data) {
        if (data != null) {
            mNearbyImg.setImageURI(Uri.parse(data.getCoverImgUrl()));
        }
    }

}
