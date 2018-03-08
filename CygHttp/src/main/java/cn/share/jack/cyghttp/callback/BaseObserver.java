package cn.share.jack.cyghttp.callback;


import android.util.Log;

import cn.share.jack.cyghttp.progress.ProgressDialogHandler;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 *
 */

public abstract class BaseObserver<T> implements Observer<T> {

    private static final String TAG = "BaseObserver";

    protected abstract void onBaseError(Throwable t);

    protected abstract void onBaseNext(T data);

    protected abstract boolean isNeedProgressDialog();

    protected abstract String getTitleMsg();

    private ProgressDialogHandler mProgressDialogHandler;
    private BaseImpl mBaseImpl;

    public BaseObserver(BaseImpl baseImpl) {
        mBaseImpl = baseImpl;
        if (null != mBaseImpl) {
            if (null == mProgressDialogHandler) {
                mProgressDialogHandler = new ProgressDialogHandler(baseImpl.getContext(), true);
            }
        }
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG, getTitleMsg()).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        //显示进度条
        if (isNeedProgressDialog()) {
            showProgressDialog();
        }
        if (null != mBaseImpl) {
            if (null != d) {
                mBaseImpl.addDisposable(d);
            }
        }
    }

    @Override
    public void onNext(T value) {
        //成功
        Log.d(TAG, "http is onNext");
        if (null != value) {
            onBaseNext(value);
        }
    }

    @Override
    public void onError(Throwable e) {
        //关闭进度条
        Log.e(TAG, "http is onError");
        if (isNeedProgressDialog()) {
            dismissProgressDialog();
        }
        onBaseError(e);
    }

    @Override
    public void onComplete() {
        //关闭进度条
        if (isNeedProgressDialog()) {
            dismissProgressDialog();
        }
    }
}