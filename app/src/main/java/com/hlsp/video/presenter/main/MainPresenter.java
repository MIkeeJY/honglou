package com.hlsp.video.presenter.main;

import com.hlsp.video.base.BasePresenter;
import com.hlsp.video.model.main.MainInfo;
import com.hlsp.video.model.main.MainModel;
import com.hlsp.video.ui.main.MainView;

import java.util.List;

import cn.share.jack.cyghttp.callback.BaseImpl;
import cn.share.jack.cyghttp.callback.CygBaseObserver;

/**
 * Created by jack on 2017/6/14
 */

public class MainPresenter extends BasePresenter<MainView<List<MainInfo>>> {

    public MainPresenter(MainView<List<MainInfo>> mainView) {
        attachView(mainView);
    }

    public void getArticleData(BaseImpl baseImpl) {
        MainModel.getInstance().execute(new CygBaseObserver<List<MainInfo>>(baseImpl) {
            @Override
            protected void onBaseNext(List<MainInfo> data) {
                getView().onRequestSuccessData(data);
            }

            @Override
            protected void onBaseError(Throwable t) {
                super.onBaseError(t);
                getView().getArticleDataFailure(t);
            }
        });
    }
}