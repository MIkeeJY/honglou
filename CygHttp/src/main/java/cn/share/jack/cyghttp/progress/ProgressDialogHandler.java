package cn.share.jack.cyghttp.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


/**
 * 显示dialog的handler
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private ProgressDialog pd;

    private Context context;
    private boolean cancelable;

    public ProgressDialogHandler(Context context, boolean cancelable) {
        super();
        this.context = context;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(String title) {
        if (pd == null) {
            pd = new ProgressDialog(context);
            if (TextUtils.isEmpty(title)) {
                title = "加载中,请稍后....";
            }
            pd.setMessage(title);
            pd.setCancelable(cancelable);
            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dismissProgressDialog();
                    }
                });
            }

            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                String title = (String) msg.obj;
                initProgressDialog(title);
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
