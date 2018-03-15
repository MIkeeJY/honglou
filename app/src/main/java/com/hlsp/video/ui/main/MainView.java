package com.hlsp.video.ui.main;


import com.hlsp.video.base.BaseRequestContract;

/**
 * Created by jack on 2017/6/14
 */

public interface MainView<T> extends BaseRequestContract<T> {

    void getArticleDataFailure(Throwable t);
}