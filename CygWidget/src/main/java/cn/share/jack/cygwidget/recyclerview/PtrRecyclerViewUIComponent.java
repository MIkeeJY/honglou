package cn.share.jack.cygwidget.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.jack.mc.cyg.cygptr.PtrClassicFrameLayout;
import com.jack.mc.cyg.cygptr.PtrDefaultHandler;
import com.jack.mc.cyg.cygptr.PtrFrameLayout;
import com.jack.mc.cyg.cygptr.loadmore.OnLoadMoreListener;

import cn.share.jack.cygwidget.R;
import cn.share.jack.cygwidget.loadmore.OnScrollToBottomLoadMoreListener;
import cn.share.jack.cygwidget.recyclerview.divider.RecyclerViewDivider;
import cn.share.jack.cygwidget.refersh.OnPullToRefreshListener;
import cn.share.jack.cygwidget.utils.CygView;


/**
 *
 */

public class PtrRecyclerViewUIComponent extends PtrClassicFrameLayout {

    private boolean canRefresh = true;

    public PtrRecyclerViewUIComponent(Context context) {
        super(context);
    }

    public PtrRecyclerViewUIComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrRecyclerViewUIComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private RecyclerViewWithEV mRecyclerView;

    private OnScrollToBottomLoadMoreListener mOnScrollToBottomLoadMoreListener;

    private OnPullToRefreshListener mOnPullToRefreshListener;

    public void setOnScrollToBottomLoadMoreListener(OnScrollToBottomLoadMoreListener mOnScrollToBottomLoadMoreListener) {
        this.mOnScrollToBottomLoadMoreListener = mOnScrollToBottomLoadMoreListener;
    }

    public void setOnPullToRefreshListener(OnPullToRefreshListener mOnPullToRefreshListener) {
        this.mOnPullToRefreshListener = mOnPullToRefreshListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        mRecyclerView = (RecyclerViewWithEV) findViewById(R.id.recyclerview_uicomponent);
        mRecyclerView.getItemAnimator().setChangeDuration(0);

        setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mOnPullToRefreshListener.onPullToRefresh();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (!canRefresh) {
                    return false;
                }
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }
        });

        setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mOnScrollToBottomLoadMoreListener.onScrollToBottomLoadMore();
            }
        });
    }

    public <T extends RecyclerView.LayoutManager> void setLayoutManager(T layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public <T extends RecyclerView.Adapter> void setAdapter(T adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    /**
     * 设置emptyView
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mRecyclerView.setEmptyView(emptyView);
    }

    public void setEmptyView(@LayoutRes int emptyResLayout) {
        View emptyView = CygView.inflateLayout(getContext(), emptyResLayout);
        setEmptyView(emptyView);
    }

    /**
     * 添加分割线
     *
     * @param divider 自定义的分割线
     */
    public void setRecyclerViewDivider(RecyclerViewDivider divider) {
        mRecyclerView.addItemDecoration(divider);
    }

    public void delayRefresh(long delayMillis) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRefresh();
            }
        }, delayMillis);
    }

    public RecyclerViewWithEV getRecyclerView() {
        return mRecyclerView;
    }
}
