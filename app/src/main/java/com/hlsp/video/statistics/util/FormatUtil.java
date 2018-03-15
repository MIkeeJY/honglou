package com.hlsp.video.statistics.util;

import java.util.Map;
import java.util.Map.Entry;

public class FormatUtil {
    public static String forMatMap(Map<String, String> map) {
        if (map.isEmpty()) {
            return "";
        }
        StringBuffer data = new StringBuffer();
        for (Entry<String, String> entry : map.entrySet()) {
            data.append(entry.getKey()).append(":").append(entry.getValue()).append("`");
        }
        int index = data.lastIndexOf("`");
        if (index == data.length() - 1) {
            data.replace(data.length() - 1, data.length(), "");
        }
        return data.toString();
    }
}
