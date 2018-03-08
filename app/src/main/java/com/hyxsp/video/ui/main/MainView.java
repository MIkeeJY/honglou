package com.hyxsp.video.ui.main;


import com.hyxsp.video.base.BaseRequestContract;

/**
 * Created by jack on 2017/6/14
 */

public interface MainView<T> extends BaseRequestContract<T> {

    void getArticleDataFailure(Throwable t);
}