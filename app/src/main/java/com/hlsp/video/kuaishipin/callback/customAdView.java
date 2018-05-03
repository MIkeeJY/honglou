package com.hlsp.video.kuaishipin.callback;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.hlsp.video.R;
import com.lightsky.video.sdk.listener.AdViewListener;
import com.lightsky.video.sdk.listener.IncomeAdLoadListener;

import static android.view.View.inflate;

/**
 * Created by zheng on 2018/4/14.
 */

public class customAdView implements AdViewListener ,View.OnClickListener{
    @Override
    public boolean playNextAd() {
        return false;
    }

    @Override
    public View GetView(Context cxt, String vtitle, String vtag[], IncomeAdLoadListener callback) {

        LinearLayout contianview=(LinearLayout)inflate(cxt, R.layout.banner_ad_image,null);
        contianview.findViewById(R.id.ad_image).setOnClickListener(this);
        contianview.findViewById(R.id.ad_image1).setOnClickListener(this);
        contianview.findViewById(R.id.adbutton).setOnClickListener(this);
        return  contianview;
    }
    @Override
    public void onClick(View v) {
        int nid=v.getId();
        String strtitle="";
        if(nid == R.id.ad_image){
            strtitle="image 1";
        }else if(nid == R.id.ad_image1){
            strtitle="image 2";

        }else if(nid == R.id.adbutton){
            strtitle="button 1";
        }
        new AlertDialog.Builder(v.getContext())
                .setTitle("广告View 测试")
                .setMessage("文告"+strtitle+"被点击")
                .setPositiveButton("确定", null)
                .show();
    }
}
