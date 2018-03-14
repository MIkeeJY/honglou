package com.hyxsp.video.statistics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.hyxsp.video.App;
import com.hyxsp.video.statistics.appender.AndroidLogClient;
import com.hyxsp.video.statistics.common.PLItemKey;
import com.hyxsp.video.statistics.util.Constants;
import com.hyxsp.video.statistics.util.FormatUtil;
import com.hyxsp.video.utils.NetworkUtil;

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
    public static final String SYSTEM_PAGE = "SYSTEM";//APP通用
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
    public static final String BOOKSHELF = "BOOKSHELF";
    public static final String RECOMMEND = "RECOMMEND";
    public static final String TOP = "TOP";
    public static final String CLASS = "CLASS";
    public static final String PERSONAL = "PERSONAL";
    public static final String SEARCH = "SEARCH";
    public static final String BOOKLIST = "BOOKLIST";


    //书架页
    public static final String MORE = "MORE";//点击书架上方更多
    public static final String CACHEMANAGE = "CACHEMANAGE";//点击书架上方更多内缓存管理
    public static final String CACHEEDIT = "CACHEEDIT";//点击缓存管理内缓存编辑(UI优化免费全本小说书城没有此选项)
    public static final String BOOKSORT = "BOOKSORT";//点击书架上方更多内书籍排序
    public static final String BOOKCLICK = "BOOKCLICK";//书籍点击
    public static final String TOBOOKCITY = "TOBOOKCITY";//空白页点击跳转书城
    public static final String LONGTIMEBOOKSHELFEDIT = "LONGTIMEBOOKSHELFEDIT";//长按编辑书架
    public static final String VERSIONUPDATE2 = "VERSIONUPDATE";//点击更新

    //书架编辑页
    public static final String SELECTALL1 = "SELECTALL";//全选
    public static final String DELETE1 = "DELETE";//删除
    public static final String CANCLE1 = "CANCLE";//右上角取消


    //缓存管理页
    public static final String BOOKCLICK1 = "BOOKCLICK";//书籍点击
    public static final String CACHEBUTTON = "CACHEBUTTON";//缓存按钮点击
    public static final String CACHEEDIT1 = "CACHEEDIT";//右上角编辑按钮点击

    //缓存编辑页
    public static final String SELECTALL = "SELECTALL";//全选
    public static final String DELETE = "DELETE";//删除
    public static final String CANCLE = "CANCLE";//右上角取消
    public static final String SORT = "SORT";//右上角取消

    //搜索页
    public static final String BAR = "BAR";//点击搜索框
    public static final String BARCLEAR = "BARCLEAR";//搜索词清空
    public static final String TOPIC = "TOPIC";//大家都在搜-点击搜索热词
    public static final String TOPICCHANGE = "TOPICCHANGE";//大家都在搜-换一换
    public static final String HISTORY = "HISTORY";//搜索历史-点击某一条搜索历史
    public static final String HISTORYCLEAR = "HISTORYCLEAR";//搜索历史-历史记录清空
    public static final String TIPLISTCLICK = "TIPLISTCLICK";//自动补全结果点击
    public static final String SEARCHBUTTON = "SEARCHBUTTON";//自动补全点击“搜索”按钮
    public static final String SHELFADD = "SHELFADD";//点击加入书架
    public static final String CLEAR = "CLEAR";//点击清空，重回搜索页
    public static final String HOTREADCLICK = "HOTREADCLICK";//热门阅读-书籍点击
    public static final String HOTREADCHANGE = "HOTREADCHANGE";//点击换一换


    public static final String BARLIST = "BARLIST"; //搜索框下拉历史词点击

    //书籍详情页
    public static final String SOURCECHANGE = "SOURCECHANGE";//点击切源弹出
    public static final String LATESTCHAPTER = "LATESTCHAPTER";//点击最新章节（目录）
    public static final String CATALOG = "CATALOG";//点击查看目录
    public static final String CASHEALL = "CASHEALL";//点击全本缓存
    public static final String SHELFEDIT = "SHELFEDIT";//点击加入书架
    public static final String TRANSCODEREAD = "TRANSCODEREAD";//点击转码阅读
    public static final String ENTER = "ENTER";//进入书籍详情页
    public static final String SOURCECHANGEPOPUP = "SOURCECHANGEPOPUP";//换源弹窗
    public static final String INTRODUCTION = "INTRODUCTION";//简介点击展开/收起
    public static final String RECOMMENDEDBOOK = "RECOMMENDEDBOOK";//点击推荐的书籍
    public static final String TRANSCODEPOPUP = "TRANSCODEPOPUP";//点击转码阅读


    //书籍目录页
    public static final String CATALOG_CASHEALL = "CASHEALL";//点击全本缓存
    public static final String CATALOG_CATALOGCHAPTER = "CATALOGCHAPTER";//目录中点击某章节
    public static final String CATALOG_SHELFEDIT = "SHELFEDIT";//点击加入书架
    public static final String CATALOG_TRANSCODEREAD = "TRANSCODEREAD";//点击转码阅读
    public static final String CATALOG_TRANSCODEPOPUP = "TRANSCODEPOPUP";//转码弹窗


    //更多设置
    public static final String PUSHSET = "PUSHSET";//	消息推送开启与关闭
    public static final String PUSHAUDIO = "PUSHAUDIO";//推送声音

    //个人中心
    public static final String LOGIN = "LOGIN";//	点击登录
    public static final String NIGHTMODE = "NIGHTMODE";//点击夜间模式
    public static final String PERSON_HISTORY = "HISTORY";//	点击浏览足迹
    public static final String HISTORYLOGIN = "HISTORYLOGIN";//点击浏览足迹内的登录
    public static final String MORESET = "MORESET";//点击更多设置
    public static final String HELP = "HELP";//点击帮助与反馈
    public static final String COMMENT = "COMMENT";//点击去评分
    public static final String VERSION = "VERSION";//点击当前版本
    public static final String VERSIONUPDATE = "VERSIONUPDATE";//点击版本更新
    public static final String CACHECLEAR = "CACHECLEAR";//点击清除缓存
    public static final String PROCTCOL = "PROCTCOL";//点击使用协议
    public static final String LOGOUT = "LOGOUT";//点击退出登录
    public static final String WIFI_AUTOCACHE = "WIFI_AUTOCACHE";//点击退出登录

    //阅读页
    public static final String LABELEDIT = "LABELEDIT";//添加书签
    public static final String ORIGINALLINK = "ORIGINALLINK";//点击源网页链接
    public static final String CACHE = "CACHE";//点击阅读页内缓存
    public static final String MORE1 = "MORE";//点击阅读页内更多
    public static final String CATALOG1 = "CATALOG";//点击阅读页内目录
    public static final String BOOKMARK = "BOOKMARK";//点击阅读页目录内书签
    public static final String NIGHTMODE1 = "NIGHTMODE";//点击阅读页内日/夜间模式
    public static final String CHAPTERTURN = "CHAPTERTURN";//点击阅读页内上/下章切换
    public static final String REPAIRDEDIALOGUE = "REPAIRDEDIALOGUE";//弹出修复提示弹窗
    public static final String DIRECTORYREPAIR = "DIRECTORYREPAIR";//点击阅读页目录内修复书籍
    public static final String POPUPSHELFADD = "POPUPSHELFADD";//阅读页加入书架弹窗加入
    public static final String POPUPSHELFADDCANCLE = "POPUPSHELFADDCANCLE";//阅读页加入书架弹窗取消
    public static final String SET = "SET";//点击阅读页内设置


    //'阅读页设置
    public static final String LIGHTEDIT = "LIGHTEDIT";//点击亮度调整
    public static final String SYSFOLLOW = "SYSFOLLOW";//点击跟随系统
    public static final String WORDSIZE = "WORDSIZE";//点击字号增/减
    public static final String BACKGROUNDCOLOR = "BACKGROUNDCOLOR";//点击阅读背景色
    public static final String READGAP = "READGAP";//点击阅读间距
    public static final String PAGETURN = "PAGETURN";//点击翻页模式
    public static final String HPMODEL = "HPMODEL";//点击横/竖屏模式
    public static final String AUTOREAD = "AUTOREAD";//点击自动阅读
    public static final String FULLSCREENPAGEREAD = "FULLSCREENPAGEREAD";//点击全屏翻页阅读

    //阅读页更多
    public static final String READ_SOURCECHANGE = "SOURCECHANGE";//换源
    public static final String READ_SOURCECHANGECONFIRM = "SOURCECHANGECONFIRM";//确认换源
    public static final String BOOKMARKEDIT = "BOOKMARKEDIT";//添加书签
    public static final String BOOKDETAIL = "BOOKDETAIL";//书籍详情


    //搜索结果页
    public static final String SEARCHRESULT = "SEARCHRESULT";//某本书点击

    //Crash
    public static final String CRASH = "CRASH";

    //青果推荐页
    public static final String QG_TJY_MODULEEXPOSE = "MODULEEXPOSE";//模块露出
    public static final String QG_TJY_BOOKEXPOSE = "BOOKEXPOSE";//各书籍位置露出
    public static final String QG_TJY_SEARCH = "SEARCH";//点击搜索
    public static final String QG_TJY_BOOKCLICK = "BOOKCLICK";//点击搜索
    public static final String QG_TJY_MORE = "MORE";//点击搜索

    //青果榜单页
    public static final String QG_BDY_MODULEEXPOSE = "MODULEEXPOSE";//模块露出
    public static final String QG_BDY_BOOKEXPOSE = "BOOKEXPOSE";//各书籍位置露出
    public static final String QG_BDY_SEARCH = "SEARCH";//点击搜索
    public static final String QG_BDY_BOOKCLICK = "BOOKCLICK";//点击搜索
    public static final String QG_BDY_MORE = "MORE";//点击搜索


    //青果分类页
    public static final String QG_FL_FIRSTCLASS = "FIRSTCLASS";//点击一级分类
    public static final String QG_FL_BOOKCLICK = "BOOKCLICK";//书籍点击
    public static final String QG_FL_SEARCH = "SEARCH";//点击搜索

    //一级分类页面
    public static final String FIRST_SEARCH = "SEARCH";//点击搜索

    //下载解包
    public static final String DOWNLOADPACKE = "DOWNLOADPACKE";
    public static final String RESOLVEPACKE = "RESOLVEPACKE";


    public static final String READFINISH = "READFINISH";//阅读完结页
    public static final String REPLACE = "REPLACE";   //完结页点击换一换
    public static final String TOSHELF = "TOSHELF";   //完结页点击去书架
    public static final String TOBOOKSTORE = "TOBOOKSTORE";   //完结页点击去书城


    private static final ExecutorService logThreadPool = Executors.newSingleThreadExecutor();
    private static List<String> prePageList = new ArrayList<>();

    private static List<ServerLog> linkList = new LinkedList<ServerLog>();

    //上传普通的点击事件
    public static void upLoadEventLog(Context context, String pageCode, String identify) {

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
