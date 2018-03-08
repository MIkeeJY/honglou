package com.hyxsp.video.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.hyxsp.video.App;
import com.hyxsp.video.R;
import com.hyxsp.video.base.BaseFragment;
import com.hyxsp.video.bean.data.DouyinVideoListData;
import com.hyxsp.video.bean.data.LevideoData;
import com.hyxsp.video.okhttp.http.OkHttpClientManager;
import com.hyxsp.video.ui.main.VerticalVideoActivity;
import com.hyxsp.video.ui.main.adapter.MainAdapter;
import com.hyxsp.video.ui.main.adapter.MainViewHolder;
import com.hyxsp.video.utils.DensityUtil;
import com.hyxsp.video.utils.DouyinUtils;
import com.jack.mc.cyg.cygptr.PtrFrameLayout;
import com.jack.mc.cyg.cygptr.header.MaterialHeader;
import com.jack.mc.cyg.cygptr.recyclerview.RecyclerAdapterWithHF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cygwidget.loadmore.OnScrollToBottomLoadMoreListener;
import cn.share.jack.cygwidget.recyclerview.PtrRecyclerViewUIComponent;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.refersh.OnPullToRefreshListener;
import okhttp3.Request;


/**
 * 推荐Fragment
 * Created by hackest on 2018-02-01.
 */

public class MainPageDouYinFragment extends BaseFragment implements CygBaseRecyclerAdapter.OnItemClickListener<MainViewHolder> {
    @BindView(R.id.am_ptr_framelayout) PtrRecyclerViewUIComponent ptrRecyclerViewUIComponent;


    private MainAdapter adapter;

    private RecyclerAdapterWithHF mAdapter;

    private List<LevideoData> mList = new ArrayList<>();


    private long max_cursor = 0;

    boolean isLoadMore = false;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_main_page_douyin;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        adapter = new MainAdapter(getActivity(), this);
        mAdapter = new RecyclerAdapterWithHF(adapter);

//        mPresenter = new SyPresenter(this, getContext());

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.isHeader(position) || mAdapter.isFooter(position)) ? gridLayoutManager.getSpanCount() : 1;
            }
        });

        ptrRecyclerViewUIComponent.setLayoutManager(gridLayoutManager);
        ptrRecyclerViewUIComponent.setAdapter(mAdapter);

        initHeader();

        ptrRecyclerViewUIComponent.delayRefresh(100);
        ptrRecyclerViewUIComponent.setLoadMoreEnable(true);

        ptrRecyclerViewUIComponent.setOnPullToRefreshListener(new OnPullToRefreshListener() {
            @Override
            public void onPullToRefresh() {
                max_cursor = 0;
                isLoadMore = false;
                if (mList != null && mList.size() > 0) {
                    mList.clear();
                }

                getDouyinListData();


            }
        });

        ptrRecyclerViewUIComponent.setOnScrollToBottomLoadMoreListener(new OnScrollToBottomLoadMoreListener() {
            @Override
            public void onScrollToBottomLoadMore() {
                isLoadMore = true;
                getDouyinListData();
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


    /**
     * 下拉数据规律：min_cursor=max_cursor=0
     * 上拉数据规律：
     * 第二次请求取第一次请求返回的json数据中的min_cursor字段，max_cursor不需要携带。
     * 第三次以及后面所有的请求都只带max_cursor字段，值为第一次请求返回的json数据中的max_cursor字段
     */
    public void getDouyinListData() {
        String url = DouyinUtils.getEncryptUrl(getActivity(), 0, max_cursor);
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                DouyinVideoListData listData = DouyinVideoListData.fromJSONData(response);
                max_cursor = listData.getMaxCursor();

                LogUtils.e(listData.getVideoDataList().size());

                if (isLoadMore) {
                    mList.addAll(listData.getVideoDataList());
                    adapter.setDataList(mList);
                    mAdapter.notifyDataSetChanged();
                    ptrRecyclerViewUIComponent.loadMoreComplete(true);

                } else {
                    mList = listData.getVideoDataList();
                    adapter.setDataList(mList);
                    mAdapter.notifyDataSetChanged();
                    ptrRecyclerViewUIComponent.refreshComplete();
                }


            }

            @Override
            public void onFailure(Request request, IOException e) {
                ptrRecyclerViewUIComponent.loadMoreComplete(true);
                ptrRecyclerViewUIComponent.refreshComplete();
            }
        });


    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), VerticalVideoActivity.class);
        intent.putParcelableArrayListExtra("videoUrlList", (ArrayList<LevideoData>) mList);
        intent.putExtra("position", position);
        getActivity().startActivity(intent);

    }
}
