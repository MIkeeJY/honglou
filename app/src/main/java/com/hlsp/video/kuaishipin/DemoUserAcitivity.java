package com.hlsp.video.kuaishipin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hlsp.video.R;
import com.lightsky.video.VideoHelper;
import com.lightsky.video.sdk.VideoSwitcher;

import java.util.List;

public class DemoUserAcitivity extends Activity {

    private Button btlookv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_user_acitivity);
        btlookv = (Button) findViewById(R.id.lookvideo);
        btlookv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSwitcher switcher = new VideoSwitcher();
                VideoHelper.get().Init(DemoUserAcitivity.this, switcher, null);
                showVideoWrapper(null, DemoUserAcitivity.this, null);
            }
        });
    }

    private void showVideoWrapper(List<Integer> tabfilter, Activity activity, Intent intent) {
        boolean isshowActvity = true;

        if (isshowActvity) {
            VideoHelper.get().showVideoListActivity(tabfilter, activity, intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoHelper.get().unInit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }
}
