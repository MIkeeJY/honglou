package com.hlsp.video.kuaishipin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.hlsp.video.R;
import com.lightsky.utils.ToastUtil;
import com.lightsky.video.VideoHelper;
import com.lightsky.video.datamanager.category.CategoryQueryNotify;
import com.lightsky.video.sdk.CategoryInfoBase;
import com.lightsky.video.sdk.VideoOption;
import com.lightsky.video.sdk.VideoSwitcher;
import com.lightsky.video.sdk.VideoTypesLoader;
import com.lightsky.video.sdk.listener.AdViewListener;
import com.lightsky.video.sdk.listener.VideoPlayListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.inflate;


public class DemoLuncherActivity extends Activity implements View.OnClickListener, CategoryQueryNotify {

    private Button bntmuti, bntsingle, bntsearch, bntinit, bntAll, bntdefinit;
    private CheckBox cbDebug, cbNbPlayer, cbUselog, cbUseShare, cbUseAdview, cbPlayCtrl, cbEnable;
    private VideoTypesLoader mTabLoader;
    private Map<String, Integer> mTabs = new HashMap<String, Integer>();
    private List<CategoryInfoBase> mTabinfos = new ArrayList<CategoryInfoBase>();
    private List<Integer> mTabFilter = new ArrayList<>();
    private VideoPlayListener ctrllistener = new VideoPlayListener() {
        @Override
        public void OnStart(Context cxt, String videoid) {
            String msg = "start play :" + videoid;
            ToastUtil.showLong(cxt, msg);
        }

        @Override
        public void OnPause(Context cxt, String videoid) {
            String msg = "pause :" + videoid;
            ToastUtil.showLong(cxt, msg);
        }

        @Override
        public void OnResume(Context cxt, String videoid) {
            String msg = "resume :" + videoid;
            ToastUtil.showLong(cxt, msg);
        }

        @Override
        public void OnPlayFinish(Context cxt, String videoid) {
            String msg = "play finished :" + videoid;
            ToastUtil.showLong(cxt, msg);
        }

        @Override
        public void OnPlayExit(Context cxt, String videoid) {
            String msg = "play exit :" + videoid;
            ToastUtil.showLong(cxt, msg);
        }

        @Override
        public void OnFullScreen(Context cxt, String videoid, boolean isfull) {
            String msg = (isfull ? "enter" : "exit") + " fullscreen : " + videoid;
            ToastUtil.showLong(cxt, msg);
        }

    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabLoader = new VideoTypesLoader();
        mTabLoader.Init(this);
        setContentView(R.layout.activity_demo_luncher);
        bntmuti = (Button) findViewById(R.id.mutitab);
        bntsingle = (Button) findViewById(R.id.singletab);
        bntsearch = (Button) findViewById(R.id.search);
        bntinit = (Button) findViewById(R.id.bnt_init);
        bntAll = (Button) findViewById(R.id.alltabs);
        bntdefinit = (Button) findViewById(R.id.bnt_default_init);


        if (bntsingle != null) {
            bntsingle.setOnClickListener(this);
        }
        if (bntmuti != null) {
            bntmuti.setOnClickListener(this);
        }
        if (bntsearch != null) {
            bntsearch.setOnClickListener(this);
        }
        if (bntinit != null) {
            bntinit.setOnClickListener(this);
        }
        if (bntAll != null) {
            bntAll.setOnClickListener(this);
        }
        if (bntdefinit != null) {
            bntdefinit.setOnClickListener(this);
        }

        cbDebug = (CheckBox) findViewById(R.id.debug_model);
        cbNbPlayer = (CheckBox) findViewById(R.id.usenbplayer);
        cbUselog = (CheckBox) findViewById(R.id.uselog);
        cbUseShare = (CheckBox) findViewById(R.id.useshare);
        cbUseAdview = (CheckBox) findViewById(R.id.useadview);
        cbPlayCtrl = (CheckBox) findViewById(R.id.playctrl);
        cbEnable = (CheckBox) findViewById(R.id.enablead);
        EnableFunctions(false);

    }

    private void showVideoWrapper(List<Integer> tabfilter, Activity activity, Intent intent) {
        RadioGroup view = (RadioGroup) findViewById(R.id.actorfrag);
        int id = view.getCheckedRadioButtonId();
        boolean isshowActvity = false;
        boolean isdynamic = false;
        if (id == R.id.rdb_useactivity) {
            isshowActvity = true;
        } else if (id == R.id.rdb_usefragment_d) {
            isdynamic = true;
        }
        if (isshowActvity) {
            VideoHelper.get().showVideoListActivity(tabfilter, activity, intent);
        } else if (isdynamic) {

            List<CategoryInfoBase> innterlist = new ArrayList<CategoryInfoBase>();
            if (tabfilter == null) {
                innterlist = mTabinfos;
            } else {
                for (CategoryInfoBase item : mTabinfos) {
                    if (tabfilter.contains(item.mId)) {
                        innterlist.add(item);
                    }
                }
            }

            TestFragmentActivity.StartdynamicFragmentActivity(this, innterlist);
        } else {
            TestFragmentActivity.StartStaticFragmentActivity(this, tabfilter);
        }
    }


    private void EnableFunctions(boolean enable) {
        int visible = enable ? View.VISIBLE : View.GONE;
        View mfun1 = findViewById(R.id.functions1);
        if (mfun1 != null) {
            mfun1.setVisibility(visible);
        }
        RadioGroup view = (RadioGroup) findViewById(R.id.actorfrag);
        if (view != null) {
            view.setVisibility(visible);
        }
        cbDebug.setEnabled(!enable);
        cbNbPlayer.setEnabled(!enable);
        cbUselog.setEnabled(!enable);
        cbUseShare.setEnabled(!enable);
        bntinit.setEnabled(!enable);
        bntdefinit.setEnabled(!enable);
        cbUseAdview.setEnabled(!enable);
        cbPlayCtrl.setEnabled(!enable);
        cbEnable.setEnabled(!enable);
    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }
        int id = view.getId();
        if (id == bntmuti.getId()) {
            showMultiChoiceDialog(view);
        } else if (id == bntsingle.getId()) {
            showSingleChoiceDialog(view);
        } else if (id == bntsearch.getId()) {
            VideoHelper.get().showVideoSearchActivity(this, getIntent());
        } else if (id == bntinit.getId()) {
            InitSdk();
        } else if (id == bntAll.getId()) {
            showVideoWrapper(null, this, getIntent());
        } else if (id == bntdefinit.getId()) {
            _InitSdk(null, null);
        }


    }

    private View.OnClickListener myprivatelistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int nid = v.getId();
            String strtitle = "";
            if (nid == R.id.ad_image) {
                strtitle = "image 1";
            } else if (nid == R.id.ad_image1) {
                strtitle = "image 2";

            } else if (nid == R.id.adbutton) {
                strtitle = "button 1";
            }
            new AlertDialog.Builder(v.getContext())
                    .setTitle("广告View 测试")
                    .setMessage("文告" + strtitle + "被点击")
                    .setPositiveButton("确定", null)
                    .show();
        }
    };
    private AdViewListener mDetailAdBigimg = new AdViewListener() {
        @Override
        public View GetView(Context cxt, String vtitle, String vtag[]) {

            LinearLayout contianview = (LinearLayout) inflate(cxt, R.layout.banner_ad_image, null);
            contianview.findViewById(R.id.ad_image).setOnClickListener(myprivatelistener);
            contianview.findViewById(R.id.ad_image1).setOnClickListener(myprivatelistener);
            contianview.findViewById(R.id.adbutton).setOnClickListener(myprivatelistener);
            return contianview;
        }
    };

    private void InitSdk() {
        VideoSwitcher setting = new VideoSwitcher();
        if (cbDebug != null) {
            setting.Debugmodel = cbDebug.isChecked();

        } else {
            setting.Debugmodel = false;
        }
        if (cbNbPlayer != null) {
            setting.UseNbPlayer = cbNbPlayer.isChecked();
        } else {
            setting.UseNbPlayer = true;
        }
        setting.UseFileLog = false;
        setting.UseLogCatLog = cbUselog.isChecked();
        setting.UseShareLayout = cbUseShare.isChecked();
        setting.incomeEable = cbEnable.isChecked();
        VideoOption option = new VideoOption();
        boolean ischeck = cbUseAdview.isChecked();
        if (ischeck) {
            option.bigImgListener = mDetailAdBigimg;
        }
        if (cbPlayCtrl.isChecked()) {
            option.playCtrlListener = ctrllistener;
        }
        _InitSdk(setting, option);

    }

    private void _InitSdk(VideoSwitcher setting, VideoOption opt) {
        VideoHelper.get().Init(this, setting, opt);
        mTabLoader.loadData();
        EnableFunctions(true);
    }

    private void showSingleChoiceDialog(View view) {
        if (!mTabLoader.HasData()) {
            return;
        }
        mTabFilter.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.hongyan_icon);
        builder.setTitle("请选择一个要展示的频道");
        builder.setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showVideoWrapper(mTabFilter, DemoLuncherActivity.this, getIntent());
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        /**
         * 设置内容区域为单选列表项
         */

        final String[] items = new String[mTabs.size()];
        int index = 0;
        for (String key : mTabs.keySet()) {
            items[index++] = key;
        }
        int defaultindex = 0;
        mTabFilter.add(mTabs.get(items[defaultindex]));
        builder.setSingleChoiceItems(items, defaultindex, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTabFilter.clear();
                int nid = mTabs.get(items[i]);
                mTabFilter.add(nid);
            }
        });

        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showMultiChoiceDialog(View view) {
        if (!mTabLoader.HasData()) {
            return;
        }
        mTabFilter.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.hongyan_icon);
        builder.setTitle("请选择要展示的频道");
        builder.setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showVideoWrapper(mTabFilter, DemoLuncherActivity.this, getIntent());
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        /**
         * 设置内容区域为多选列表项
         */
        final String[] items = new String[mTabs.size()];
        boolean[] checks = new boolean[mTabs.size()];
        int index = 0;
        for (String key : mTabs.keySet()) {
            items[index] = key;
            checks[index++] = true;
            mTabFilter.add(mTabs.get(key));
        }
        builder.setMultiChoiceItems(items, checks, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                Integer nid = mTabs.get(items[i]);
                if (b) {
                    mTabFilter.add(nid);
                }
                mTabFilter.remove(nid);

            }
        });


        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onCategoryQueryFinish(boolean bSuccess, List<CategoryInfoBase> infos) {
        mTabs.clear();
        mTabinfos.clear();
        for (CategoryInfoBase item : infos) {
            mTabs.put(item.name, item.mId);
            mTabinfos.add(item);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoHelper.get().unInit();
    }
}
