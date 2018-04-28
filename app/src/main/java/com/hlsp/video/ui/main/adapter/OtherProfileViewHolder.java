package com.hlsp.video.ui.main.adapter;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.bean.AuthorVideo;
import com.hlsp.video.utils.DensityUtil;
import com.hlsp.video.utils.WindowUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerViewHolder;


/**
 * Created by hackest on 2018-04-27
 */

public class OtherProfileViewHolder extends CygBaseRecyclerViewHolder<AuthorVideo> {

    @BindView(R.id.video_img) SimpleDraweeView video_img;

    public OtherProfileViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

    }

    @Override
    protected void onItemDataUpdated(@Nullable AuthorVideo data) {
        if (data != null) {
            ViewGroup.LayoutParams params = video_img.getLayoutParams();
            params.width = (WindowUtil.getScreenWidth(App.getInstance()) - DensityUtil.dip2px(App.getInstance(), 2)) / 2;
            params.height = (params.width) * 6 / 5;
            video_img.setLayoutParams(params);

            final Uri uri = Uri.parse(data.getVideo_coverURL());

            if (isNotEqualsUriPath(video_img, data.getVideo_coverURL())) {
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setOldController(video_img.getController())
                        .setControllerListener(new BaseControllerListener<ImageInfo>() {

                            @Override
                            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                                video_img.setTag(R.id.nearby_img, uri);
                            }

                        })
                        .build();

                video_img.setController(controller);
            }


        }
    }

    /**
     * 解决fresco 闪屏
     *
     * @param mNearbyImg
     * @param imgUrl
     * @return
     */
    public boolean isNotEqualsUriPath(SimpleDraweeView mNearbyImg, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl) || TextUtils.isEmpty(mNearbyImg.getTag(R.id.nearby_img) + "")) {
            return false;
        }
        return !(mNearbyImg.getTag(R.id.nearby_img) + "").equals(imgUrl);
    }

}
