package com.hlsp.video.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mobstat.StatService;
import com.hlsp.video.utils.StatusBarCompat;

import butterknife.ButterKnife;
import cn.share.jack.cyghttp.callback.BaseImpl;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by jack on 2017/6/13
 */

public abstract class BaseActivity<PRESENTER extends BasePresenter> extends AppCompatActivity implements BaseImpl {

    private CompositeDisposable mCompositeDisposable;

    protected PRESENTER mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == mCompositeDisposable) {
            mCompositeDisposable = new CompositeDisposable();
        }
        if (null == mPresenter) {
            mPresenter = createPresenter();
        }

        setContentView(layoutRes());
        StatusBarCompat.translucentStatusBar(this, true);
        ButterKnife.bind(this);
        initView();
    }

    protected abstract int layoutRes();

    protected PRESENTER createPresenter() {
        return null;
    }

    protected abstract void initView();

    @Override
    public boolean addDisposable(Disposable disposable) {
        if (null != mCompositeDisposable) {
            mCompositeDisposable.add(disposable);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCompositeDisposable) {
            mCompositeDisposable.clear();
        }
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 页面埋点，需要使用Activity的引用，以便代码能够统计到具体页面名
        StatService.onPageStart(this, getClass().getName());

        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 页面结束埋点，需要使用Activity的引用，以便代码能够统计到具体页面名
        StatService.onPageEnd(this, getClass().getName());

        StatService.onPause(this);
    }

}
