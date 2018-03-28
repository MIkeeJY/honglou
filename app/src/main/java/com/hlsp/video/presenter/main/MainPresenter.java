package com.hlsp.video.presenter.main;

import com.hlsp.video.base.BasePresenter;
import com.hlsp.video.bean.ChannelListItem;
import com.hlsp.video.ui.main.MainView;

import java.util.List;

import cn.share.jack.cyghttp.callback.BaseImpl;

/**
 * Created by jack on 2017/6/14
 */

public class MainPresenter extends BasePresenter<MainView<List<ChannelListItem>>> {

    public MainPresenter(MainView<List<ChannelListItem>> mainView) {
        attachView(mainView);
    }

    public void getChannelData(BaseImpl baseImpl) {
//        MainModel.getInstance().executeChannelList(new CygBaseObserver<BaseResponse<ChannelListData>>() {
//            @Override
//            protected void onBaseNext(BaseResponse<ChannelListData> data) {
//
//            }
//        });

    }

//    public void getVideoListData(BaseImpl baseImpl) {
//        MainModel.getInstance().executeChannelList(new CygBaseObserver<LoginResult>() {
//            @Override
//            protected void onBaseNext(LoginResult data) {
////                getView().onRequestSuccessData(data);
//            }
//        });

//    }
}