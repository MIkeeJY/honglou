package com.hlsp.video.statistics.util;

import java.util.Map;

public class MultiInputStreamHelper {

    private static String getEncoding(Map<String, String> responseHeaders) {
        String header1 = responseHeaders.get("Accept-Encoding");
        String header2 = responseHeaders.get("Content-Encoding");
        StringBuilder encoding = new StringBuilder();
        if (header1 != null) {
            encoding.append(header1);
        }

        if (header2 != null) {
            encoding.append(header2);
        }

        return encoding.toString();
    }

    public static byte[] encrypt(byte[] old) {
        for (int i = 0; i < old.length; i++) {
            old[i] = (byte) ~old[i];
        }
        return old;
    }


    public enum IEncoding {
        NONE(""), ESENC("esenc"), GZIP("gzip"), ESENCGZIP("gzip,esenc");

        public final String encoding;

        IEncoding(String encoding) {
            this.encoding = encoding;
        }
    }

}
