package com.hlsp.video.widget.tablayout;

public interface OnTabSelectListener {

    /**
     * @return 如果返回true表示消费这个事件, 下面的动作不再处理
     */
    boolean onTabSelect(int position);

    void onTabReselect(int position);
}