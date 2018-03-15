package com.hlsp.video.statistics;


import com.hlsp.video.utils.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        try {
            json_log_group.put("__source__", mSource);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json_log_group.put("__topic__", mTopic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray log_arrays = new JSONArray();

        for (ServerLog log : mContent) {
            Map<String, Object> map = log.GetContent();
            JSONObject json_log = new JSONObject(map);
            log_arrays.put(json_log);
        }
        try {
            json_log_group.put("__logs__", log_arrays);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = GsonUtil.GsonString(json_log_group);
        return s;
    }

}
