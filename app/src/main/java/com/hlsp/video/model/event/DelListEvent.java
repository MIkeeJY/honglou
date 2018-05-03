package com.hlsp.video.model.event;

/**
 * Created by hackest on 2018/5/3.
 */

public class DelListEvent {
    int position;

    public DelListEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
