package com.hlsp.video.ui.fragment;

import android.view.View;

import com.hlsp.video.R;
import com.hlsp.video.base.BaseFragment;

import butterknife.ButterKnife;


/**
 * Created by hackest on 2018-02-01.
 */

public class MineFragment extends BaseFragment {


    @Override
    protected int layoutRes() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onViewReallyCreated(View view) {
        mUnbinder = ButterKnife.bind(this, view);


    }


}
