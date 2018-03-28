package com.hlsp.video.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseLoadFragment;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.bean.data.VideoListData;
import com.hlsp.video.model.main.MainModel;
import com.hlsp.video.ui.main.adapter.RecommondAdapter;
import com.hlsp.video.ui.main.adapter.RecommondViewHolder;
import com.hlsp.video.utils.DensityUtil;
import com.jack.mc.cyg.cygptr.PtrFrameLayout;
import com.jack.mc.cyg.cygptr.header.MaterialHeader;
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


    private List<VideoListItem> mList = new ArrayList<>();

    private RecyclerAdapterWithHF mAdapter;
    private RecommondAdapter adapter;


    int position;
    int id;
    int backdata_Recoomond = 1;
    int backdata_Funny = 1;
    int backdata_Entertainment = 1;

    String RecoomondLoadMore = "down";
    String FunnyLoadMore = "down";
    String EntertainmentLoadMore = "down";

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
//        ptrRecyclerViewUIComponent.delayRefresh(100);
        ptrRecyclerViewUIComponent.setLoadMoreEnable(true);


        ptrRecyclerViewUIComponent.setOnPullToRefreshListener(new OnPullToRefreshListener() {
            @Override
            public void onPullToRefresh() {

                if (mList != null && mList.size() > 0) {
                    mList.clear();
                }

                switch (position) {
                    case 0:
                        RecoomondLoadMore = "down";
                        getVideoList(id,RecoomondLoadMore);

                        break;
                    case 1:
                        FunnyLoadMore = "down";
                        getVideoList(id,FunnyLoadMore);

                        break;
                    case 2:
                        EntertainmentLoadMore = "down";
                        getVideoList(id,EntertainmentLoadMore);

                        break;


                }

            }
        });

        ptrRecyclerViewUIComponent.setOnScrollToBottomLoadMoreListener(new OnScrollToBottomLoadMoreListener() {
            @Override
            public void onScrollToBottomLoadMore() {

                switch (position) {
                    case 0:
                        RecoomondLoadMore = "up";
                        getVideoList(id,RecoomondLoadMore);

                        break;
                    case 1:
                        FunnyLoadMore = "up";
                        getVideoList(id,FunnyLoadMore);

                        break;
                    case 2:
                        EntertainmentLoadMore = "up";
                        getVideoList(id,EntertainmentLoadMore);

                        break;


                }

            }
        });

    }


    private void initHeader() {
        MaterialHeader header = new MaterialHeader(getActivity());
        int[] colors = App.getInstance().getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.WRAP_CONTENT));
        header.setPadding(0, DensityUtil.dip2px(App.getInstance(), 15), 0, DensityUtil.dip2px(App.getInstance(), 10));
        header.setPtrFrameLayout(ptrRecyclerViewUIComponent);

        ptrRecyclerViewUIComponent.setDurationToCloseHeader(300);
        ptrRecyclerViewUIComponent.setHeaderView(header);
        ptrRecyclerViewUIComponent.addPtrUIHandler(header);

    }


    @Override
    protected void lazyLoad() {
        ptrRecyclerViewUIComponent.delayRefresh(100);
    }



    private void getVideoList(int id,String backdata) {
        MainModel.getInstance().executeVideoList(id + "", backdata + "", "down", new CygBaseObserver<VideoListData>() {
            @Override
            protected void onBaseNext(VideoListData data) {
                mList = data.getList();

            }

            @Override
            protected void onBaseError(Throwable t) {
                super.onBaseError(t);
                ptrRecyclerViewUIComponent.loadMoreComplete(true);
                ptrRecyclerViewUIComponent.refreshComplete();

            }

        });
    }


    @Override
    public void onItemClick(int position) {

    }
}
