package com.hlsp.video.bean;

/**
 * 统计事件先关实体
 * Created by hackest on 2018/2/8.
 */

public class EventEntity {

    private String eventId;
    private String eventName;

    public EventEntity(String eventId, String eventName) {
        this.eventId = eventId;
        this.eventName = eventName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
