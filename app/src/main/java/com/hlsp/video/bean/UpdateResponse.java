package com.hlsp.video.bean;

import com.hlsp.video.bean.data.UpdateData;

/**
 * Created by hackest on 2018/3/16.
 */

public class UpdateResponse {

    /**
     * data : {"currentVersion":"2.2.2","update_version":"2.2.2","update_content":"修改视频重复BUG，加快内容加载速度","download_link":"http://openbox.mobilem.360.cn/index/d/sid/3120307","is_update":"1","is_force_update":"0"}
     * msg : 111111
     * code : 000000
     */

    private UpdateData data;
    private String msg;
    private String code;

    public UpdateData getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }
}
