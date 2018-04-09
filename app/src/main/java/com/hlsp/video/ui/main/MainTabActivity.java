/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.hlsp.video.ui.main;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.baidu.mobstat.StatService;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.hlsp.video.App;
import com.hlsp.video.R;
import com.hlsp.video.base.BaseActivity;
import com.hlsp.video.ui.fragment.MainPageDouYinFragment;
import com.hlsp.video.ui.fragment.MineFragment;
import com.hlsp.video.ui.fragment.RecommondVideoFragment;
import com.hlsp.video.utils.StatusBarCompat;
import com.hlsp.video.utils.WindowUtil;
import com.hlsp.video.utils.statusbar.StatusBarFontHelper;
import com.hlsp.video.utils.update.SoftCheckUpdateUtil;
import com.lightsky.video.VideoHelper;
import com.lightsky.video.base.FollowingVideoFragment;

import java.util.HashMap;

/**
 * PS:activity_main.xml 跟360快视频SDK冲突 所以改名
 *
 * @author hackest
 * @date 2018-02-02 16:19:50
 */
public class MainTabActivity extends BaseActivity {

    private TabHost mTabHost;
    private TabManager mTabManager;
    private LayoutInflater inflater;

    private final String TAB1 = "MAIN";
    private final String TAB2 = "VIDEO";
    private final String TAB3 = "FOLLOWING";
    private final String TAB4 = "MINE";

    @Override
    protected int layoutRes() {
        return R.layout.activity_main_tab;
    }

    @Override
    protected void initView() {
        inflater = LayoutInflater.from(this);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabHost.getTabWidget().setDividerDrawable(null); // 去除分割线

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);

        mTabManager.addTab(mTabHost.newTabSpec(TAB1).setIndicator(createTabIndicatorView(R.layout.tab_main)), MainPageDouYinFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(TAB2).setIndicator(createTabIndicatorView(R.layout.tab_following)), RecommondVideoFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(TAB3).setIndicator(createTabIndicatorView(R.layout.tab_video)), FollowingVideoFragment.class, null);
        mTabManager.addTab(mTabHost.newTabSpec(TAB4).setIndicator(createTabIndicatorView(R.layout.tab_mine)), MineFragment.class, null);

        SoftCheckUpdateUtil checkUpdateUtil = new SoftCheckUpdateUtil();
        checkUpdateUtil.checkSoftUpdate(MainTabActivity.this, false, WindowUtil.getScreenWidth(App.getInstance()));
    }


    private View createTabIndicatorView(int layoutResource) {
        return inflater.inflate(layoutResource, null);
    }


    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost,
                          int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state. If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = mActivity.getSupportFragmentManager()
                    .findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager()
                        .beginTransaction();
                ft.hide(info.fragment);
                ft.commitAllowingStateLoss();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            int pos = mTabHost.getCurrentTab(); // To get tab position

            /*
              打点相关
             */
            if (pos == 0) {
                StatService.onEvent(mActivity, "aweme", "小视频");
            } else if (pos == 1) {
                StatService.onEvent(mActivity, "recommon_tab", "底部推荐");
            } else if (pos == 2) {
                StatService.onEvent(mActivity, "discover", "发现");
            }

            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager()
                        .beginTransaction();
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.hide(mLastTab.fragment);
                    }
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {
                        ft.show(newTab.fragment);
                    }
                }

                mLastTab = newTab;
                ft.commitAllowingStateLoss();
                mActivity.getSupportFragmentManager()
                        .executePendingTransactions();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (VideoViewManager.instance().getCurrentVideoPlayer() != null) {
            VideoViewManager.instance().getCurrentVideoPlayer().pause();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTabHost.getCurrentTab() == 0 || mTabHost.getCurrentTab() == 2) {
            return;
        }

        if (VideoViewManager.instance().getCurrentVideoPlayer() != null) {
            VideoViewManager.instance().getCurrentVideoPlayer().resume();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoViewManager.instance().releaseVideoPlayer();
    }


    private long mExitTime;

    @Override
    public void onBackPressed() {
        boolean iscanback = VideoHelper.get().isCanBack(this);
        if (!iscanback) {
            return;
        }

        if (!VideoViewManager.instance().onBackPressed()) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }


    }

    public TabHost getTabHost() {
        return mTabHost;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtils.e("横屏");
        } else {
            StatusBarCompat.setStatusBarColor(this, 0xfffffff);
            StatusBarFontHelper.setStatusBarMode(this, true);
        }
        super.onConfigurationChanged(newConfig);
    }

}
