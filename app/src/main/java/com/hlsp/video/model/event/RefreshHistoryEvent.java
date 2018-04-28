package com.hlsp.video.model.event;

import com.hlsp.video.bean.VideoListItem;

import java.util.List;

/**
 * Created by hackest on 2018/4/26.
 */

public class RefreshHistoryEvent {

    private List<VideoListItem> mList;

    public RefreshHistoryEvent(List<VideoListItem> mList) {
        this.mList = mList;
    }

    public List<VideoListItem> getList() {
        return mList;
    }

    public void setList(List<VideoListItem> mList) {
        this.mList = mList;
    }
}
