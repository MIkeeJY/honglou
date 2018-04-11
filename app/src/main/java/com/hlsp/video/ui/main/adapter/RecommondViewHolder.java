package com.hlsp.video.ui.main.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.dueeeke.videoplayer.controller.StandardVideoController;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.bean.data.VideoUrlData;
import com.hlsp.video.model.main.MainModel;
import com.hlsp.video.utils.CommonUtils;
import com.hlsp.video.utils.GlideUtils;
import com.hlsp.video.utils.Utils;
import com.hlsp.video.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cyghttp.callback.CygBaseObserver;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerViewHolder;


/**
 * Created by hackest on 2018-04-09
 */

public class RecommondViewHolder extends CygBaseRecyclerViewHolder<VideoListItem> {

    @BindView(R.id.iv_user_avatar) CircleImageView mIvUserAvatar;
    @BindView(R.id.tv_username) TextView mTvUsername;
    @BindView(R.id.tv_play_count) TextView mTvPlayCount;

    @BindView(R.id.ijk_videoview) IjkVideoView ijkVideoView;

    private StandardVideoController controller;
    private PlayerConfig mPlayerConfig;


    public RecommondViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);

        controller = new StandardVideoController(view.getContext());
        ijkVideoView.setVideoController(controller);
        mPlayerConfig = new PlayerConfig.Builder()
                .addToPlayerManager()
                .build();
    }

    @Override
    protected void onItemDataUpdated(@Nullable final VideoListItem data) {
        if (data != null) {
            GlideUtils.loadImage(App.getInstance(), data.getVideo_author_avatarURL(), mIvUserAvatar, null, R.color.black, R.color.black);

            GlideUtils.loadImage(App.getInstance(), data.getVideo_coverURL(), controller.getThumb(), null, R.color.white, R.color.white);

            controller.getIjkTitle().setText(data.getVideo_name());
            controller.getIjkControlSize().setText(CommonUtils.getTime(data.getVideo_duration()));


            ijkVideoView.setPlayerConfig(mPlayerConfig);

            ijkVideoView.setTitle(data.getVideo_name());
            ijkVideoView.setVideoController(controller);
            ijkVideoView.setTag(getAdapterPosition());


            controller.getThumb().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("dingyue".equals(data.getVideo_source())) {
                        ijkVideoView.setUrl(data.getVideo_playURL());
                        ijkVideoView.start();
                    } else {
                        MainModel.getInstance().executeVideoUrl(data.getVideo_id(), data.getVideo_extData(), new CygBaseObserver<VideoUrlData>() {
                            @Override
                            protected void onBaseNext(VideoUrlData videoUrlData) {
                                ijkVideoView.setUrl(videoUrlData.getVideo_url());
                                ijkVideoView.start();
                            }
                        });
                    }

                }
            });


            mTvUsername.setText(data.getVideo_author_name());
            mTvPlayCount.setText(Utils.formatNumber(data.getVideo_count_play()) + "次观看");


        }
    }


}
