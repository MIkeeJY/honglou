package com.hyxsp.video.ui.main.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyxsp.video.App;
import com.hyxsp.video.R;
import com.hyxsp.video.bean.data.LevideoData;
import com.hyxsp.video.utils.WindowUtil;

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
            ViewGroup.LayoutParams params = mNearbyImg.getLayoutParams();
            params.width = WindowUtil.getScreenWidth(App.getInstance()) / 2;
            params.height = (params.width) * 8 / 5;
            mNearbyImg.setLayoutParams(params);

            mNearbyImg.setImageURI(Uri.parse(data.getCoverImgUrl()));


        }
    }

}
