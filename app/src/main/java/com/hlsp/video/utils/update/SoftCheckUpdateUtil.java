package com.hlsp.video.utils.update;


import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.WeakHandler;
import com.hlsp.video.bean.UpdateResponse;
import com.hlsp.video.bean.data.UpdateData;
import com.hlsp.video.model.ConstantsValue;
import com.hlsp.video.model.HttpBaseUrl;
import com.hlsp.video.okhttp.http.OkHttpClientManager;
import com.hlsp.video.utils.CommonUtils;
import com.hlsp.video.utils.GsonUtil;
import com.hlsp.video.utils.SpUtils;
import com.hlsp.video.utils.ToastUtil;
import com.hlsp.video.view.NumberProgressBar;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

/**
 * 检查版本升级工具类
 *
 * @author admin
 */
public class SoftCheckUpdateUtil {

    // 关于界面 点击升级时弹出正在检查更新提示
    private boolean isOnclick;
    private Dialog checkUpdateDialog;

    private Dialog alertAppUpdateDialog;
    private TextView updatTittle;
    private TextView updatMessage;
    private Button cancelBtn;
    private Button confirmBtn;

    private Context ctx;
    private UpdateData requestCheckSoftUpdate;

    private NumberProgressBar softUpdateProcessBar;

    boolean isFirst = true;

    private int width;
    WeakHandler handler;

    /**
     * @param ctx
     * @param handler
     * @param isOnclick 是否是点击检查，
     */
    public void checkSoftUpdate(final Context ctx, boolean isOnclick, int width) {


        this.width = width;
        this.ctx = ctx;

        handler = new WeakHandler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                if (msg.what == 1) {
                    int index = msg.getData().getInt("index");
                    int len = msg.getData().getInt("step");
                    final int fileLength = msg.getData().getInt("fileLength");
                    if (softUpdateProcessBar != null) {
                        softUpdateProcessBar.setVisibility(View.VISIBLE);

                        softUpdateProcessBar.setMax(fileLength / 1000);
                        if (index == 0) {
                            count1 = len;
                        }
                        if (index == 1) {
                            count2 = len;
                        }
                        if (index == 2) {
                            count3 = len;
                        }
                        count = count1 + count2 + count3;

                        softUpdateProcessBar.setProgress(count / 1000);


                    }
                    if (count >= fileLength) {
                        CommonUtils.installFuction(ctx, confirmBtn);
                    }
                }
                return false;

            }
        });


        this.isOnclick = isOnclick;

        if (isOnclick) {
            showCheckUpdateDialog(ctx);
        }

        // 是否执行下次不再检查功能
        boolean autoCheckSoft = SpUtils.getBoolean(ConstantsValue.USER_SOFT_CHECK_UPDATE, true);

        if (autoCheckSoft || isOnclick) {

            initDialog();

            if (isOnclick) {
                checkBox.setVisibility(View.GONE);
            }

            requestSoftCheckUpdate();
        }
    }

    // 弹出检查更新提示
    public void showCheckUpdateDialog(Context ctx) {
        //        checkUpdateDialog = CustomDialog.createLoadingDialog(ctx, R.string.progress_dialog_tip_type15);
        //        checkUpdateDialog.show();
    }

    private void initDialog() {
        LinearLayout view = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.activity_app_update_downloaded_info_show, null);
        alertAppUpdateDialog = new Dialog(ctx, R.style.Dialog_loading);
        alertAppUpdateDialog.getWindow().setGravity(Gravity.CENTER);
        alertAppUpdateDialog.getWindow().setContentView(view);

        if (width == 0) {
            width = 450;
        }

        alertAppUpdateDialog.getWindow().setLayout((int) (width * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
        cancelBtn = (Button) view.findViewById(R.id.app_update_cancel);
        confirmBtn = (Button) view.findViewById(R.id.app_update_confirm);
        confirmBtn.setText("升级");
        updatTittle = (TextView) view.findViewById(R.id.app_update_dialog_tittle);
        updatMessage = (TextView) view.findViewById(R.id.app_update_dialog_message);
        checkBox = (CheckBox) view.findViewById(R.id.app_update_dialog_checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    confirmBtn.setText("保存");
                } else {
                    checkBox.setChecked(false);
                    confirmBtn.setText("升级");
                }
            }
        });

        softUpdateProcessBar = (NumberProgressBar) view.findViewById(R.id.progressBarUpdate);

        softUpdateProcessBar.setVisibility(View.INVISIBLE);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    alertAppUpdateDialog.cancel();
                    SpUtils.put(ConstantsValue.USER_SOFT_CHECK_UPDATE, false);
                } else {
                    updateConfigBtn();
                    if (!isOnclick) {
                        SpUtils.put(ConstantsValue.USER_SOFT_CHECK_UPDATE, true);
                    }
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertAppUpdateDialog.cancel();
                if (loader != null) {
                    loader.stopping = true;
                }
            }
        });
    }

    /**
     * 请求检查更新
     *
     * @param
     */
    private void requestSoftCheckUpdate() {
        HashMap<String, String> paramsMap = new HashMap<>();

        paramsMap.put("packageName", CommonUtils.getProcessName());
        paramsMap.put("versionName", CommonUtils.getVersionName(App.getInstance()));
        paramsMap.put("channelId", CommonUtils.getMetaData(App.getInstance(), "BaiduMobAd_CHANNEL"));

        StringBuilder paramsSb = new StringBuilder();

        for (String key : paramsMap.keySet()) {
            paramsSb.append(key + "=" + paramsMap.get(key) + "&");
        }
        String urlStr = HttpBaseUrl.BASE_URL + "v1/app/check" + "?" + paramsSb.toString();
        if (urlStr.endsWith("&")) {
            urlStr = urlStr.substring(0, urlStr.length() - 1);
        }

        LogUtils.e(urlStr);

        OkHttpClientManager.getAsyn(urlStr, new OkHttpClientManager.StringCallback() {
            @Override
            public void onResponse(String responseStr) {
                LogUtils.json(responseStr);

                UpdateResponse response = null;
                try {
                    response = GsonUtil.GsonToBean(responseStr, UpdateResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (null != response && null != response.getData()) {//处理更新逻辑 需要比较以前下载过的版本
                    requestCheckSoftUpdate = response.getData();

                    //                    showUpdateDialog(ctx, requestCheckSoftUpdate, requestCheckSoftUpdate.isForce_update());

                    System.out.println("serverVsionCode : " + requestCheckSoftUpdate.getUpdate_version() + " name ");
                    String serverVersionCode = requestCheckSoftUpdate.getUpdate_version();

                    if (!TextUtils.isEmpty(requestCheckSoftUpdate.getUpdate_version())) {
                        // 当前手机已装软件版本和服务器版本比较
                        int compareTo = serverVersionCode.compareTo(CommonUtils.getVersionName(ctx));
                        LogUtils.e(compareTo);

                        if (compareTo > 0) {
                            // 更新
                            boolean checkLocalFil = checkLocalFil(ctx, serverVersionCode);
                            if (checkLocalFil) {
                                showUpdateDialog(ctx, requestCheckSoftUpdate, requestCheckSoftUpdate.isForceUpdate());
                            } else {
                                // 安装界面
                                if (!requestCheckSoftUpdate.isForceUpdate()) {
                                    showInstallDialog(ctx);
                                } else {
                                    showUpdateDialog(ctx, requestCheckSoftUpdate, requestCheckSoftUpdate.isForceUpdate());
                                }
                            }
                        } else {
                            if (isOnclick) {
                                ToastUtil.showToast("已经是最新版本");
                            }
                        }
                        return;
                    } else {
                        if (isOnclick) {
                            ToastUtil.showToast("升级信息异常");
                        }
                        return;

                    }
                }


            }

            @Override
            public void onFailure(Request request, IOException e) {

                ToastUtil.showToast("网络连接失败");
            }
        });


//
//        MHHttpClient.getInstance().post(UpdateResponse.class, ConstantsValue.Url.CHECK_UPDATE, new MHHttpHandler<UpdateResponse>() {
//            @Override
//            public void onSuccess(UpdateResponse response) {
//
//                if (null != response && null != response.getData()) {//处理更新逻辑 需要比较以前下载过的版本
//                    requestCheckSoftUpdate = response.getData();
//
//                    //                    showUpdateDialog(ctx, requestCheckSoftUpdate, requestCheckSoftUpdate.isForce_update());
//
//                    System.out.println("serverVsionCode : " + requestCheckSoftUpdate.getVersion_name() + " name ");
//                    String serverVersionCode = requestCheckSoftUpdate.getVersion_name();
//
//                    if (!TextUtils.isEmpty(requestCheckSoftUpdate.getVersion_name())) {
//                        // 当前手机已装软件版本和服务器版本比较
//                        int compareTo = serverVersionCode.compareTo(CommonUtils.getVersionName(ctx));
//                        LogUtils.e(compareTo);
//
//                        if (compareTo > 0) {
//                            // 更新
//                            boolean checkLocalFil = checkLocalFil(ctx, serverVersionCode);
//                            if (checkLocalFil) {
//                                showUpdateDialog(ctx, requestCheckSoftUpdate, requestCheckSoftUpdate.isForce_update());
//                            } else {
//                                // 安装界面
//                                if (!requestCheckSoftUpdate.isForce_update()) {
//                                    showInstallDialog(ctx);
//                                } else {
//                                    showUpdateDialog(ctx, requestCheckSoftUpdate, requestCheckSoftUpdate.isForce_update());
//                                }
//                            }
//                        } else {
//                            if (isOnclick) {
//                                ToastUtil.showToast("已经是最新版本");
//                            }
//                        }
//                        return;
//                    } else {
//                        if (isOnclick) {
//                            ToastUtil.showToast("升级信息异常");
//                        }
//                        return;
//
//                    }
//                } else {
//                    if (!isComeMainActivity) {
//                        ToastUtil.showToast("已经是最新版本");
//                    }
//                }
//
//
//            }
//
//
//            @Override
//            public void onFailure(String content) {
//                if (!isComeMainActivity) {
//                    baseActivity.hiddenLoadingView();
//                }
//
//            }
//        });


    }

    /**
     * 升级的dialog
     *
     * @param ctx
     * @param requestCheckSoftUpdate
     */
    private void showUpdateDialog(Context ctx, final UpdateData requestCheckSoftUpdate, final boolean forceUpdate) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (forceUpdate) {
                    updatTittle.setText(R.string.activity_soft_update_tittle_warning);
                    updatMessage.setText(requestCheckSoftUpdate.getUpdate_content());
                    softUpdateProcessBar.setVisibility(View.INVISIBLE);
                    confirmBtn.setText(R.string.activity_soft_update_dialog_confirm_tip);
                    confirmBtn.setBackgroundResource(R.drawable.selector_dialog_left_update);
                    cancelBtn.setVisibility(View.GONE);
                    alertAppUpdateDialog.setCanceledOnTouchOutside(false);
                    alertAppUpdateDialog.setCancelable(false);
                    alertAppUpdateDialog.show();

                } else {
                    updatTittle.setText(R.string.activity_soft_update_tittle_warning);
                    updatMessage.setText(requestCheckSoftUpdate.getUpdate_content());
                    softUpdateProcessBar.setVisibility(View.INVISIBLE);
                    confirmBtn.setText(R.string.activity_soft_update_dialog_confirm_tip);
                    cancelBtn.setText(R.string.cancel);
                    cancelBtn.setVisibility(View.VISIBLE);
                    alertAppUpdateDialog.setCanceledOnTouchOutside(false);
                    alertAppUpdateDialog.setCancelable(false);
                    alertAppUpdateDialog.show();
                }
            }
        });

    }

    /**
     * 点击升级按钮
     */
    private MtDownloader loader;
    private CheckBox checkBox;

    private void updateConfigBtn() {
        if (requestCheckSoftUpdate != null) {
            softUpdateProcessBar.setVisibility(View.VISIBLE);
        }

        updatMessage.setText("提示： 升级过程中请勿关闭应用！");

        if (isFirst) {

            isFirst = false;
            FileUtil.setMkdir(ctx);

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                new Thread() {
                    public void run() {
                        loader = new MtDownloader(requestCheckSoftUpdate.getDownload_link(), 3, FileUtil.setMkdir(ctx), handler, ctx, alertAppUpdateDialog);
                        // 动态添加进度条codeUI
                        loader.stopping = false;
                        loader.doDownload();
                    }

                    ;
                }.start();
            } else {
                Toast.makeText(ctx, "sd卡不存在！", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Toast.makeText(ctx, "sd卡不存在！", 1).show();
        }

    }

    /**
     * 安装界面
     *
     * @param ctx
     */
    private void showInstallDialog(final Context ctx) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                alertAppUpdateDialog.show();
                confirmBtn.setText("安装");
                updatMessage.setText(requestCheckSoftUpdate.getUpdate_content());
                checkBox.setVisibility(View.GONE);
                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtils.installFuction(ctx, confirmBtn);
                    }
                });
            }
        });
    }

    /**
     * 检查本地是否已经下载，并且是最新的apk
     *
     * @param ctx
     * @return
     */
    public boolean checkLocalFil(Context ctx, String serverVsionCode) {

        String Mkdir = FileUtil.setMkdir(ctx);

        LogUtils.i(Mkdir);
        if (Mkdir.toLowerCase().endsWith(".apk")) {

            PackageManager pm = ctx.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(Mkdir, PackageManager.GET_ACTIVITIES);

            if (packageInfo != null) {
                String versionName = packageInfo.versionName;
                int compareTo = versionName.compareTo(serverVsionCode);

                if (compareTo >= 0) {
                    return false;
                }
            }
        }
        return true; // true
    }

    /**
     * 获取processbar
     */
    public NumberProgressBar getProcess() {
        return softUpdateProcessBar;
    }

    /**
     * 获取processbar
     */
    public Button getconfirmBtn() {
        return confirmBtn;
    }

    int count = 0;
    int count2 = 0;
    int count3 = 0;
    int count1 = 0;


}
