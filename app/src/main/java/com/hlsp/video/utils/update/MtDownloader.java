package com.hlsp.video.utils.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.hlsp.video.base.WeakHandler;
import com.hlsp.video.bean.DownloadBean;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载器
 */
public class MtDownloader {
    public String url;
    public int count;
    public String localFilePath;

    /* 文件大小 */
    public int fileLength;

    public WeakHandler h;

    public List<DownloadBean> downloadInfos;

    /* 不要停止下载 */
    public boolean stopping = false;
    private Context ctx;
    private Dialog dialog;

    public MtDownloader(String url, int count, String localFilePath, WeakHandler h, Context ctx, Dialog dialog) {
        this.url = url;
        this.count = count;
        this.localFilePath = localFilePath;
        this.h = h;
        this.ctx = ctx;
        this.dialog = dialog;
        //初始化下载
        init();
    }

    /**
     * 初始化下载
     */
    private void init() {
        //判断是新传/续传
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            fileLength = conn.getContentLength();
            File file = new File(localFilePath);
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.setLength(fileLength);
            raf.close();
            //初始化下载信息集合
            iniDownloadInfos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化下载信息集合
     */
    private void iniDownloadInfos() {
        downloadInfos = new ArrayList<DownloadBean>();
        DownloadBean bean = null;

        //计算平均每个线程的下载快数
        int block = fileLength / count;

        int startPos;
        int endPos;
        for (int i = 0; i < count; i++) {
            //处理每个线程开始和结束位置
            startPos = i * block;
            if (i == (count - 1)) {
                endPos = fileLength - 1;
            } else {
                endPos = (i + 1) * block - 1;
            }
            bean = new DownloadBean();
            bean.index = i;
            bean.starPos = startPos;
            bean.endPos = endPos;
            downloadInfos.add(bean);
        }
    }

    /**
     * 开启n条线程进行下载,
     */
    public void doDownload() {
        if (downloadInfos != null) {
            for (DownloadBean bean : downloadInfos) {
                DownThread t = new DownThread(bean);
                t.start();
            }
        }
    }

    /**
     * 下载线程
     */
    class DownThread extends Thread {
        public DownloadBean bean;
        private int downLength;

        public DownThread(DownloadBean bean) {
            this.bean = bean;
        }

        public void run() {
            HttpURLConnection conn;
            if (!stopping) {
                try {
                    URL u = new URL(url);
                    conn = (HttpURLConnection) u.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    //设置访问的范围
                    conn.setRequestProperty("Range", "bytes=" + (bean.starPos + bean.amount) + "-" + bean.endPos);
                    int responseCode = conn.getResponseCode();
                    InputStream is = conn.getInputStream();
                    RandomAccessFile raf = new RandomAccessFile(new File(localFilePath), "rwd");
                    //定位本地文件开始写入的位置
                    raf.seek(bean.starPos + bean.amount);
                    byte[] buffer = new byte[4196];
                    int len = -1;
                    while (((len = is.read(buffer)) != -1) && !stopping) {
                        //写入数据到本地文件
                        raf.write(buffer, 0, len);
                        downLength = downLength + len;
                        //更新数据库的下载量
                        //向主线程ui发送消息
                        Message msg = new Message();
                        msg.what = 1;
                        Bundle b = new Bundle();
                        b.putInt("step", downLength);
                        b.putInt("fileLength", fileLength);
                        b.putInt("index", bean.index);
                        msg.setData(b);
                        h.sendMessage(msg);
                    }
                } catch (Exception e) {
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                stopping = true;//下载失败状态重置
                                Toast.makeText(ctx, "升级失败，请重试！", Toast.LENGTH_SHORT).show();
                            } catch (Exception e2) {
                            }
                        }
                    });
                }
            } else {
                File file = new File(localFilePath);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

}
