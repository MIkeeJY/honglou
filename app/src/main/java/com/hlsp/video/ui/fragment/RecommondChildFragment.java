package com.hlsp.video.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dueeeke.videoplayer.player.IjkVideoView;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseLoadFragment;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.bean.data.VideoListData;
import com.hlsp.video.model.main.MainModel;
import com.hlsp.video.ui.main.adapter.RecommondAdapter;
import com.hlsp.video.ui.main.adapter.RecommondViewHolder;
import com.hlsp.video.utils.DensityUtil;
import com.hlsp.video.widget.MyCustomHeader;
import com.jack.mc.cyg.cygptr.PtrFrameLayout;
import com.jack.mc.cyg.cygptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cyghttp.callback.CygBaseObserver;
import cn.share.jack.cygwidget.loadmore.OnScrollToBottomLoadMoreListener;
import cn.share.jack.cygwidget.recyclerview.PtrRecyclerViewUIComponent;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.refersh.OnPullToRefreshListener;

/**
 * 推荐，搞笑，娱乐
 * Created by hackest on 2018/3/26.
 */

public class RecommondChildFragment extends BaseLoadFragment implements CygBaseRecyclerAdapter.OnItemClickListener<RecommondViewHolder> {

    @BindView(R.id.am_ptr_framelayout) PtrRecyclerViewUIComponent ptrRecyclerViewUIComponent;
    @BindView(R.id.ar_empty_view) View emptyView;

    private List<VideoListItem> mRecommondList = new ArrayList<>();
    private List<VideoListItem> mFunnyList = new ArrayList<>();
    private List<VideoListItem> mEntertainmentList = new ArrayList<>();

    private RecyclerAdapterWithHF mAdapter;
    private RecommondAdapter adapter;

    private int position;
    private int id;
    private String backdata_Recommond = "1";
    private String backdata_Funny = "1";
    private String backdata_Entertainment = "1";

    private String RecommondLoadMore = "down";
    private String FunnyLoadMore = "down";
    private String EntertainmentLoadMore = "down";

    Handler mHandler = new Handler();

    public static RecommondChildFragment newInstance(Bundle args) {
        RecommondChildFragment fragment = new RecommondChildFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int layoutRes() {
        return R.layout.fragment_recommond_child;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        position = mBundle.getInt("position");
        id = mBundle.getInt("id");

        adapter = new RecommondAdapter(getActivity(), this);
        mAdapter = new RecyclerAdapterWithHF(adapter);

        ptrRecyclerViewUIComponent.setLayoutManager(new LinearLayoutManager(getActivity()));
        ptrRecyclerViewUIComponent.setAdapter(mAdapter);
        initHeader();

        ptrRecyclerViewUIComponent.setLoadMoreEnable(true);

        ptrRecyclerViewUIComponent.getRecyclerView().addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                IjkVideoView ijkVideoView = view.findViewById(R.id.ijk_videoview);
                if (ijkVideoView != null && !ijkVideoView.isFullScreen()) {
                    int tag = (int) ijkVideoView.getTag();
                    ijkVideoView.stopPlayback();
                }
            }
        });


        ptrRecyclerViewUIComponent.setOnPullToRefreshListener(new OnPullToRefreshListener() {
            @Override
            public void onPullToRefresh() {

                switch (position) {
                    case 0:
                        if (mRecommondList != null && mRecommondList.size() > 0) {
                            mRecommondList.clear();
                        }
                        RecommondLoadMore = "down";
                        getVideoList(id, backdata_Recommond, RecommondLoadMore);

                        break;
                    case 1:
                        if (mFunnyList != null && mFunnyList.size() > 0) {
                            mFunnyList.clear();
                        }
                        FunnyLoadMore = "down";
                        getVideoList(id, backdata_Funny, FunnyLoadMore);

                        break;
                    case 2:
                        if (mEntertainmentList != null && mEntertainmentList.size() > 0) {
                            mEntertainmentList.clear();
                        }
                        EntertainmentLoadMore = "down";
                        getVideoList(id, backdata_Entertainment, EntertainmentLoadMore);

                        break;


                }

            }
        });

        ptrRecyclerViewUIComponent.setOnScrollToBottomLoadMoreListener(new OnScrollToBottomLoadMoreListener() {
            @Override
            public void onScrollToBottomLoadMore() {

                switch (position) {
                    case 0:
                        RecommondLoadMore = "up";
                        getVideoList(id, backdata_Recommond, RecommondLoadMore);

                        break;
                    case 1:
                        FunnyLoadMore = "up";
                        getVideoList(id, backdata_Funny, FunnyLoadMore);

                        break;
                    case 2:
                        EntertainmentLoadMore = "up";
                        getVideoList(id, backdata_Entertainment, EntertainmentLoadMore);

                        break;


                }

            }
        });

    }


    private void initHeader() {
        MyCustomHeader header = new MyCustomHeader(getActivity());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.WRAP_CONTENT));
        header.setPadding(0, DensityUtil.dip2px(App.getInstance(), 15), 0, DensityUtil.dip2px(App.getInstance(), 10));

        header.setUp(ptrRecyclerViewUIComponent);

        ptrRecyclerViewUIComponent.setHeaderView(header);

        ptrRecyclerViewUIComponent.setDurationToCloseHeader(600);
        ptrRecyclerViewUIComponent.setLoadingMinTime(1200);

        ptrRecyclerViewUIComponent.addPtrUIHandler(header);

    }


    @Override
    protected void lazyLoad() {
        ptrRecyclerViewUIComponent.delayRefresh(100);
    }


    private void getVideoList(int id, String backdata, String loadMore) {
        MainModel.getInstance().executeVideoList(id + "", backdata, loadMore, new CygBaseObserver<VideoListData>() {
            @Override
            protected void onBaseNext(VideoListData data) {


                switch (position) {
                    case 0:
                        backdata_Recommond = data.getBackdata();
                        if ("up".equals(RecommondLoadMore)) {
                            mRecommondList.addAll(data.getList());
                            adapter.setDataList(mRecommondList, false);
                            mAdapter.notifyDataSetChanged();
                            ptrRecyclerViewUIComponent.loadMoreComplete(true);

                        } else {
                            mRecommondList = data.getList();
                            adapter.setDataList(mRecommondList);
                            mAdapter.notifyDataSetChanged();
                            ptrRecyclerViewUIComponent.refreshComplete();
                        }


                        break;
                    case 1:
                        backdata_Funny = data.getBackdata();
                        if ("up".equals(FunnyLoadMore)) {
                            mFunnyList.addAll(data.getList());
                            adapter.setDataList(mFunnyList, false);
                            mAdapter.notifyDataSetChanged();
                            ptrRecyclerViewUIComponent.loadMoreComplete(true);

                        } else {
                            mFunnyList = data.getList();
                            adapter.setDataList(mFunnyList);
                            mAdapter.notifyDataSetChanged();
                            ptrRecyclerViewUIComponent.refreshComplete();
                        }


                        break;
                    case 2:
                        backdata_Entertainment = data.getBackdata();
                        if ("up".equals(EntertainmentLoadMore)) {
                            mEntertainmentList.addAll(data.getList());
                            adapter.setDataList(mEntertainmentList, false);
                            mAdapter.notifyDataSetChanged();
                            ptrRecyclerViewUIComponent.loadMoreComplete(true);

                        } else {
                            mEntertainmentList = data.getList();
                            adapter.setDataList(mEntertainmentList);
                            mAdapter.notifyDataSetChanged();
                            ptrRecyclerViewUIComponent.refreshComplete();
                        }

                        break;


                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ptrRecyclerViewUIComponent.getRecyclerView().smoothScrollBy(0, 1);//偏移1像素解决前连个列表条目冲突
                    }
                });

            }

            @Override
            protected void onBaseError(Throwable t) {
                super.onBaseError(t);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ptrRecyclerViewUIComponent.loadMoreComplete(true);
                        ptrRecyclerViewUIComponent.refreshComplete();
                    }
                });

            }

        });
    }


    @Override
    public void onItemClick(int position) {

    }




}
