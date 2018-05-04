package com.hlsp.video.ui.main;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseActivity;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.model.ConstantsValue;
import com.hlsp.video.model.event.ClearListEvent;
import com.hlsp.video.model.event.DelListEvent;
import com.hlsp.video.model.event.RefreshHistoryEvent;
import com.hlsp.video.ui.main.adapter.EditHistoryVideoAdapter;
import com.hlsp.video.ui.main.adapter.EditHistoryViewHolder;
import com.hlsp.video.utils.FileUtils;
import com.hlsp.video.utils.StatusBarCompat;
import com.hlsp.video.utils.statusbar.StatusBarFontHelper;
import com.hlsp.video.widget.CommonDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.titlebar.TitleBarUIComponent;

/**
 * 浏览足迹
 * Created by hackest on 2018/4/25.
 */

public class FootMarkActivity extends BaseActivity implements CygBaseRecyclerAdapter.OnItemClickListener<EditHistoryViewHolder> {


    @BindView(R.id.titlebar) TitleBarUIComponent titlebar;
    @BindView(R.id.rv_history) RecyclerView mRecyclerView;
    @BindView(R.id.ll_bottom) View mBottomView;
    @BindView(R.id.tv_clear) TextView mTvClear;
    @BindView(R.id.tv_finish) TextView mTvFinish;
    @BindView(R.id.iv_shadow) ImageView mShadow;

    @BindView(R.id.ar_empty_view) View emptyView;

    EditHistoryVideoAdapter mAdapter;

    private List<VideoListItem> mList = new ArrayList<>();

    LinearLayoutManager mLayoutManager;
    CommonDialog commonDialog;

    @Override
    protected int layoutRes() {
        return R.layout.activity_foot_mark;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        StatusBarCompat.setStatusBarColor(this, 0xfffffff);
        StatusBarFontHelper.setStatusBarMode(this, true);

        titlebar.initTitle(
                getResources().getString(R.string.foot_mark),
                getResources().getColor(R.color.home_tab_selected_color));


        titlebar.initLeft(R.drawable.nav_icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titlebar.initRight(R.drawable.search_icon_deletehistory, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlebar.getTvRight().setVisibility(View.GONE);
                mBottomView.setVisibility(View.VISIBLE);
                mShadow.setVisibility(View.VISIBLE);

                if (mList != null && mList.size() > 0) {
                    for (VideoListItem item : mList) {
                        item.setSeleted(1);
                    }
                }

                mAdapter.setDataList(mList);


            }
        });

        mList = FileUtils.readParcelableList(App.getInstance(), ConstantsValue.HISTORY_VIDEO, VideoListItem.class);


        if (mList != null && mList.size() > 0) {
            titlebar.getTvRight().setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            for (VideoListItem item : mList) {
                item.setSeleted(0);
            }
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            titlebar.getTvRight().setVisibility(View.GONE);
        }

        mAdapter = new EditHistoryVideoAdapter(this, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        mLayoutManager.setReverseLayout(true);//列表翻转

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setDataList(mList);


    }

    @OnClick(R.id.tv_clear)
    void clearClick() {
        commonDialog = new CommonDialog(this);
        commonDialog.setCancelable(false);
        commonDialog.setLeftButtonMsg("取消");
        commonDialog.setRightButtonMsg("确认");
        commonDialog.setContentMsg("确定要清空所有浏览足迹吗？");

        commonDialog.setOnLeftButtonOnClickListener(new CommonDialog.LeftButtonOnClickListener() {
            @Override
            public void onLeftButtonOnClick() {
                if (!isFinishing()) {
                    commonDialog.dismiss();
                }

            }
        });
        commonDialog.setOnRightButtonOnClickListener(new CommonDialog.RightButtonOnClickListener() {
            @Override
            public void onRightButtonOnClick() {
                mList.clear();
                FileUtils.writeParcelableList(App.getInstance(), ConstantsValue.HISTORY_VIDEO, mList);
                EventBus.getDefault().post(new ClearListEvent());

                if (!isFinishing()) {
                    commonDialog.dismiss();
                }
                finish();
            }
        });
        commonDialog.show();


    }

    @OnClick(R.id.tv_finish)
    void finishClick() {
        titlebar.getTvRight().setVisibility(View.VISIBLE);
        mBottomView.setVisibility(View.GONE);
        mShadow.setVisibility(View.GONE);

        if (mList != null && mList.size() > 0) {
            titlebar.getTvRight().setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            for (VideoListItem item : mList) {
                item.setSeleted(0);
            }
            mAdapter.setDataList(mList);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            titlebar.getTvRight().setVisibility(View.GONE);
        }

        FileUtils.writeParcelableList(App.getInstance(), ConstantsValue.HISTORY_VIDEO, mList);
        EventBus.getDefault().post(new RefreshHistoryEvent(mList));

    }

    @Override
    public void onItemClick(int position) {
        if (mList.get(position).getSeleted() == 1) {
//            mList.remove(position);
//            mAdapter.setDataList(mList);
        } else {
            Intent intent = new Intent(this, HistoryDetailActivity.class);
            intent.putExtra("VideoListItem", mList.get(position));
            startActivity(intent);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commonDialog != null && commonDialog.isShowing()) {
            commonDialog.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getHistory(DelListEvent event) {
        mList.remove(event.getPosition());
        mAdapter.setDataList(mList);
    }

}
