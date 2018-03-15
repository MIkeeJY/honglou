package com.hlsp.video.statistics.common;

/**
 * 针对project和logstore 指定一个对应的编码，用来存储使用
 */
public enum PLItemKey {
    ZN_API_SEARCH("ZN_API_SEARCH", "datastatistics-zn", "statistics_search", "智能api部门搜索相关统计"),
    ZN_APP_ANROID_SEARCH("ZN_APP_ANROID_SEARCH", "datastatistics-zn", "statistics_search", "智能anroid_app部门搜索相关统计"),
    ZN_DF_EDITION_SET_DEFAULT_CONTENT("ZN_DF_EDITION_SET_DEFAULT_CONTENT", "datastatistics-zn", "df_edition_set_default_content", "智能数据流相关统计"),
    ZN_DF_EDITION_EXCEPTION_EVENT("ZN_DF_EDITION_EXCEPTION_EVENT", "datastatistics-zn", "df_edition_exception_event", "智能数据流相关统计"),
    ZN_DF_EDITION_SERIAL_BUT_STOP("ZN_DF_EDITION_SERIAL_BUT_STOP", "datastatistics-zn", "df_edition_serial_but_stop", "智能数据流相关统计"),
    ZN_DF_ONLINE_RULE_RESP("ZN_DF_ONLINE_RULE_RESP", "datastatistics-zn", "df_online_rule_resp", "智能数据流相关统计"),
    ZN_DF_NEW_EDITION_MAPPING("ZN_DF_NEW_EDITION_MAPPING", "datastatistics-zn", "df_new_edition_mapping", "智能数据流相关统计"),
    ZN_DF_DOWN_CONTENT_EMPTY("ZN_DF_DOWN_CONTENT_EMPTY", "datastatistics-zn", "df_down_content_empty", "智能数据流相关统计"),
    ZN_DF_EBS_NO_MP("ZN_DF_EBS_NO_MP", "datastatistics-zn", "df_ebs_no_mp", "智能数据流相关统计"),
    ZN_DF_ERR_HB_SYNC("ZN_DF_ERR_HB_SYNC", "datastatistics-zn", "df_err_hb_sync", "智能数据流相关统计"),
    ZN_DF_OK_SYNC("ZN_DF_OK_SYNC", "datastatistics-zn", "df_ok_sync", "智能数据流相关统计"),
    ZN_DF_OK_ED_CU("ZN_DF_OK_ED_CU", "datastatistics-zn", "df_ok_ed_cu", "智能数据流相关统计"),
    ZN_DF_OK_HOT_ONLIN("ZN_DF_OK_HOT_ONLIN", "datastatistics-zn", "df_ok_hot_online", "智能数据流相关统计"),
    ZN_DF_OK_EDITION_FIND("ZN_DF_OK_EDITION_FIND", "datastatistics-zn", "df_ok_edition_find", "智能数据流相关统计"),
    ZN_DF_EDITION_CHAPTER_UPDATE("ZN_DF_EDITION_CHAPTER_UPDATE", "datastatistics-zn", "df_edition_chapter_update", "智能数据流相关统计"),
    ZN_APP_EVENT("ZN_DF_APP_EVENT", "datastatistics-zn", "event", "智能数据流APP点击事件"),
    ZN_APP_CRASH("ZN_APP_CRASH", "datastatistics-zn", "app_crash", "客户端异常日志"),
    ZN_APP_APPSTORE("ZN_APP_APPSTORE", "datastatistics-zn", "appstore", "app列表"),
    ZN_APP_READ_CONTENT("ZN_APP_READ_CONTENT", "datastatistics-zn", "read_content", "阅读内容"),
    ZN_APP_TEST("ZN_APP_TEST", "datastatistics-zn", "test", "智能数据流APP点击事件"),
    ZN_APP_FEEDBACK("ZN_APP_FEEDBACK", "datastatistics-zn", "feedback", "客户端问题章节反馈");
    private String key;
    private String desc;
    private String project;
    private String logstore;

    private PLItemKey(String key, String project, String logstore, String desc) {
        this.key = key;
        this.project = project;
        this.logstore = logstore;
        this.desc = desc;
    }

    public static PLItemKey getKey(String key) {
        PLItemKey itemKey = null;
        for (PLItemKey item : PLItemKey.values()) {
            if (key.equalsIgnoreCase(item.getKey())) {
                itemKey = item;
                break;
            }
        }
        return itemKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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


}
