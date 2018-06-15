package com.hlsp.video.ui.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseActivity;
import com.hlsp.video.bean.AuthorInfoResponse;
import com.hlsp.video.bean.AuthorVideo;
import com.hlsp.video.bean.AuthorVideoResponse;
import com.hlsp.video.bean.VideoListItem;
import com.hlsp.video.model.author.AuthorModel;
import com.hlsp.video.ui.main.adapter.OtherProfileAdapter;
import com.hlsp.video.ui.main.adapter.OtherProfileViewHolder;
import com.hlsp.video.utils.GlideUtils;
import com.hlsp.video.utils.StatusBarCompat;
import com.hlsp.video.utils.statusbar.StatusBarFontHelper;
import com.hlsp.video.view.CircleImageView;
import com.jack.mc.cyg.cygptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.share.jack.cyghttp.callback.CygBaseObserver;
import cn.share.jack.cygwidget.loadmore.OnScrollToBottomLoadMoreListener;
import cn.share.jack.cygwidget.recyclerview.PtrRecyclerViewUIComponent;
import cn.share.jack.cygwidget.recyclerview.adapter.CygBaseRecyclerAdapter;
import cn.share.jack.cygwidget.titlebar.TitleBarUIComponent;

/**
 * 他人详情页
 */
public class OtherProfileActivity extends BaseActivity implements CygBaseRecyclerAdapter.OnItemClickListener<OtherProfileViewHolder> {

    @BindView(R.id.titlebar) TitleBarUIComponent titlebar;
    @BindView(R.id.am_ptr_framelayout) PtrRecyclerViewUIComponent ptrRecyclerViewUIComponent;

    @BindView(R.id.iv_user_avatar) CircleImageView mIvUserAvatar;
    @BindView(R.id.tv_user) TextView mTvUser;

    String authorId;

    OtherProfileAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;

    int page = 1;
    int size = 10;

    List<AuthorVideo> mList = new ArrayList<>();

    boolean isLoadMore;

    Handler mHandler = new Handler();

    AuthorInfoResponse authorInfoResponse;

    @Override
    protected int layoutRes() {
        return R.layout.activity_other_profile;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, 0xfffffff);
        StatusBarFontHelper.setStatusBarMode(this, true);

        titlebar.initLeft(R.drawable.nav_icon_back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        authorId = getIntent().getStringExtra("authorId");


        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.isHeader(position) || mAdapter.isFooter(position)) ? gridLayoutManager.getSpanCount() : 1;
            }
        });

        ptrRecyclerViewUIComponent.setLayoutManager(gridLayoutManager);
        ptrRecyclerViewUIComponent.setCanRefresh(false);

        adapter = new OtherProfileAdapter(this, this);
        mAdapter = new RecyclerAdapterWithHF(adapter);

        ptrRecyclerViewUIComponent.setLayoutManager(gridLayoutManager);
        ptrRecyclerViewUIComponent.setAdapter(mAdapter);
        ptrRecyclerViewUIComponent.setLoadMoreEnable(true);


        ptrRecyclerViewUIComponent.setOnScrollToBottomLoadMoreListener(new OnScrollToBottomLoadMoreListener() {
            @Override
            public void onScrollToBottomLoadMore() {
                isLoadMore = true;
                getAuthorVideoList(authorId);
            }
        });

        getAuthorInfoData(authorId);
        getAuthorVideoList(authorId);
    }

    private void getAuthorInfoData(String authorId) {
        AuthorModel.getInstance().executeAuthorInfo(authorId, new CygBaseObserver<AuthorInfoResponse>() {
            @Override
            protected void onBaseNext(AuthorInfoResponse data) {
                if (data == null) {
                    return;
                }

                authorInfoResponse = data;

                if (data.getAurthor_img() != null && data.getAurthor_img().size() > 0 && !TextUtils.isEmpty(data.getAurthor_img().get(0).getImg_url())) {
                    GlideUtils.loadImage(App.getInstance(), data.getAurthor_img().get(0).getImg_url(), mIvUserAvatar, null, R.color.black, R.color.black);

                }
                mTvUser.setText(data.getName());

            }
        });

    }

    private void getAuthorVideoList(String authorId) {
        AuthorModel.getInstance().executeAuthorVideoList(authorId, page + "", 10 + "", new CygBaseObserver<AuthorVideoResponse>() {
            @Override
            protected void onBaseNext(AuthorVideoResponse data) {
                page++;
                if (isLoadMore) {
                    mList.addAll(data.getList());
                    adapter.setDataList(mList, false);
                    mAdapter.notifyDataSetChanged();
                    ptrRecyclerViewUIComponent.loadMoreComplete(true);

                } else {
                    mList = data.getList();
                    adapter.setDataList(mList);
                    mAdapter.notifyDataSetChanged();
                    ptrRecyclerViewUIComponent.refreshComplete();
                }

                if ("false".equals(data.getHasMore())) {
                    ptrRecyclerViewUIComponent.loadMoreComplete(false);

                } else {
                    ptrRecyclerViewUIComponent.loadMoreComplete(true);
                }

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
        AuthorVideo video = mList.get(position);

        VideoListItem item = new VideoListItem();
        item.setVideo_coverURL(video.getVideo_coverURL());
        item.setVideo_duration(0);
        item.setVideo_name(video.getVideo_name());
        item.setVideo_source(video.getVideo_source());
        if (!TextUtils.isEmpty(authorInfoResponse.getAurthor_img().get(0).getImg_url())) {
            item.setVideo_author_avatarURL(authorInfoResponse.getAurthor_img().get(0).getImg_url());
        }
        item.setVideo_playURL(video.getVideo_playURL());
        item.setVideo_author_name(video.getVideo_author_name());

        Intent intent = new Intent(this, HistoryDetailActivity.class);
        intent.putExtra("VideoListItem", item);
        startActivity(intent);

    }
}
