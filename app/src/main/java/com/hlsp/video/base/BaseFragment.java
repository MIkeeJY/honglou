package com.hlsp.video.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;

import butterknife.Unbinder;

/**
 * Created by jack on 2017/6/14
 */

public abstract class BaseFragment<PRESENTER extends BasePresenter> extends Fragment {

    private static final String TAG = "BaseFragment";

    public BaseActivity context;

    protected Unbinder mUnbinder;

    protected abstract int layoutRes();

    protected void onViewReallyCreated(View view) {
    }

    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (BaseActivity) getActivity();
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public <VIEW extends View> VIEW findView(int id) {
        if (null != rootView) {
            View child = rootView.findViewById(id);
            try {
                return (VIEW) child;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "findView: " + String.valueOf(e.getMessage()));
                return null;
            }
        }
        return null;
    }


    @Override
    public void onPause() {
        super.onPause();
        // 页面埋点
        StatService.onPageStart(getActivity(), getActivity().getClass().getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        // 页面埋点
        StatService.onPageEnd(getActivity(), getActivity().getClass().getName());
    }


    @Override
    public void onDestroyView() {
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();
        }

        super.onDestroyView();
    }
}