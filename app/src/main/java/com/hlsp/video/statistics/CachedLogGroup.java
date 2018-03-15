package com.hlsp.video.statistics;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by megrez on 2016/12/25.
 */
public class CachedLogGroup extends LogGroup {
    public static final int DefaultCounterThreshold = 10;
    @SuppressWarnings("unused")
    private static final String TAG = CachedLogGroup.class.getSimpleName();
    private String topic;
    private String source;
    private LinkedBlockingQueue<ServerLog> logs;

    public CachedLogGroup(String topic, String source) {
        logs = new LinkedBlockingQueue<ServerLog>();
        this.topic = topic;
        this.source = source;
    }

    @Override
    public void PutTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void PutSource(String source) {
        this.source = source;
    }

    public LogGroup takeOneLogGroup(int threshold) {
        if (logs.size() == 0) {
            return null;
        }
        LogGroup group = new LogGroup(this.topic, this.source);
        if (threshold == 0) {
            ServerLog log;
            while ((log = logs.poll()) != null) {
                group.PutLog(log);
            }
            return group;
        }
        for (int i = 0; i < threshold; i++) {
            ServerLog log = logs.poll();
            if (log == null) {
                break;
            }
            group.PutLog(log);
        }
        return group;
    }

    public void addLogGroup(LogGroup logGroup) {
        for (ServerLog log : logGroup.mContent) {
            logs.offer(log);
        }
    }

    public int getSize() {
        return this.logs.size();
    }

    @Override
    public void PutLog(ServerLog log) {
        logs.offer(log);
    }
}
