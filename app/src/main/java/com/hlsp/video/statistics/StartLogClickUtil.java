package com.hlsp.video.statistics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.hlsp.video.App;
import com.hlsp.video.statistics.appender.AndroidLogClient;
import com.hlsp.video.statistics.common.PLItemKey;
import com.hlsp.video.statistics.util.Constants;
import com.hlsp.video.statistics.util.FormatUtil;
import com.hlsp.video.utils.NetworkUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017\8\14 0014.
 */

public class StartLogClickUtil {
    public static final String TAG = "StartLogClickUtil";

    //页面编码
    public static final String PAGE_PORTRAIT_LIST = "page_portrait_list";//小视频
    public static final String MAIN_PAGE = "MAIN";//主页
    public static final String SEARCH_PAGE = "SEARCH";//搜索页
    public static final String SEARCHRESULT_PAGE = "SEARCHRESULT";//搜索结果页
    public static final String SHELF_PAGE = "SHELF";//书架页
    public static final String SHELFEDIT_PAGE = "SHELFEDIT";//书架编辑页
    public static final String CHCHEEDIT_PAGE = "CHCHEEDIT";//缓存编辑页
    public static final String CACHEMANAGE_PAGE = "CACHEMANAGE";//缓存管理页
    public static final String BOOOKDETAIL_PAGE = "BOOOKDETAIL";//书籍详情页
    public static final String PEASONAL_PAGE = "PERSONAL";//个人中心页
    public static final String MORESET_PAGE = "MORESET";//更多设置
    public static final String READPAGE_PAGE = "READPAGE";//阅读页
    public static final String READPAGESET_PAGE = "READPAGESET";//阅读页设置
    public static final String READPAGEMORE_PAGE = "READPAGEMORE";//阅读页更多
    public static final String RECOMMEND_PAGE = "RECOMMEND";//青果推荐页
    public static final String TOP_PAGE = "TOP";//榜单页
    public static final String CLASS_PAGE = "CLASS";//分类页
    public static final String FIRSTCLASS_PAGE = "FIRSTCLASS";//分类一级页面的搜索
    public static final String FIRSTTOP_PAGE = "FIRSTTOP";//榜单一级页面的搜索
    public static final String FIRSTRECOMMEND_PAGE = "FIRSTRECOMMEND";//推荐一级页面的搜索
    public static final String BOOKCATALOG = "BOOKCATALOG";//书籍目录页
    public static final String PROCTCOL_PAGE = "PROCTCOL";//使用协议
    public static final String PERHELP_PAGE = "PERHELP";//帮助与反馈
    public static final String PERHISTORY_PAGE = "PERHISTORY";//浏览足迹
    public static final String BOOKENDPAGE_PAGE = "BOOKENDPAGE";//书籍end页
    public static final String AUTHORPAGE_PAGE = "AUTHORPAGE";//作者主页


    //APP通用
    public static final String APPINIT = "APPINIT";//客户端启动
    public static final String HOME = "HOME";//切换至后台
    public static final String ACTIVATE = "ACTIVATE";//切换至前台
    public static final String BACK = "BACK";//返回
    public static final String SCREENSCROLL = "SCREENSCROLL";//屏幕滑动
    public static final String CASHERESULT = "CASHERESULT";//缓存结果
    public static final String SYSTEM_SEARCHRESULT = "SEARCHRESULT";//被动搜索结果


    //主页
    public static final String SEARCH_PORTAIT_LIST = "search_portait_list"; //搜索-竖屏视频
    public static final String RECOMMEND = "RECOMMEND";
    public static final String TOP = "TOP";
    public static final String CLASS = "CLASS";
    public static final String PERSONAL = "PERSONAL";
    public static final String SEARCH = "SEARCH";
    public static final String BOOKLIST = "BOOKLIST";





    public static final String VERSION = "VERSION";//点击当前版本



    private static final ExecutorService logThreadPool = Executors.newSingleThreadExecutor();
    private static List<String> prePageList = new ArrayList<>();

    private static List<ServerLog> linkList = new LinkedList<ServerLog>();

    //上传普通的点击事件
    public static void upLoadEventLog(String pageCode, String identify) {

        final ServerLog log = getCommonLog();


        log.PutContent("code", identify);//点击事件唯一标识
        log.PutContent("page_code", pageCode);
        log.PutContent("pre_page_code", getPrePageCode(pageCode));


//        AppLog.e("log", log.GetContent().toString());
//        if (identify.equals(APPINIT)) log.setEventType(ServerLog.MINORITY);
//
//        AndroidLogStorage.getInstance().accept(log);

        logThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LogUtils.tag(TAG).e(log.GetContent().toString());
                AndroidLogClient.putLog(log);
            }
        });

    }

    public static void sendDirectLog(PLItemKey key, String page, String identify, Map<String, String> params) {
        LogGroup logGroup = new LogGroup("", "", key.getProject(), PLItemKey.ZN_APP_EVENT.getLogstore());
        ServerLog log = getCommonLog();
        log.PutContent("code", identify);//点击事件唯一标识
        log.PutContent("page_code", page);

        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            log.PutContent(entry.getKey(), entry.getValue());
        }

        logGroup.PutLog(log);

        LOGClient logClient = new LOGClient(AndroidLogClient.endPoint, AndroidLogClient.accessKeyId, AndroidLogClient.accessKeySecret, key.getProject());
        try {
            long start = System.currentTimeMillis();
            logClient.PostLog(logGroup, key.getLogstore());
            Log.i("upload-Log", "useTime : " + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @NonNull
    private static ServerLog getCommonLog() {
        final ServerLog log = new ServerLog(PLItemKey.ZN_APP_EVENT);

        log.PutContent("project", PLItemKey.ZN_APP_EVENT.getProject());
        log.PutContent("logstore", PLItemKey.ZN_APP_EVENT.getLogstore());


        log.PutContent("os", "android");//手机操作系统
        log.PutContent("log_time", System.currentTimeMillis() + "");//日志产生时间（毫秒数）
        log.PutContent("network", NetworkUtil.getNetworkType(App.getInstance()));//网络状况
        log.PutContent("longitude", Constants.longitude + "");//经度
        log.PutContent("latitude", Constants.latitude + "");//纬度
        log.PutContent("city_info", Constants.adCityInfo);//城市
        log.PutContent("location_detail", Constants.adLocationDetail);//具体位置信息
        return log;
    }

    //上传普通的点击事件,带事件参数
    public static void upLoadEventLog(Context context, String pageCode, String identify, Map<String, String> extraParam) {
        if (!Constants.dy_ad_new_statistics_switch || context == null) {
            return;
        }
        final ServerLog log = new ServerLog(PLItemKey.ZN_APP_EVENT);

        log.PutContent("project", PLItemKey.ZN_APP_EVENT.getProject());
        log.PutContent("logstore", PLItemKey.ZN_APP_EVENT.getLogstore());
        log.PutContent("code", identify);//点击事件唯一标识
        log.PutContent("page_code", pageCode);


        log.PutContent("os", "android");//手机操作系统
        log.PutContent("log_time", System.currentTimeMillis() + "");//日志产生时间（毫秒数）
        log.PutContent("network", NetworkUtil.getNetworkType(App.getInstance()));//网络状况
        log.PutContent("longitude", Constants.longitude + "");//经度
        log.PutContent("latitude", Constants.latitude + "");//纬度
        log.PutContent("city_info", Constants.adCityInfo);//城市
        log.PutContent("location_detail", Constants.adLocationDetail);//具体位置信息
        log.PutContent("pre_page_code", getPrePageCode(pageCode));

        //事件对应的额外的参数部分

        if (extraParam != null) {
            log.PutContent("data", FormatUtil.forMatMap(extraParam));
        }


        logThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LogUtils.tag(TAG).e("log", log.GetContent().toString());
                AndroidLogClient.putLog(log);
            }
        });

    }

    //上传用户App列表
    public static void upLoadApps(Context context, String applist) {
        if (!Constants.dy_ad_new_statistics_switch) {
            return;
        }
        final ServerLog log = new ServerLog(PLItemKey.ZN_APP_APPSTORE);

        log.PutContent("uid", "");

        log.PutContent("apps", applist);
        log.PutContent("time", System.currentTimeMillis() + "");

        logThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LogUtils.tag(TAG).e("log", log.GetContent().toString());
                AndroidLogClient.putLog(log);
            }
        });


    }


    //上传用户阅读内容(传参按此格式顺序)
    public static void upLoadReadContent(String... params) {
        if (!Constants.dy_ad_new_statistics_switch || !Constants.dy_readPage_statistics_switch) {
            return;
        }
        ServerLog log = new ServerLog(PLItemKey.ZN_APP_READ_CONTENT);

        log.PutContent("uid", "");
        if (params != null) {
            log.PutContent("book_id", params[0]);//书籍唯一字符串
            log.PutContent("chapter_id", params[1]);//阅读章节唯一字符串
            log.PutContent("source_ids", params[2]);//使用书籍源，中间有切换源则多个源使用分隔符"`"进行连接，尽量准确获取（不丢数据）
            log.PutContent("page_num", params[3]);//当前阅读章节被切分的总页数
            log.PutContent("pager", params[4]);//章节页数索引，即当前为第几页
            log.PutContent("page_size", params[5]);//当前页尺寸，可以是byte或总字数（包括所有字符，需要知道当前页内容）
            log.PutContent("from", params[6]);//当前页面来源，所有可能来源的映射唯一字符串。书籍封面/书架/上一页翻页等等（不包括退出App后在进入来源）
            log.PutContent("begin_time", params[7]);//进入当前页时间戳（秒数）
            log.PutContent("end_time", params[8]);//退出当前页时间戳（秒数）（不包括用户退出App在进来，即该时间表示为用户主动翻页和主动退出阅读）
            log.PutContent("read_time", params[9]);//总阅读时长秒数（考虑中间退出App的时长不应该包括进来，即排除打电话等时间）
            log.PutContent("has_exit", params[10]);//是否有阅读中间退出行为
            log.PutContent("channel_code", params[11]);//书籍来源1为青果，2为智能
            log.PutContent("lon", Constants.longitude + "");//经度
            log.PutContent("lat", Constants.latitude + "");//纬度

        }
        linkList.add(log);
        LogUtils.tag(TAG).e("log", log.GetContent().toString());
        if (linkList != null && linkList.size() > 10) {
            logThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < linkList.size(); i++) {
                        AndroidLogClient.putLog(linkList.get(i));
                    }
                    linkList.clear();
                }
            });

        }

    }



    private static String decode(String content) {
        if (content == null || "".equals(content)) {
            return "";
        }
        try {
            return URLDecoder.decode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取prePageCode
    public synchronized static String getPrePageCode(String pageCode) {
        String pre_page_code = "";

        if (prePageList.size() == 0 || (prePageList.size() > 0 && pageCode != null && !prePageList.get(prePageList.size() - 1).equals(pageCode))) {
            prePageList.add(pageCode);
            removePre(prePageList);
        }
        if (prePageList != null && prePageList.size() != 0) {

            for (int i = 0; i < prePageList.size(); i++) {
                LogUtils.e(prePageList.get(i));
            }

            if (prePageList.size() > 1) {
                pre_page_code = prePageList.get(prePageList.size() - 2);
            } else {
                pre_page_code = prePageList.get(prePageList.size() - 1);
            }

        } else {
            pre_page_code = "";
        }
        return pre_page_code;
    }

    public static void removePre(List<String> prePageList) {

        if (prePageList.size() > 6) {
            for (int i = 0; i < 2; i++)
                prePageList.remove(prePageList.get(i));
        }

    }

}
