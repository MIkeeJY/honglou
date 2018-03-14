package com.hyxsp.video.statistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangjwchn on 16/8/2.
 */

public class LogGroup {
    protected List<ServerLog> mContent = new ArrayList<ServerLog>();
    private String mTopic = "";
    private String mSource = "";
    private String project;
    private String logstore;

    public LogGroup() {
    }

    public LogGroup(String topic, String source, String project, String logstore) {
        mTopic = topic;
        mSource = source;
        this.project = project;
        this.logstore = logstore;
    }

    public LogGroup(String topic, String source) {
        mTopic = topic;
        mSource = source;
    }

    public List<ServerLog> getLogs() {
        return mContent;
    }

    public void PutTopic(String topic) {
        mTopic = topic;
    }

    public void PutSource(String source) {
        mSource = source;
    }

    public void PutLog(ServerLog log) {
        mContent.add(log);
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getLogstore() {
        return logstore;
    }

    public void setLogstore(String logstore) {
        this.logstore = logstore;
    }

    public String LogGroupToJsonString() {
        JSONObject json_log_group = new JSONObject();
        json_log_group.put("__source__", mSource);
        json_log_group.put("__topic__", mTopic);
        JSONArray log_arrays = new JSONArray();

        for (ServerLog log : mContent) {
            Map<String, Object> map = log.GetContent();
            JSONObject json_log = new JSONObject(map);
            log_arrays.add(json_log);
        }
        json_log_group.put("__logs__", log_arrays);
        String s = json_log_group.toJSONString();
        return s;
    }

}
