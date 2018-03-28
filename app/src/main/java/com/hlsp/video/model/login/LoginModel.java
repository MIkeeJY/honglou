package com.hlsp.video.model.login;


import com.hlsp.video.base.BaseModel;
import com.hlsp.video.bean.LoginResult;
import com.hlsp.video.utils.GsonUtil;

import cn.share.jack.cyghttp.BaseResponse;
import cn.share.jack.cyghttp.HttpFunction;
import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.RequestBody;

/**
 * Created by jack on 2017/6/13
 */

public class LoginModel extends BaseModel {

    public static LoginModel getInstance() {
        return getPresent(LoginModel.class);
    }

    /**
     * @param account    （邮箱或第三方id）
     * @param credential (密码或第三方token)
     * @param type       (注册方式 1,邮箱，2，微信，3，QQ，4，微博 )
     * @param device_id  （设备id）
     */
    public void execute(String account, String credential, final int type, String device_id, Observer<BaseResponse<LoginResult>> observer) {
        addParams("account", account);
        addParams("credential", credential);
        addParams("type", type + "");
        addParams("device_id", device_id);

        RequestBody requestBody = RequestBody.create(JSON, GsonUtil.mapToJson(mParams));

        Observable observable = mServletApi.login(requestBody).map(new HttpFunction());
        toSubscribe(observable, observer);
    }
}