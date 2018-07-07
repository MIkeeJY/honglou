package com.hlsp.video.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseLoadFragment;
import com.hlsp.video.ui.main.adapter.RecommondViewHolder;
import com.hlsp.video.utils.DensityUtil;
import com.hlsp.video.widget.MyCustomHeader;
import com.jack.mc.cyg.cygptr.PtrFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cygwidget.recyclerview.PtrRecyclerViewUIComponent;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;

/**
 * 推荐，搞笑，娱乐
 * Created by hackest on 2018/3/26.
 */

public class RecommondChildFragment extends BaseLoadFragment implements CygBaseRecyclerAdapter.OnItemClickListener<RecommondViewHolder> {

    @BindView(R.id.am_ptr_framelayout) PtrRecyclerViewUIComponent ptrRecyclerViewUIComponent;
    @BindView(R.id.ar_empty_view) View emptyView;

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
//        ptrRecyclerViewUIComponent.delayRefresh(100);
    }




    @Override
    public void onItemClick(int position) {

    }




}
