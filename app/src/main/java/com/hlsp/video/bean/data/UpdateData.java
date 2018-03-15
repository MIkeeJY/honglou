package com.hlsp.video.bean.data;

public class UpdateData {
    private boolean force_update;
    private String version_name;
    private String version_code;
    private String download_uri;
    private String description;

    public boolean isForce_update() {
        return force_update;
    }

    public String getVersion_name() {
        return version_name;
    }

    public String getVersion_code() {
        return version_code;
    }

    public String getDownload_uri() {
        return download_uri;
    }

    public String getDescription() {
        return description;
    }
}
