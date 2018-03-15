package com.hlsp.video.ui.login;


import com.hlsp.video.base.BaseRequestContract;

/**
 * Created by jack on 2017/6/13
 */

public interface LoginView<T> extends BaseRequestContract<T> {

    String getUserName();

    String getPassword();

}