package com.hlsp.video.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity mActivity, float bgAlpha) {
        //如果activity的实例化为null。不去执行虚化
        if (mActivity != null) {
            WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
            //如果版本号小于21(5.0)不设置透明度
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                lp.alpha = 1f; // 0.0-1.0
            } else {
                lp.alpha = bgAlpha; // 0.0-1.0
            }
            mActivity.getWindow().setAttributes(lp);
        }
    }


    /**
     * 判断是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity == null) {
                Log.w("Utility", "couldn't get connectivity manager");
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].isAvailable()) {
                            Log.d("Utility", "network is available");
                            return true;
                        }
                    }
                }
            }
        }
        Log.d("Utility", "network is not available");
        return false;
    }

    /**
     * 发送文字通知
     *
     * @param context
     * @param Msg
     * @param Title
     * @param content
     * @param i
     */
    @SuppressWarnings("deprecation")
    public static void sendText(Context context, String Msg, String Title,
                                String content, Intent i) {
        //QT E
        /*NotificationManager mn = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				Msg, System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, Title, content, contentIntent);
		mn.notify(0, notification);*/
        //QT B
    }

    /**
     * 移除SharedPreference
     *
     * @param context
     * @param key
     */
    public static final void RemoveValue(Context context, String key) {
        Editor editor = getSharedPreference(context).edit();
        editor.remove(key);
        boolean result = editor.commit();
        if (!result) {
            Log.e("移除Shared", "save " + key + " failed");
        }
    }

    private static final SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 获取SharedPreference 值
     *
     * @param context
     * @param key
     * @return
     */
    public static final String getValue(Context context, String key) {
        return getSharedPreference(context).getString(key, "");
    }

    public static final Boolean getBooleanValue(Context context, String key) {
        return getSharedPreference(context).getBoolean(key, false);
    }

    public static final void putBooleanValue(Context context, String key,
                                             boolean bl) {
        Editor edit = getSharedPreference(context).edit();
        edit.putBoolean(key, bl);
        edit.commit();
    }

    public static final int getIntValue(Context context, String key) {
        return getSharedPreference(context).getInt(key, 0);
    }

    public static final long getLongValue(Context context, String key,
                                          long default_data) {
        return getSharedPreference(context).getLong(key, default_data);
    }

    public static final boolean putLongValue(Context context, String key,
                                             Long value) {
        Editor editor = getSharedPreference(context).edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static final Boolean hasValue(Context context, String key) {
        return getSharedPreference(context).contains(key);
    }

    /**
     * 设置SharedPreference 值
     *
     * @param context
     * @param key
     * @param value
     */
    public static final boolean putValue(Context context, String key,
                                         String value) {
        value = value == null ? "" : value;
        Editor editor = getSharedPreference(context).edit();
        editor.putString(key, value);
        boolean result = editor.commit();
        if (!result) {
            return false;
        }
        return true;
    }

    /**
     * 设置SharedPreference 值
     *
     * @param context
     * @param key
     * @param value
     */
    public static final boolean putIntValue(Context context, String key,
                                            int value) {
        Editor editor = getSharedPreference(context).edit();
        editor.putInt(key, value);
        boolean result = editor.commit();
        if (!result) {
            return false;
        }
        return true;
    }

    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;

            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取只有小数点的版本号 1.1.3
     *
     * @param context
     * @return
     */
    public static String getPointVersion(Context context) {
        String version = getVersion(context);
        String[] verArray = version.split("-");
        return verArray[0];
    }

    /**
     * 获取format版本号    没有 . 的版本号  例：111
     */
    public static String getFormatBersion(Context context) {
        String version = getVersion(context);
        String ver = version.replace(".", "");
        String[] verArray = ver.split("-");
        return verArray[0];
    }

    /**
     * 获取format版本号    有 . 的版本号  例：1.1.1
     */
    public static String getHasDotVersion(Context context) {
        return getVersion(context);
    }

    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(4000);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 判断配置文件需不需要更新
     *
     * @param var1 服务器配置文件
     * @param var2 本地配置文件
     * @return
     */

    public static boolean isCongruent(String var1, String var2) {
        String[] split = var1.split("\\.");
        String[] split1 = var2.split("\\.");
        if (split != null && split1 != null) {          //如果两个转数组不为空，进入下一步判断
            if (split.length == split1.length) {        //如果数组长度相等。进入下一步判断
                for (int i = 0; i < split.length; i++) {    //循环遍历
                    if (Integer.parseInt(split[i]) > Integer.parseInt(split1[i])) {         //如果服务器某一项大于本地，去更新
                        return true;
                    } else if (Integer.parseInt(split[i]) < Integer.parseInt(split1[i])) {     //如果服务器某一项小于本地，不更新
                        return false;
                    }
                    //都不成里进入下次循环
                }
            } else {                                    //如果数组长度不相等。直接更新
                return true;
            }
        } else {                                        //如如果任意一个转数组为空。直接更新
            return true;
        }
        return false;       //都不成立，不更新
    }


    /**
     * @return True iff the url is valid.
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        return (isHttpUrl(url) ||
                isHttpsUrl(url));
    }

    /**
     * @return True iff the url is an http: url.
     */
    public static boolean isHttpUrl(String url) {
        return (null != url) &&
                (url.length() > 6) &&
                url.substring(0, 7).equalsIgnoreCase("http://");
    }

    /**
     * @return True iff the url is an https: url.
     */
    public static boolean isHttpsUrl(String url) {
        return (null != url) &&
                (url.length() > 7) &&
                url.substring(0, 8).equalsIgnoreCase("https://");
    }

    private static float sDensity = 0;

    /**
     * DP转换为像素
     *
     * @param context
     * @param nDip
     * @return
     */
    public static int dipToPixel(Context context, float nDip) {
        if (sDensity == 0) {
            final WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            sDensity = dm.density;
        }
        return (int) (sDensity * nDip);
    }

    /***
     * sp 转换成 px
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * PX转为DP
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int pixelToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * MD5加密
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * MD5加密
     */
    public static String md5(byte[] hash) {
        try {
            //先MD5一下
            hash = MessageDigest.getInstance("MD5").digest(hash);
            //转成字符串
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * pulltofresh
     */
    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    /**
     * 去掉String 类型的小数点后面的
     */
    public static String removeDot(String num) {
        float temp = Float.parseFloat(num);
        int tempint = (int) temp;
        return String.valueOf(tempint);
    }

    /**
     * 获取权限状态
     *
     * @param permission
     * @return
     */
    public boolean selfPermissionGranted(String permission, Context cx) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

//        if (isMarshmallow(cx)) {
//        	cx.checkSelfPermission(Manifest.permission.READ_CONTACTS);
//         //   != PackageManager.PERMISSION_GRANTED
//                result = cx.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
//            } else {
//                result = cx.PermissionChecker.checkSelfPermission(context, permission)
//                        == PermissionChecker.PERMISSION_GRANTED;
//            }

        return result;
    }

    //判断当前版本是否为6.0
    private boolean isMarshmallow(Context cx) throws NameNotFoundException {
        PackageManager packageManager = cx.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(cx.getPackageName(), 0);
        String version = packInfo.versionName;

        return version.equals("6.0");
    }

    /**
     * 去掉字符！后面的
     *
     * @param str
     * @return
     */
    public static String removeMark(String str) {
        int loc = str.indexOf("!");
        String newStr;
        if (loc != -1) {
            newStr = str.substring(0, loc);
        } else {
            newStr = str;
        }
        return newStr;
    }

    /**
     * 传入文件路径,返回byte数组
     *
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 传入文件路径,返回byte数组
     *
     * @param file
     * @return
     */
    public static byte[] File2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 传入文件返回MD5
     *
     * @param file
     * @return
     */
    public static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            MappedByteBuffer byteBuffer = is.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
            /* MD5自动补齐 */
            int valueLength = 32 - value.length();
            for (int i = 0; i < valueLength; i++) {
                value = "0" + value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 检查又拍云集合中是否存在传入
     *
     * @param file
     * @return result: MD5, 不存在  1, 存在相同图片  -1, 异常
     */
//    public static String checkUYImageMD5(File file) {
//        String result = "-1";
//        long VERIFY_FILE_LENGTH = 8 * 1024;
//        String fileMd5Str = getFileMD5(file, VERIFY_FILE_LENGTH);
//        /* 获取文件MD5为空, 返回异常状态 */
//        if (TextUtils.isEmpty(fileMd5Str))
//            return result;
//        /* 是否存在于数据库 */
//        RealmHelper mRealmHelper = new RealmHelper(SampleApplicationLike.getInstance());
//        boolean isExist = mRealmHelper.getImageEmojiDao().FindByFileMd5(fileMd5Str);
//
//        result = isExist ? "1" : fileMd5Str;
//
//        return result;
//
//    }

    /**
     * 获取图片后 size长度的 MD5
     *
     * @param file
     * @param size
     * @return
     */
    public static String getFileMD5(File file, long size) {
        String result = null;    //文件的MD5
        long verifySize = size;
        long startPosition = 0L;
        RandomAccessFile is = null;
        try {
            /* 如果文件长度小于传入size, 验证MD5size更为文件长度 */
            if (verifySize >= file.length()) {
                verifySize = file.length();
            } else {
                startPosition = file.length() - size;
            }
            is = new RandomAccessFile(file, "r");
            is.seek(startPosition);
            byte[] buffer = new byte[(int) verifySize];
            is.read(buffer);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            result = bi.toString(16);
            /* MD5自动补齐 */
            int valueLength = 32 - result.length();
            for (int i = 0; i < valueLength; i++) {
                result = "0" + result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 传入文件返回MD5
     *
     * @param file
     * @return
     */
    public static String fileMD5(File file) throws IOException {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        String value = null;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用DigestInputStream
            fileInputStream = new FileInputStream(file);
            digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0) ;
            // 获取最终的MessageDigest
            messageDigest = digestInputStream.getMessageDigest();
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            value = byteArrayToHex(resultByteArray);
            if (value != null) {
                /* MD5自动补齐 */
                int valueLength = 32 - value.length();
                for (int i = 0; i < valueLength; i++) {
                    value = "0" + value;
                }
            }
            return value;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } finally {
            try {
                digestInputStream.close();
            } catch (Exception e) {
            }
            try {
                fileInputStream.close();
            } catch (Exception e) {
            }

        }

    }

    public static String byteArrayToHex(byte[] byteArray) {
        //首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        //new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符）
        char[] resultCharArray = new char[byteArray.length * 2];
        //遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        //字符数组组合成字符串返回
        return new String(resultCharArray);
    }


    /**
     * 获取手机的rom版本（魅族的flyme，小米的MIUI）
     *
     * @return
     */
    public static String getDisplay() {
        return Build.DISPLAY;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getDevice() {
        return Build.DEVICE;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getDeviceId() {
        return Build.ID;
    }

    /**
     * Return pseudo unique ID
     * 给后台传的设备唯一标识码（别问我我也不知道这是什么意思  by@bug）
     *
     * @return ID
     */
    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
    /*
    String phoneInfo = "Product: " + android.os.Build.PRODUCT;
    phoneInfo += ",\n CPU_ABI: " + android.os.Build.CPU_ABI;
    phoneInfo += ", \nTAGS: " + android.os.Build.TAGS;
    phoneInfo += ", \nVERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
    phoneInfo += ",\n MODEL: " + android.os.Build.MODEL;
    phoneInfo += ", \nSDK: " + android.os.Build.VERSION.SDK;
    phoneInfo += ", \nVERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
    phoneInfo += ",\n DEVICE: " + android.os.Build.DEVICE;
    phoneInfo += ",\n DISPLAY: " + android.os.Build.DISPLAY;
    phoneInfo += ",\n BRAND: " + android.os.Build.BRAND;
    phoneInfo += ",\n BOARD: " + android.os.Build.BOARD;
    phoneInfo += ", \nFINGERPRINT: " + android.os.Build.FINGERPRINT;
    phoneInfo += ",\n ID: " + android.os.Build.ID;
    phoneInfo += ",\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
    phoneInfo += ", \nUSER: " + android.os.Build.USER;
    phoneInfo += ", \nsssss: " + Utils.getUniquePsuedoID();
    Log.e(TAG,phoneInfo); */


    public static String filterStrBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String formatNumber(long val) {
        if (val < 10000) {
            return val + "";
        }
        DecimalFormat df = new DecimalFormat("######0.0");
        double d = val / 10000.0;
        return df.format(d) + "万";
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatTimeStr(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String dateString = formatter.format(time);
        return dateString;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatTimeDetailStr(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getWifiMac(Context ctx) {
        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String str = info.getMacAddress();
        if (str == null) {
            str = "";
        }
        return str;
    }

    @SuppressWarnings("deprecation")
    public static String getOSSDK() {
        return android.os.Build.VERSION.SDK;
    }

    public static String getOSRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getDeviceName() {
        return android.os.Build.MODEL;
    }

    public static String getDeviceFactory() {
        return android.os.Build.BRAND;
    }

    public static int getDeviceWidth(Activity act) {
        DisplayMetrics metric = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getDeviceHeight(Activity act) {
        DisplayMetrics metric = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static int getDeviceDpi(Activity act) {
        DisplayMetrics metric = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.densityDpi;
    }

    public static float getDeviceDensity(Activity act) {
        DisplayMetrics metric = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.density;
    }


    public static String getDeviceUUID(Context ctx) {
        try {
            return UUID.nameUUIDFromBytes(getDeviceAndroidId(ctx).getBytes("utf8")).toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDeviceAndroidId(Context ctx) {
        try {
            return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return "";
        }
    }

    @SuppressWarnings("deprecation")
    public static String getDeviceIMEI(Context ctx) {
        try {
            return ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }


    private static Method systemProperties_get = null;

    private static String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null)
                return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return "";
    }


    /**
     * 获取android 设备序列号
     *
     * @return SerialNo
     */
    public static String getSerialNo() {
        String[] propertys = {"ro.boot.serialno", "ro.serialno"};
        String v = "";
        for (String key : propertys) {
            v = getAndroidOsSystemProperties(key);
            Log.e("", "get " + key + " : " + v);
        }
        return v;
    }


}
