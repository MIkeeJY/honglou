package com.hlsp.video.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;
import com.hlsp.video.bean.data.DouyinVideoListData;
import com.hlsp.video.bean.data.HotsoonVideoListData;
import com.hlsp.video.bean.data.LevideoData;
import com.hlsp.video.model.event.RefreshEvent;
import com.hlsp.video.okhttp.http.OkHttpClientManager;
import com.hlsp.video.ui.main.VerticalVideoActivity;
import com.hlsp.video.ui.main.adapter.MainAdapter;
import com.hlsp.video.ui.main.adapter.MainViewHolder;
import com.hlsp.video.utils.DensityUtil;
import com.hlsp.video.utils.DouyinUtils;
import com.hlsp.video.utils.HotsoonUtils;
import com.hlsp.video.utils.NoDoubleClickUtils;
import com.hlsp.video.utils.StatusBarCompat;
import com.hlsp.video.utils.ToastUtil;
import com.hlsp.video.utils.WeakDataHolder;
import com.hlsp.video.utils.statusbar.StatusBarFontHelper;
import com.hlsp.video.view.LoadFrameLayout;
import com.hlsp.video.widget.MyCustomHeader;
import com.jack.mc.cyg.cygptr.PtrFrameLayout;
import com.jack.mc.cyg.cygptr.recyclerview.RecyclerAdapterWithHF;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.ar_empty_view) View emptyView;
    @BindView(R.id.load_frameLayout) LoadFrameLayout loadFrameLayout;

    TextView mRetry;

    private MainAdapter adapter;

    private RecyclerAdapterWithHF mAdapter;

    private List<LevideoData> mList = new ArrayList<>();


    private long max_cursor = 0;

    boolean isLoadMore = false;

    boolean douYinDisable = false;

    MyCustomHeader header;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_main_page_douyin;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarCompat.translucentStatusBar(getActivity(), true);
    }


    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);
        mRetry = loadFrameLayout.findViewById(R.id.tv_retry);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    if (douYinDisable) {
                        HuoshanListData();
                    } else {
                        getDouyinListData();
                    }
                }


            }
        });


        adapter = new MainAdapter(getActivity(), this);
        mAdapter = new RecyclerAdapterWithHF(adapter);


        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.isHeader(position) || mAdapter.isFooter(position)) ? gridLayoutManager.getSpanCount() : 1;
            }
        });

        ptrRecyclerViewUIComponent.setLayoutManager(gridLayoutManager);
        ptrRecyclerViewUIComponent.setAdapter(mAdapter);
//        ptrRecyclerViewUIComponent.setEmptyView(emptyView);

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

                if (douYinDisable) {
                    HuoshanListData();
                } else {
                    getDouyinListData();
                }


            }
        });

        ptrRecyclerViewUIComponent.setOnScrollToBottomLoadMoreListener(new OnScrollToBottomLoadMoreListener() {
            @Override
            public void onScrollToBottomLoadMore() {
                isLoadMore = true;
                if (douYinDisable) {
                    HuoshanListData();
                } else {
                    getDouyinListData();
                }
            }
        });


    }

    private void initHeader() {
        header = new MyCustomHeader(getActivity());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.WRAP_CONTENT));
        header.setPadding(0, DensityUtil.dip2px(App.getInstance(), 15), 0, DensityUtil.dip2px(App.getInstance(), 10));

        header.setUp(ptrRecyclerViewUIComponent);
        header.getTvtitle().setText("发现20条精彩视频");

        ptrRecyclerViewUIComponent.setHeaderView(header);

        ptrRecyclerViewUIComponent.setDurationToCloseHeader(600);
        ptrRecyclerViewUIComponent.setLoadingMinTime(1200);

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
                LogUtils.json(response);
                loadFrameLayout.showContentView();
                try {
                    DouyinVideoListData listData = DouyinVideoListData.fromJSONData(response);
                    max_cursor = listData.getMaxCursor();

                    if (listData.getVideoDataList() == null || listData.getVideoDataList().size() == 0) {
                        douYinDisable = true;
                        max_cursor = 0;
                        isLoadMore = false;
                        HuoshanListData();
                        return;
                    } else {
                        douYinDisable = false;
                    }

                    LogUtils.e(listData.getVideoDataList().size());

                    header.getTvtitle().setText("发现" + listData.getVideoDataList().size() + "条精彩视频");
                    ptrRecyclerViewUIComponent.removeView(header);
                    ptrRecyclerViewUIComponent.setHeaderView(header);

                    if (isLoadMore) {
                        mList.addAll(listData.getVideoDataList());
                        adapter.setDataList(mList, false);
                        mAdapter.notifyDataSetChanged();
                        ptrRecyclerViewUIComponent.loadMoreComplete(true);

                    } else {
                        mList = listData.getVideoDataList();
                        if (mList.size() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            ptrRecyclerViewUIComponent.setLoadMoreEnable(false);
                        } else {
                            emptyView.setVisibility(View.GONE);
                            ptrRecyclerViewUIComponent.setLoadMoreEnable(true);
                        }

                        adapter.setDataList(mList);
                        mAdapter.notifyDataSetChanged();
                        ptrRecyclerViewUIComponent.refreshComplete();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Request request, IOException e) {
                ptrRecyclerViewUIComponent.loadMoreComplete(false);
                ptrRecyclerViewUIComponent.refreshComplete();
                ToastUtil.showToast("网络连接失败");
                header.getTvtitle().setText("网络连接失败，请重试");
                ptrRecyclerViewUIComponent.removeView(header);
                ptrRecyclerViewUIComponent.setHeaderView(header);

                loadFrameLayout.showErrorView();

            }
        });


    }


    public void HuoshanListData() {
        String url = HotsoonUtils.getEncryptUrl(getActivity(), 0, max_cursor);
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String response) {
                LogUtils.json(response);
                try {
                    HotsoonVideoListData listData = HotsoonVideoListData.fromJSONData(response);
                    max_cursor = listData.getMaxTime();

                    LogUtils.e(listData.getVideoDataList().size());

                    if (isLoadMore) {
                        mList.addAll(listData.getVideoDataList());
                        adapter.setDataList(mList, false);
                        mAdapter.notifyDataSetChanged();
                        ptrRecyclerViewUIComponent.loadMoreComplete(true);

                    } else {
                        mList = listData.getVideoDataList();
                        if (mList.size() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            ptrRecyclerViewUIComponent.setLoadMoreEnable(false);
                        } else {
                            emptyView.setVisibility(View.GONE);
                            ptrRecyclerViewUIComponent.setLoadMoreEnable(true);
                        }

                        adapter.setDataList(mList);
                        mAdapter.notifyDataSetChanged();
                        ptrRecyclerViewUIComponent.refreshComplete();
                    }

                } catch (Exception e) {
                    ptrRecyclerViewUIComponent.refreshComplete();
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Request request, IOException e) {
                ptrRecyclerViewUIComponent.loadMoreComplete(true);
                ptrRecyclerViewUIComponent.refreshComplete();
                ToastUtil.showToast("网络连接失败");

            }
        });


    }

    /**
     * 加载过程中不让点击
     */
    @Override
    public void onItemClick(int position) {
        if (ptrRecyclerViewUIComponent.isLoadingMore() || ptrRecyclerViewUIComponent.isRefreshing()) {
            return;
        }

        Intent intent = new Intent(getActivity(), VerticalVideoActivity.class);
        WeakDataHolder.getInstance().saveData("videoUrlList", mList);

        intent.putExtra("max_cursor", max_cursor);
        intent.putExtra("position", position);
        getActivity().startActivity(intent);

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            StatusBarCompat.translucentStatusBar(getActivity(), true);
        } else {
            StatusBarCompat.setStatusBarColor(getActivity(), 0xfffffff);
            StatusBarFontHelper.setStatusBarMode(getActivity(), true);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getImageData(RefreshEvent event) {
        adapter.setDataList(event.getList());
        mAdapter.notifyDataSetChanged();

        ptrRecyclerViewUIComponent.getRecyclerView().scrollToPosition(event.getPosition());
        max_cursor = event.getMaxCursor();

    }

}
