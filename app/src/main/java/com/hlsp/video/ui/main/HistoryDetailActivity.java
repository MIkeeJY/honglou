/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.hlsp.video.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.dueeeke.videoplayer.controller.StandardVideoController;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseActivity;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.bean.data.VideoListData;
import com.hlsp.video.bean.data.VideoUrlData;
import com.hlsp.video.model.main.MainModel;
import com.hlsp.video.ui.main.adapter.HistoryDetailViewHolder;
import com.hlsp.video.ui.main.adapter.HistoryVideoDetailAdapter;
import com.hlsp.video.utils.CommonUtils;
import com.hlsp.video.utils.DateUtil;
import com.hlsp.video.utils.GlideUtils;
import com.hlsp.video.utils.StatusBarCompat;
import com.hlsp.video.view.CircleImageView;
import com.jack.mc.cyg.cygptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.share.jack.cyghttp.callback.CygBaseObserver;
import cn.share.jack.cygwidget.loadmore.OnScrollToBottomLoadMoreListener;
import cn.share.jack.cygwidget.recyclerview.PtrRecyclerViewUIComponent;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;

/**
 * 浏览足迹详情
 */
public class HistoryDetailActivity extends BaseActivity implements CygBaseRecyclerAdapter.OnItemClickListener<HistoryDetailViewHolder> {

    @BindView(R.id.ijk_videoview) IjkVideoView ijkVideoView;

    @BindView(R.id.am_ptr_framelayout) PtrRecyclerViewUIComponent ptrRecyclerViewUIComponent;
    private RecyclerAdapterWithHF mAdapter;

    VideoListItem data;
    private StandardVideoController controller;
    private PlayerConfig mPlayerConfig;


    private List<VideoListItem> mRecommondList = new ArrayList<>();

    private final int id = 0;
    private final String backdata = "1";
    private final String loadMore = "down";

    private HistoryVideoDetailAdapter adapter;

    private CircleImageView mIvUserAvatar;
    private TextView mTvUsername;
    private TextView mTvVideoTitle;
    private TextView mTvPubTime;

    boolean isLoadMore = false;
    Handler mHandler = new Handler();


    @Override
    protected int layoutRes() {
        return R.layout.activity_history_detail;
    }


    @OnClick(R.id.iv_back)
    void back() {
        onBackPressed();
    }

    @Override
    protected void initView() {
        data = getIntent().getParcelableExtra("VideoListItem");

        controller = new StandardVideoController(this);
        ijkVideoView.setVideoController(controller);
        mPlayerConfig = new PlayerConfig.Builder().build();

        GlideUtils.loadImage(App.getInstance(), data.getVideo_coverURL(), controller.getThumb(), null, R.color.white, R.color.white);
        controller.getIjkControlSize().setText(CommonUtils.getTime(data.getVideo_duration()));

        ijkVideoView.setPlayerConfig(mPlayerConfig);

        ijkVideoView.setTitle(data.getVideo_name());
        ijkVideoView.setVideoController(controller);

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

        initRecyclerView();
    }


    private void initRecyclerView() {
        ptrRecyclerViewUIComponent.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryVideoDetailAdapter(this, this);
        mAdapter = new RecyclerAdapterWithHF(adapter);

        View view = LayoutInflater.from(this).inflate(R.layout.view_history_detail_header, null);
        mIvUserAvatar = (CircleImageView) view.findViewById(R.id.iv_user_avatar);
        mTvUsername = (TextView) view.findViewById(R.id.tv_username);
        mTvVideoTitle = (TextView) view.findViewById(R.id.tv_video_title);
        mTvPubTime = (TextView) view.findViewById(R.id.tv_pub_time);

        GlideUtils.loadImage(App.getInstance(), data.getVideo_author_avatarURL(), mIvUserAvatar, null);

        mTvUsername.setText(data.getVideo_author_name());
        mTvVideoTitle.setText(data.getVideo_name());

        try {
            mTvPubTime.setText(DateUtil.formatDate(Long.parseLong(data.getVideo_pubtime()) * 1000));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        mAdapter.addHeader(view);

        ptrRecyclerViewUIComponent.setAdapter(mAdapter);

        ptrRecyclerViewUIComponent.setCanRefresh(false);
        ptrRecyclerViewUIComponent.setLoadMoreEnable(true);


        ptrRecyclerViewUIComponent.setOnScrollToBottomLoadMoreListener(new OnScrollToBottomLoadMoreListener() {
            @Override
            public void onScrollToBottomLoadMore() {
                isLoadMore = true;
                getVideoList(id, backdata, loadMore);
            }
        });


        getVideoList(id, backdata, "up");
    }


    private void getVideoList(int id, String backdata, String loadMore) {
        MainModel.getInstance().executeVideoList(id + "", backdata, loadMore, new CygBaseObserver<VideoListData>() {
            @Override
            protected void onBaseNext(VideoListData data) {

                if (isLoadMore) {
                    mRecommondList.addAll(data.getList());
                    adapter.setDataList(mRecommondList, false);
                    mAdapter.notifyDataSetChanged();
                    ptrRecyclerViewUIComponent.loadMoreComplete(true);

                } else {
                    mRecommondList = data.getList();
                    adapter.setDataList(mRecommondList);
                    mAdapter.notifyDataSetChanged();
                }


            }

            @Override
            protected void onBaseError(Throwable t) {
                super.onBaseError(t);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ptrRecyclerViewUIComponent.loadMoreComplete(true);
                    }
                });

            }

        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        ijkVideoView.pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        ijkVideoView.resume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ijkVideoView.release();
    }


    @Override
    public void onBackPressed() {
        if (!ijkVideoView.onBackPressed()) {
            super.onBackPressed();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtils.e("横屏");
        } else {
            StatusBarCompat.translucentStatusBar(this, true);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, HistoryDetailActivity.class);
        intent.putExtra("VideoListItem", mRecommondList.get(position));
        startActivity(intent);
    }
}
