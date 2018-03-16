package com.hlsp.video.bean.data;

public class UpdateData {
    private String currentVersion;
    private String update_version;
    private String update_content;
    private String download_link;
    private String is_update;
    private String is_force_update;


    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getUpdate_version() {
        return update_version;
    }

    public String getUpdate_content() {
        return update_content;
    }

    public String getDownload_link() {
        return download_link;
    }

    public String getIs_update() {
        return is_update;
    }

    public String getIs_force_update() {
        return is_force_update;
    }


    public boolean isForceUpdate() {
        if ("0".equals(is_force_update)) {
            return false;
        } else {
            return true;
        }
    }
}
