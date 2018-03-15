package com.hlsp.video.ui.login;

//import android.support.design.widget.TextInputEditText;
import com.apkfuns.logutils.LogUtils;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseActivity;
import com.hlsp.video.bean.LoginResult;
import com.hlsp.video.presenter.login.LoginPresenter;

import cn.share.jack.cyghttp.HttpData;

/**
 * Created by jack on 2017/6/13
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView<HttpData<LoginResult>> {

//    @BindView(R.id.al_et_user_name)
//    TextInputEditText alEtUserName;
//    @BindView(R.id.al_et_password)
//    TextInputEditText alEtPassword;

    @Override
    protected int layoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initView() {

    }

//    @Override
//    public String getUserName() {
//        return alEtUserName.getText().toString().trim();
//    }
//
//    @Override
//    public String getPassword() {
//        return alEtPassword.getText().toString().trim();
//    }
//
//    @OnClick(R.id.al_btn_login)
//    public void onViewClicked() {
//        if (TextUtils.isEmpty(getUserName())) {
//            alEtPassword.setError("用户名不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(getPassword())) {
//            alEtPassword.setError("密码不能为空");
//            return;
//        }
//        mPresenter.getUserInfo(this);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public void onRequestSuccessData(HttpData<LoginResult> data) {

        LogUtils.e(data);
//        mPresenter.toMainActivity(this);
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }
}