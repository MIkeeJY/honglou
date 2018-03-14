package com.hyxsp.video.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.hyxsp.video.utils.update.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 通用方法工具类
 * Created by hackest on 2018/2/7.
 */

public class CommonUtils {

    /**
     * 获取渠道号
     */
    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            //
        }
        return null;
    }


    /**
     * @return 大于10万的返回以万为单位的数字, 如12.5万
     */
    public static String formatCount(long count) {
        if (count < 0)
            return "0";
        if (count < 100000) {
            return count + "";
        } else {
            return String.format("%.1f万", count / 10000.0f);
        }
    }


    // 启动安装界面
    public static void installFuction(final Context ctx, final Button confirmBtn) {
        String setMkdir = FileUtil.setMkdir(ctx);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(setMkdir)), "application/vnd.android.package-archive");
        ctx.startActivity(intent);

        confirmBtn.setText("安装");
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtils.installFuction(ctx, confirmBtn);
            }
        });
    }


    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context)// 获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
    }


    /**
     * 这个效率高一些
     *
     * @return http://www.rogerblog.cn/2016/03/17/android-proess/
     */
    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
