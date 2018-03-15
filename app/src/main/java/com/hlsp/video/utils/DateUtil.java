package com.hlsp.video.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat nf = new SimpleDateFormat("yyyy.MM.dd");
    public static SimpleDateFormat sf = new SimpleDateFormat("MM月dd日");
    public static SimpleDateFormat cf = new SimpleDateFormat("昨天HH:mm");
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat month = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat hour = new SimpleDateFormat("HH:mm");


    /******************** 时间相关常量 ********************/
    /**
     * 毫秒与毫秒的倍数
     */
    public static final int MSEC = 1;
    /**
     * 秒与毫秒的倍数
     */
    public static final int SEC = 1000;
    /**
     * 分与毫秒的倍数
     */
    public static final int MIN = 60000;
    /**
     * 时与毫秒的倍数
     */
    public static final int HOUR = 3600000;
    /**
     * 天与毫秒的倍数
     */
    public static final int DAY = 86400000;


    public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


    // dateUtil 获取当前的时间
    public static String getNowDate() {
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    // dateUtil 时间到字符串
    public static String DateToString(Date date) {
        String str = formatter.format(date);
        return str;
    }

    public static String getCostTime(long distance) {
        int day = (int) (distance / 86400);
        int hour = (int) ((distance % 86400) / 3600);
        int minute = (int) ((distance % 86400) % 3600) / 60;
        int second = (int) ((distance % 86400) % 3600) % 60;

        return day + "天" + hour + "小时" + minute + "分钟";
    }

    public static String getVoiceTime(int distance) {


        int minute = ((distance / 1000) / 60);
        int second = ((distance / 1000) % 60);

        Log.i("ogami", "时间计算" + minute + "/’" + second + "/’/’");


        return minute + "’" + second + "’’";
    }

    public static Date StringToDate(String str) {
        Date date = new Date();
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int calculateVideoDate(String time) {
        try {
            Date videoTime = format.parse(time);
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            long diff = curDate.getTime() - videoTime.getTime();
            return (int) (diff / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // dateUtil 获取当前的时间
    public static String getNowDateDF() {
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = df.format(curDate);
        return str;
    }

    public static String getNowDateNF() {
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = nf.format(curDate);
        return str;
    }

    public static String getLoactionDate(Context context, Date date) {
        ContentResolver cv = context.getContentResolver();
        String strTimeFormat = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TIME_12_24);
        if ("12".equals(strTimeFormat)) {
            return date.toLocaleString();
        }
        SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return sd.format(date);
    }

    public static String getLoactionDate2(Context context, Date date) {
        ContentResolver cv = context.getContentResolver();
        String strTimeFormat = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TIME_12_24);
        if ("12".equals(strTimeFormat)) {
            return date.toLocaleString();
        }
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

    /**
     * 距当前时间的字符串值
     *
     * @param beginLong
     * @return
     */
    public static String formatDate(long beginLong) {
        if (beginLong == 0) {
            return "未知";
        }
        return formatDate(beginLong, System.currentTimeMillis());
    }

    /*
     * 根据时间戳得到当前显示的时间
     */
    public static String formatDate(long beginLong, long nowLong) {

        Date beginDate = new Date(beginLong);
        Date endDate = new Date(nowLong);

        long betweenTime = (endDate.getTime() - beginDate.getTime()) / 1000;
        String dateStr = null;

        int betweenMinute = (int) Math.ceil(betweenTime / 60);
        if (betweenMinute > 1) {
            // 一分钟内
            int betweenHour = (int) Math.ceil(betweenTime / 3600);

            if (betweenHour >= 1) {

                if (betweenHour >= 24) {
                    // 一小时之外
                    int betweenDay = (int) Math.ceil(betweenTime / 86400);

                    if (betweenDay >= 1 && betweenDay < 2) {
                        dateStr = "昨天";
                    } else if (betweenDay >= 2 && betweenDay < 3) {
                        dateStr = "前天";
                    } else {
                        dateStr = df.format(new Date(beginLong));
                    }
                } else {
                    dateStr = betweenHour + "小时前";
                }

            } else {

                // 一小时之内
                dateStr = betweenMinute + "分钟前";
            }
        } else {

            dateStr = "刚刚";
        }
        return dateStr;
    }

    /*
     * 根据时间戳返回据当前的时间
     */
    public static boolean glFormatDate(long dataLine) {

        Date beginDate = new Date(System.currentTimeMillis());
        Date endDate = new Date(dataLine);
        long betweenTime = (beginDate.getTime() - endDate.getTime()) / 1000;
        int betweenMinute = (int) Math.ceil(betweenTime / 86400);

        if (betweenMinute > 5) {
            return true;
        } else {
            return false;
        }
    }

    public static String formatGameDiscussDate(Date beginDate) {

        if (beginDate == null) {
            return null;
        }
        Date systemDate = new Date();
        systemDate.setTime(System.currentTimeMillis());

        long betweenTime = (systemDate.getTime() - beginDate.getTime()) / 1000;
        String dateStr = null;

        int betweenMinute = (int) Math.ceil(betweenTime / 60);
        if (betweenMinute > 1) {
            // 一分钟内
            int betweenHour = (int) Math.ceil(betweenTime / 3600);

            if (betweenHour >= 1) {
                // 一小时之外
                int betweenDay = (int) Math.ceil(betweenTime / 86400);

                if (betweenDay >= 1 && betweenDay <= 3) {
                    // 一天之外
                    dateStr = betweenDay + "天前";
                } else if (betweenDay > 3) {
                    dateStr = df.format(beginDate);
                } else {
                    dateStr = betweenHour + "小时前";
                }

            } else {
                dateStr = betweenMinute + "分钟前";
            }
        } else {

            dateStr = "刚刚";
        }
        return dateStr;
    }

    /*
     * 根据生日计算年龄
     */
    public static int getAge(String birthdayStr) throws Exception {

        Date birthDay = df.parse(birthdayStr);
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    // do nothing
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        } else {
            // monthNow<monthBirth
            // donothing
        }
        return age;
    }

    /**
     * @param newDate 新设置的时间
     * @param nowDate 当前时间
     * @return true:大于当前时间 false:小于或等于当前时间
     * @author Lrchao
     */
    public static boolean compareDate(String newDate, String nowDate) {
        boolean isGreater = false;
        try {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(df.parse(newDate));
            c2.setTime(df.parse(nowDate));
            int result = c1.compareTo(c2);
            if (result > 0) {
                isGreater = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isGreater;
    }

    /**
     * 两个时间之间的时间差
     *
     * @param date1 当前日期
     * @param date2 比较相差的日期
     * @return
     */
    public static String getDays(Date date, Date mydate) {
        // 转换为标准时间
        long day = 0;
        try {
            day = (date.getTime() - mydate.getTime()) / (60 * 60 * 1000);
            // day = Math.abs(day);
            if (day > 24) {
                day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
                // day = Math.abs(day);
                if (day > 30) {
                    day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000) / 30;
                    // day = Math.abs(day);
                    return day + "个月";
                } else if (day > 7) {
                    day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000) / 7;
                    // day = Math.abs(day);
                    return day + "周";
                }
                return day + "天";
            } else if (day == 0) {
                day = (date.getTime() - mydate.getTime()) / (60 * 1000);
                // day = Math.abs(day);
                return day + "分钟";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day + "小时";
    }

    /**
     * 两个时间之间的时间差
     *
     * @param date1 当前日期
     * @param date2 比较相差的日期
     * @return
     */
    public static String getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return "0";
        if (date2 == null || date2.equals(""))
            return "0";
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        Date mydate = null;
        long day = 0;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
            day = (date.getTime() - mydate.getTime()) / (60 * 60 * 1000);
            // day = Math.abs(day);
            if (day > 24) {
                day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
                // day = Math.abs(day);
                if (day > 30) {
                    day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000) / 30;
                    // day = Math.abs(day);
                    return day + "个月";
                } else if (day > 7) {
                    day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000) / 7;
                    // day = Math.abs(day);
                    return day + "周";
                }
                return day + "天";
            } else if (day == 0) {
                day = (date.getTime() - mydate.getTime()) / (60 * 1000);
                // day = Math.abs(day);
                return day + "分钟";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day + "小时";
    }

    /**
     * 计算两个日期的时间间隔
     */
    public static int calInterval(Date sDate, Date eDate, String type) {
        // 时间间隔，初始为0
        int interval = 0;
        /* 比较两个日期的大小，如果开始日期更大，则交换两个日期 */
        // 标志两个日期是否交换过
        boolean reversed = false;
        if (compareDate(sDate, eDate) > 0) {
            Date dTest = sDate;
            sDate = eDate;
            eDate = dTest;
            // 修改交换标志
            reversed = true;
        }
        /* 将两个日期赋给日历实例，并获取年、月、日相关字段值 */
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setTime(sDate);
        int sYears = sCalendar.get(Calendar.YEAR);
        int sMonths = sCalendar.get(Calendar.MONTH);
        int sDays = sCalendar.get(Calendar.DAY_OF_YEAR);
        Calendar eCalendar = Calendar.getInstance();
        eCalendar.setTime(eDate);
        int eYears = eCalendar.get(Calendar.YEAR);
        int eMonths = eCalendar.get(Calendar.MONTH);
        int eDays = eCalendar.get(Calendar.DAY_OF_YEAR);
        // 年
        if (cTrim(type).equals("Y") || cTrim(type).equals("y")) {
            interval = eYears - sYears;
            if (eMonths < sMonths) {
                --interval;
            }
        }
        // 月
        else if (cTrim(type).equals("M") || cTrim(type).equals("m")) {
            interval = 12 * (eYears - sYears);
            interval += (eMonths - sMonths);
        }
        // 日
        else if (cTrim(type).equals("D") || cTrim(type).equals("d")) {
            interval = 365 * (eYears - sYears);
            interval += (eDays - sDays);
            // 除去闰年天数
            while (sYears < eYears) {
                if (isLeapYear(sYears)) {
                    --interval;
                }
                ++sYears;
            }
        }
        // 如果开始日期更大，则返回负值
        if (reversed) {
            interval = -interval;
        }
        // 返回计算结果
        return interval;
    }

    /**
     * 比较两个Date类型的日期大小
     *
     * @param sDate开始时间
     * @param eDate结束时间
     * @return result返回结果(0--相同 1--前者大 2--后者大)
     */
    private static int compareDate(Date sDate, Date eDate) {
        int result = 0;
        // 将开始时间赋给日历实例
        Calendar sC = Calendar.getInstance();
        sC.setTime(sDate);
        // 将结束时间赋给日历实例
        Calendar eC = Calendar.getInstance();
        eC.setTime(eDate);
        // 比较
        result = sC.compareTo(eC);
        // 返回结果
        return result;
    }

    /**
     * 判定某个年份是否是闰年
     *
     * @param year待判定的年份
     * @return 判定结果
     */
    private static boolean isLeapYear(int year) {
        return (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0));
    }

    /**
     * 字符串去除两头空格，如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
     *
     * @param tStr输入字符串
     * @return 如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
     */
    public static String cTrim(String tStr) {
        String ttStr = "";
        if (tStr == null) {
        } else {
            ttStr = tStr.trim();
        }
        return ttStr;
    }


    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param milliseconds 毫秒时间戳
     * @return 时间字符串
     */
    public static String milliseconds2String(long milliseconds) {
        return milliseconds2String(milliseconds, DEFAULT_SDF);
    }


    /**
     * 将时间戳转为时间字符串
     * <p>格式为用户自定义</p>
     *
     * @param milliseconds 毫秒时间戳
     * @param format       时间格式
     * @return 时间字符串
     */
    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }


    public static String getOrederTime(long milliseconds) {
        return milliseconds2String(milliseconds, new SimpleDateFormat("MM/dd HH:mm"));
    }


    public static String milliseconds2M(long milliseconds) {
        return milliseconds2String(milliseconds, month);
    }

    public static String milliseconds2h(long milliseconds) {
        return milliseconds2String(milliseconds, hour);
    }

    public static String milliseconds2nf(long milliseconds) {
        return milliseconds2String(milliseconds, nf);
    }

}
