package com.jack.mc.cyg.cygptr.loadmore;

import android.view.View;
import android.view.View.OnClickListener;


public interface LoadMoreHandler {

    /**
     * @param contentView
     * @param loadMoreView
     * @param onClickLoadMoreListener
     * @return 是否有 initJPush ILoadMoreView
     */
    public boolean handleSetAdapter(View contentView, ILoadMoreViewFactory.ILoadMoreView loadMoreView, OnClickListener
            onClickLoadMoreListener);

    public void setOnScrollBottomListener(View contentView, OnScrollBottomListener onScrollBottomListener);

    void removeFooter();
    void addFooter();
}
