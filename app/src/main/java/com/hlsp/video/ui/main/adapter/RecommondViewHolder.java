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
import com.hlsp.video.utils.GlideUtils;
import com.hlsp.video.utils.Utils;
import com.hlsp.video.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cyghttp.callback.CygBaseObserver;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerViewHolder;


/**
 * Created by jack on 2017/6/14
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
                .enableCache()
                .addToPlayerManager()
                .build();
    }

    @Override
    protected void onItemDataUpdated(@Nullable final VideoListItem data) {
        if (data != null) {
            GlideUtils.loadImage(App.getInstance(), data.getVideo_author_avatarURL(), mIvUserAvatar, null, R.color.black, R.color.black);

            GlideUtils.loadImage(App.getInstance(), data.getVideo_coverURL(), controller.getThumb(), null, R.color.white, R.color.white);

            ijkVideoView.setPlayerConfig(mPlayerConfig);

            ijkVideoView.setTitle(data.getVideo_name());
            ijkVideoView.setVideoController(controller);
            ijkVideoView.setTag(getAdapterPosition());


            if ("dingyue".equals(data.getVideo_source())) {
                ijkVideoView.setUrl(data.getVideo_playURL());
            } else {
                MainModel.getInstance().executeVideoUrl(data.getVideo_id(), data.getVideo_extData(), new CygBaseObserver<VideoUrlData>() {
                    @Override
                    protected void onBaseNext(VideoUrlData videoUrlData) {
                        ijkVideoView.setUrl(videoUrlData.getVideo_url());
                    }
                });
            }

            mTvUsername.setText(data.getVideo_author_name());
            mTvPlayCount.setText(Utils.formatNumber(data.getVideo_count_play()) + "次观看");


//
//            //设置列表item播放当前进度一定设置.不然不会保存进度
//            manualPlayer.setTag(getAdapterPosition());
//
//            mExoControlsSize.setText(CommonUtils.getTime(data.getVideo_duration()));
//
//            Uri uri = Uri.parse(data.getVideo_coverURL());
//
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setUri(uri)
//                    .setOldController(mExoPreviewImage.getController())
//                    .build();
//
//            mExoPreviewImage.setController(controller);
//
//            mExoControlsTitle2.setText(data.getVideo_name());
//
//
//            manualPlayer.setOnPlayClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if ("dingyue".equals(data.getVideo_source())) {
//                        manualPlayer.setPlayUri(data.getVideo_playURL());
//                        manualPlayer.startPlayer();
//                        return;
//                    }
//
//                    MainModel.getInstance().executeVideoUrl(data.getVideo_id(), data.getVideo_extData(), new CygBaseObserver<VideoUrlData>() {
//                        @Override
//                        protected void onBaseNext(VideoUrlData data) {
//                            manualPlayer.setPlayUri(data.getVideo_url());
//                            manualPlayer.startPlayer();
//                        }
//                    });
//                }
//            });
//
//
//            videoPlayer.getPreviewImage().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if ("dingyue".equals(data.getVideo_source())) {
//                        manualPlayer.setPlayUri(data.getVideo_playURL());
//                        manualPlayer.startPlayer();
//                        return;
//                    }
//
//                    videoPlayer.getPreviewImage().setEnabled(false);
//
//                    MainModel.getInstance().executeVideoUrl(data.getVideo_id(), data.getVideo_extData(), new CygBaseObserver<VideoUrlData>() {
//                        @Override
//                        protected void onBaseNext(VideoUrlData data) {
//                            manualPlayer.setPlayUri(data.getVideo_url());
//                            manualPlayer.startPlayer();
//                            videoPlayer.getPreviewImage().setEnabled(true);
//                        }
//                    });
//
//                }
//            });


        }
    }


}
