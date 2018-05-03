package com.hlsp.video.kuaishipin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.hlsp.video.R;
import com.lightsky.video.VideoHelper;
import com.lightsky.video.datamanager.category.CategoryQueryNotify;
import com.lightsky.video.sdk.CategoryInfoBase;
import com.lightsky.video.sdk.VideoTypesLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DemoLuncherActivity extends Activity implements View.OnClickListener, CategoryQueryNotify {

    private Button bntmuti, bntsingle, bntAll;
    private ImageView settingimg, tabsearch;
    private VideoTypesLoader mTabLoader;
    private Map<String, Integer> mTabs = new HashMap<>();
    private List<CategoryInfoBase> mTabinfos = new ArrayList<>();
    private List<Integer> mTabFilter = new ArrayList<>();

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
        tabsearch = (ImageView) findViewById(R.id.tab_search);
        settingimg = (ImageView) findViewById(R.id.settingicon);
        if (settingimg != null) {
            settingimg.setOnClickListener(this);
        }
        if (tabsearch != null) {
            tabsearch.setOnClickListener(this);
        }
        bntAll = (Button) findViewById(R.id.alltabs);


        if (bntsingle != null) {
            bntsingle.setOnClickListener(this);
        }
        if (bntmuti != null) {
            bntmuti.setOnClickListener(this);
        }

        if (bntAll != null) {
            bntAll.setOnClickListener(this);
        }
        mTabLoader.loadData();

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
        } else if (id == tabsearch.getId()) {
            VideoHelper.get().showVideoSearchActivity(this, getIntent());
        } else if (id == bntAll.getId()) {
            showVideoWrapper(null, this, getIntent());
        } else if (id == settingimg.getId()) {
//            Intent intent = new Intent();
//            intent.setClass(this, SettingActivity.class);
//            startActivity(intent);
        }


    }


    private void showSingleChoiceDialog(View view) {
        if (!mTabLoader.HasData()) {
            return;
        }
        mTabFilter.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.honglou_icon);
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
        builder.setIcon(R.mipmap.honglou_icon);
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
    }
}
