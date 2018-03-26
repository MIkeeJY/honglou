package com.hlsp.video.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Unbinder;

/**
 * 处理fragment预加载
 * Created by hackest on 2016/11/9.
 */

public abstract class BaseLoadFragment extends Fragment {

    protected boolean hasInited;
    public BaseActivity context;
    protected boolean isVisibleToUser;


    protected Unbinder mUnbinder;

    protected abstract int layoutRes();

    protected void onViewReallyCreated(View view) {
    }

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (BaseActivity) getActivity();
    }


    public BaseActivity getContext() {
        return context;
    }


    // 当前Fragment 是否可见
    protected boolean isVisible = false;
    // 是否加载过数据
    protected boolean isLoadData = false;
    protected boolean isViewInit = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
        preLoadData(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.isViewInit = true;
        // 防止一开始加载的时候未 调用 preLoadData 方法， 因为setUserVisibleHint 比 onActivityCreated 触发 前
        if (getUserVisibleHint()) {
            preLoadData(false);
        }
    }


    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == rootView) {
            rootView = inflater.inflate(layoutRes(), null);
            onViewReallyCreated(rootView);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }


    /**
     * 当UI初始化成功，UI可见并且没有加载过数据的时候 加载数据
     *
     * @param forceLoad 强制加载数据
     */
    public void preLoadData(boolean forceLoad) {
        if (isViewInit && isVisible && (!isLoadData || forceLoad)) {
            lazyLoad();
            isLoadData = true;
        }
    }

    /**
     * 延迟加载
     * 子类必须重写此方法,这个方法只会调用一次,相当于activity的onCreate
     */
    protected abstract void lazyLoad();


    /**
     * 第一次不会调用这个方法,第一次会调用{@link #lazyLoad()},这样来保证数据加载一次
     * 当重新回到这个页面会调用这个方法
     */
//    protected void onMyResume() {
//
//    }
//
//    /**
//     * 页面失去焦点
//     */
//    protected void onMyPause() {
//
//    }
    @Override
    public void onDestroyView() {
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();
        }

        super.onDestroyView();
    }

}
