package com.hlsp.video.kuaishipin.callback;

import android.content.Context;

import com.lightsky.utils.ToastUtil;
import com.lightsky.video.sdk.listener.VideoPlayListener;

/**
 * Created by zheng on 2018/4/14.
 */

public class VideoPlayCtroler implements VideoPlayListener {
    @Override
    public void OnStart(Context cxt, String videoid,String postfix) {
        String msg="start play : "+videoid+" postfix: "+postfix;
        ToastUtil.showLong(cxt,msg);
    }

    @Override
    public void OnPause(Context cxt, String videoid,String postfix) {
        String msg="pause :"+videoid+" postfix: "+postfix;
        ToastUtil.showLong(cxt,msg);
    }

    @Override
    public void OnResume(Context cxt, String videoid,String postfix) {
        String msg="resume :"+videoid+" postfix: "+postfix;
        ToastUtil.showLong(cxt,msg);
    }

    @Override
    public void OnPlayFinish(Context cxt, String videoid,String postfix) {
        String msg="play finished :"+videoid+" postfix: "+postfix;
        ToastUtil.showLong(cxt,msg);
    }

    @Override
    public void OnPlayExit(Context cxt, String videoid,String postfix) {
        String msg="play exit :"+videoid+" postfix: "+postfix;
        ToastUtil.showLong(cxt,msg);
    }

    @Override
    public void OnFullScreen(Context cxt, String videoid, boolean isfull,String postfix) {
        String msg=(isfull?"enter":"exit")+" fullscreen : "+videoid+" postfix: "+postfix;
        ToastUtil.showLong(cxt,msg);
    }
}
