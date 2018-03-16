package com.hlsp.video.utils.update;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 */

public class FileUtil {

    /**
     * 检验SDcard状态
     *
     * @return boolean
     */
    public static boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存文件文件到目录
     *
     * @param context
     * @return 文件保存的目录
     */
    public static String setMkdir(Context context) {

        String filePath;
        if (checkSDCard()) {
            filePath = Environment.getExternalStorageDirectory() + "/Honglou/temp" + File.separator + "Honglou.apk";
        } else {
            filePath = context.getCacheDir().getAbsolutePath() + File.separator
                    + "temp" + File.separator + "Honglou.apk";
        }

        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 得到文件的名称
     *
     * @return
     * @throws IOException
     */
    public static String getFileName(String url) {
        String name = null;
        try {
            name = url.substring(url.lastIndexOf("/") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 得到文件的名称
     *
     * @return
     * @throws IOException
     */
    public static String getFileType(String url) {
        String name = null;
        try {
            name = url.substring(url.lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
}