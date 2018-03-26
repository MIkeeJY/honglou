package com.hlsp.video.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseLoadFragment;
import com.hlsp.video.utils.DensityUtil;
import com.jack.mc.cyg.cygptr.PtrFrameLayout;
import com.jack.mc.cyg.cygptr.header.MaterialHeader;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.share.jack.cygwidget.recyclerview.PtrRecyclerViewUIComponent;

/**
 * Created by hackest on 2018/3/26.
 */

public class RecommondChildFragment extends BaseLoadFragment {

    @BindView(R.id.am_ptr_framelayout) PtrRecyclerViewUIComponent ptrRecyclerViewUIComponent;
    @BindView(R.id.ar_empty_view) View emptyView;


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

    }
}
