package com.hlsp.video.ui.main.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.bean.data.VideoUrlData;
import com.hlsp.video.model.data.DataHttpSource;
import com.hlsp.video.model.main.MainModel;
import com.hlsp.video.utils.CommonUtils;
import com.hlsp.video.utils.GlideUtils;
import com.hlsp.video.utils.Utils;
import com.hlsp.video.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import chuangyuan.ycj.videolibrary.listener.VideoInfoListener;
import chuangyuan.ycj.videolibrary.video.ManualPlayer;
import chuangyuan.ycj.videolibrary.whole.WholeMediaSource;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;
import cn.share.jack.cyghttp.callback.CygBaseObserver;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerViewHolder;


/**
 * Created by jack on 2017/6/14
 */

public class RecommondViewHolder extends CygBaseRecyclerViewHolder<VideoListItem> {

    @BindView(R.id.iv_user_avatar) CircleImageView mIvUserAvatar;
    @BindView(R.id.tv_username) TextView mTvUsername;
    @BindView(R.id.tv_play_count) TextView mTvPlayCount;

    @BindView(R.id.exo_play_context_id) VideoPlayerView videoPlayer;

    ManualPlayer manualPlayer;
    WholeMediaSource wholeMediaSource;

    private SimpleDraweeView mExoPreviewImage;
    private ImageButton mExoPreviewPlay;
    private TextView mExoControlsTitle2;
    private TextView mExoControlsSize;


    public RecommondViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        wholeMediaSource = new WholeMediaSource(view.getContext(), new DataHttpSource(App.getInstance()));
        manualPlayer = new ManualPlayer((Activity) view.getContext(), wholeMediaSource, videoPlayer);

        mExoPreviewImage = (SimpleDraweeView) videoPlayer.findViewById(R.id.exo_preview_image);
        mExoPreviewPlay = (ImageButton) videoPlayer.findViewById(R.id.exo_preview_play);
        mExoControlsTitle2 = (TextView) videoPlayer.findViewById(R.id.exo_controls_title2);
        mExoControlsSize = (TextView) videoPlayer.findViewById(R.id.exo_controls_size);

    }

    @Override
    protected void onItemDataUpdated(@Nullable final VideoListItem data) {
        if (data != null) {
            GlideUtils.loadImage(App.getInstance(), data.getVideo_author_avatarURL(), mIvUserAvatar, null, R.color.black, R.color.black);
            mTvUsername.setText(data.getVideo_author_name());
            mTvPlayCount.setText(Utils.formatNumber(data.getVideo_count_play()) + "次观看");

            //设置列表item播放当前进度一定设置.不然不会保存进度
            manualPlayer.setTag(getAdapterPosition());

            mExoControlsSize.setText(CommonUtils.getTime(data.getVideo_duration()));

            Uri uri = Uri.parse(data.getVideo_coverURL());

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setOldController(mExoPreviewImage.getController())
                    .build();

            mExoPreviewImage.setController(controller);

            mExoControlsTitle2.setText(data.getVideo_name());

            manualPlayer.setVideoInfoListener(new VideoInfoListener() {
                @Override
                public void onPlayStart() {

                }

                @Override
                public void onLoadingChanged() {

                }

                @Override
                public void onPlayerError(@Nullable ExoPlaybackException e) {

                }

                @Override
                public void onPlayEnd() {

                }

                @Override
                public void isPlaying(boolean playWhenReady) {

                }
            });


            manualPlayer.setOnPlayClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ("dingyue".equals(data.getVideo_source())) {
                        manualPlayer.setPlayUri(data.getVideo_playURL());
                        manualPlayer.startPlayer();
                        return;
                    }

                    MainModel.getInstance().executeVideoUrl(data.getVideo_id(), data.getVideo_extData(), new CygBaseObserver<VideoUrlData>() {
                        @Override
                        protected void onBaseNext(VideoUrlData data) {
                            manualPlayer.setPlayUri(data.getVideo_url());
                            manualPlayer.startPlayer();
                        }
                    });
                }
            });


            videoPlayer.getPreviewImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ("dingyue".equals(data.getVideo_source())) {
                        manualPlayer.setPlayUri(data.getVideo_playURL());
                        manualPlayer.startPlayer();
                        return;
                    }

                    videoPlayer.getPreviewImage().setEnabled(false);

                    MainModel.getInstance().executeVideoUrl(data.getVideo_id(), data.getVideo_extData(), new CygBaseObserver<VideoUrlData>() {
                        @Override
                        protected void onBaseNext(VideoUrlData data) {
                            manualPlayer.setPlayUri(data.getVideo_url());
                            manualPlayer.startPlayer();
                            videoPlayer.getPreviewImage().setEnabled(true);
                        }
                    });

                }
            });


        }
    }


}
