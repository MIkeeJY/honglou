package cn.share.jack.cyghttp.callback;

import android.accounts.NetworkErrorException;
import android.net.ParseException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import cn.share.jack.cyghttp.ApiException;
import cn.share.jack.cyghttp.util.FRToast;

/**
 * 对请求失败的分类操作
 */
public abstract class CygBaseObserver<T> extends BaseObserver<T> {

    private static final String TAG = "CygBaseObserver";

    private boolean isNeedProgress;
    private String titleMsg;

    public CygBaseObserver() {
        this(null, null);
    }

    public CygBaseObserver(BaseImpl base) {
        this(base, null);
    }

    public CygBaseObserver(BaseImpl base, String titleMsg) {
        super(base);
        this.titleMsg = titleMsg;
        if (TextUtils.isEmpty(titleMsg)) {
            this.isNeedProgress = false;
        } else {
            this.isNeedProgress = true;
        }
    }

    @Override
    protected boolean isNeedProgressDialog() {
        return isNeedProgress;
    }

    @Override
    protected String getTitleMsg() {
        return titleMsg;
    }

    @Override
    protected void onBaseError(Throwable t) {
        StringBuffer sb = new StringBuffer();
        //        sb.append("请求失败：");
        if (t instanceof NetworkErrorException || t instanceof UnknownHostException || t instanceof ConnectException) {
            sb.append("网络异常");
        } else if (t instanceof SocketTimeoutException || t instanceof InterruptedIOException || t instanceof TimeoutException) {
            sb.append("请求超时");
        } else if (t instanceof JsonSyntaxException) {
            //            sb.append("请求不合法");
            // FIXME: 2017/6/22
            FRToast.showToastSafe(t.getMessage());
            return;

        } else if (t instanceof JsonParseException
                || t instanceof JSONException
                || t instanceof ParseException) {   //  解析错误
            sb.append("解析错误");
        } else if (t instanceof ApiException) {
            if (((ApiException) t).isTokenExpried()) {
                sb.append("Token出错");
            }
        } else {
            FRToast.showToastSafe(t.getMessage());
            return;
        }
        Log.e(TAG, "onBaseError: " + sb.toString());
//        FRToast.showToastSafe(t.getMessage());
        FRToast.showToastSafe(sb.toString());
    }
}