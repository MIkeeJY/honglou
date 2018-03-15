package com.hlsp.video.presenter.login;

import com.hlsp.video.base.BasePresenter;
import com.hlsp.video.bean.LoginResult;
import com.hlsp.video.model.login.LoginModel;
import com.hlsp.video.ui.login.LoginView;
import com.hlsp.video.utils.Utils;

import cn.share.jack.cyghttp.HttpData;
import cn.share.jack.cyghttp.callback.BaseImpl;
import cn.share.jack.cyghttp.callback.CygBaseObserver;

/**
 * Created by jack on 2017/6/13
 */

public class LoginPresenter extends BasePresenter<LoginView<HttpData<LoginResult>>> {

    public LoginPresenter(LoginView<HttpData<LoginResult>> loginView) {
        attachView(loginView);
    }


    public void getUserInfo(BaseImpl baseImpl) {
        LoginModel.getInstance().execute(getView().getUserName(), getView().getPassword(), 1, Utils.getUniquePsuedoID(), new CygBaseObserver<HttpData<LoginResult>>(baseImpl, "正在登录") {
            @Override
            protected void onBaseNext(HttpData<LoginResult> data) {
//                UserInfo userInfo = new UserInfo();
//                userInfo.setId(data.getId());
//                userInfo.setUsername(getView().getUserName());
//                userInfo.setToken(data.getToken());
//                UserDao.getInstance().deleteAll(UserInfo.class);
//                UserDao.getInstance().insertObject(userInfo);
                getView().onRequestSuccessData(data);
            }
        });
    }

//    public void toMainActivity(Context context) {
//        context.startActivity(new Intent(context, MainPtrActivity.class));
//    }
}
