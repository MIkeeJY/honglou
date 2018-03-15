package com.hlsp.video.statistics;


import com.hlsp.video.App;
import com.hlsp.video.statistics.common.PLItemKey;
import com.hlsp.video.statistics.util.AppUtils;
import com.hlsp.video.statistics.util.DeviceID;
import com.hlsp.video.statistics.util.OpenUDID;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangjwchn on 16/8/2.
 */
public class ServerLog {
    Map<String, Object> mContent = new HashMap<String, Object>();

    //isClickEvent 标识是否是event点击事件
    public ServerLog(PLItemKey type) {
        if (type.equals(PLItemKey.ZN_APP_APPSTORE)) {

            if (!mContent.containsKey("project")) {
                mContent.put("project", PLItemKey.ZN_APP_APPSTORE.getProject());
            }
            if (!mContent.containsKey("logstore")) {
                mContent.put("logstore", PLItemKey.ZN_APP_APPSTORE.getLogstore());
            }
            if (!mContent.containsKey("udid")) {
                mContent.put("udid", OpenUDID.getOpenUDIDInContext(App.getInstance()));//用户ID（唯一,跟设备有关系）
            }

        } else if (type.equals(PLItemKey.ZN_APP_EVENT)) {
            if (!mContent.containsKey("project")) {
                mContent.put("project", PLItemKey.ZN_APP_EVENT.getProject());
            }
            if (!mContent.containsKey("logstore")) {
                mContent.put("logstore", PLItemKey.ZN_APP_EVENT.getLogstore());
            }

            if (!mContent.containsKey("app_package")) {
                mContent.put("app_package", AppUtils.getPackageName());//app包名
            }
            if (!mContent.containsKey("app_version")) {
                mContent.put("app_version", AppUtils.getVersionName());//app版本
            }
            if (!mContent.containsKey("app_version_code")) {
                mContent.put("app_version_code", AppUtils.getVersionCode() + "");//app内部version code
            }
            if (!mContent.containsKey("app_channel_id")) {
                mContent.put("app_channel_id", AppUtils.getChannelId());//app渠道号
            }
            if (!mContent.containsKey("phone_identity")) {
                mContent.put("phone_identity", DeviceID.getOpenUDIDInContext());//手机唯一标识符
            }
            if (!mContent.containsKey("vendor")) {
                mContent.put("vendor", AppUtils.getPhoneBrand() + "," + AppUtils.getPhoneModel() + "," + AppUtils.getRelease());//设备信息
            }
            if (!mContent.containsKey("operator")) {
                mContent.put("operator", AppUtils.getProvidersName(App.getInstance()));//运营商
            }
            if (!mContent.containsKey("resolution_ratio")) {
                mContent.put("resolution_ratio", AppUtils.getMetrics(App.getInstance()));//分辨率
            }
            if (!mContent.containsKey("udid")) {
                mContent.put("udid", OpenUDID.getOpenUDIDInContext(App.getInstance()));//用户ID（唯一,跟设备有关系）
            }

        } else if (type.equals(PLItemKey.ZN_APP_READ_CONTENT)) {
            if (!mContent.containsKey("udid")) {
                mContent.put("udid", OpenUDID.getOpenUDIDInContext(App.getInstance()));//用户ID（唯一,跟设备有关系）
            }
            if (!mContent.containsKey("project")) {
                mContent.put("project", PLItemKey.ZN_APP_READ_CONTENT.getProject());
            }
            if (!mContent.containsKey("logstore")) {
                mContent.put("logstore", PLItemKey.ZN_APP_READ_CONTENT.getLogstore());
            }

            if (!mContent.containsKey("app_package")) {
                mContent.put("app_package", AppUtils.getPackageName());//app包名
            }
            if (!mContent.containsKey("app_version")) {
                mContent.put("app_version", AppUtils.getVersionName());//app版本
            }
            if (!mContent.containsKey("app_version_code")) {
                mContent.put("app_version_code", AppUtils.getVersionCode() + "");//app内部version code
            }
            if (!mContent.containsKey("app_channel_id")) {
                mContent.put("app_channel_id", AppUtils.getChannelId());//app渠道号
            }
        } else if (type.equals(PLItemKey.ZN_APP_FEEDBACK)) {
            if (!mContent.containsKey("project")) {
                mContent.put("project", PLItemKey.ZN_APP_FEEDBACK.getProject());
            }
            if (!mContent.containsKey("logstore")) {
                mContent.put("logstore", PLItemKey.ZN_APP_FEEDBACK.getLogstore());
            }
            if (!mContent.containsKey("phone_identity")) {
                mContent.put("phone_identity", DeviceID.getOpenUDIDInContext());//手机唯一标识符
            }
            if (!mContent.containsKey("vendor")) {
                mContent.put("vendor", AppUtils.getPhoneBrand() + "," + AppUtils.getPhoneModel() + "," + AppUtils.getRelease());//设备信息
            }
            if (!mContent.containsKey("operator")) {
                mContent.put("operator", AppUtils.getProvidersName(App.getInstance()));//运营商
            }
            if (!mContent.containsKey("resolution_ratio")) {
                mContent.put("resolution_ratio", AppUtils.getMetrics(App.getInstance()));//分辨率
            }

        }

        mContent.put("__time__", new Long(System.currentTimeMillis() / 1000).intValue());
    }

    public void PutTime(int time) {
        mContent.put("__time__", time);
    }

    public void PutContent(String key, String value) {
        if (key == null || key.isEmpty()) {
            return;
        }
        if (value == null) {
            mContent.put(key, "");
        } else {
            mContent.put(key, value);
        }
    }

    public Map<String, Object> GetContent() {
        return mContent;
    }
}
