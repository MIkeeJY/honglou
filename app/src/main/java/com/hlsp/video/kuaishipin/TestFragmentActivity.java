package com.hlsp.video.kuaishipin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlsp.video.R;
import com.lightsky.video.VideoHelper;
import com.lightsky.video.sdk.CategoryInfoBase;
import com.lightsky.video.sdk.listener.PlayerControler;

import java.util.ArrayList;
import java.util.List;

public class TestFragmentActivity extends FragmentActivity implements View.OnClickListener {

    public static void StartdynamicFragmentActivity(Context cxt, List<CategoryInfoBase> infos) {
        if (cxt == null) {
            return;
        }
        if (infos == null) {
            return;
        }
        Bundle start = new Bundle();
        int nsize = infos.size();
        int index = 0;
        Parcelable[] types = new Parcelable[nsize];
        for (CategoryInfoBase info : infos) {
            types[index++] = info;
        }
        start.putParcelableArray("video_types", types);
        StartFragmentActivy(cxt, start);

    }

    public static void StartStaticFragmentActivity(Context cxt, List<Integer> tabfilter) {
        VideoHelper.get().SetVideoTabFilter(tabfilter);
        StartFragmentActivy(cxt, null);
    }

    public static void StartFragmentActivy(Context cxt, Bundle bundle) {

        Intent intent = new Intent();
        String dynamickey = "isdynamic";
        if (bundle != null) {
            intent.putExtras(bundle);
            intent.putExtra(dynamickey, true);
        } else {
            intent.putExtra(dynamickey, false);
        }
        intent.setClass(cxt, TestFragmentActivity.class);
        if (!(cxt instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        cxt.startActivity(intent);
    }


    private HorizontalScrollView mTabsView;
    private ViewPager mPager;
    private DynamicFragmentAdpter madpter;
    private TextView mLastSelectView;
    private PlayerControler mplayctrl;

    private void setButtonclickListner(int id) {
        View view = findViewById(id);
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        mTabsView = (HorizontalScrollView) findViewById(R.id.video_type_tab);

        mPager = (ViewPager) findViewById(R.id.video_pager_fragments);

        boolean isDynamic = getIntent().getBooleanExtra("isdynamic", false);
        if (isDynamic) {

            Bundle bundle = getIntent().getExtras();
            InitScollView(bundle);

        } else {
            setButtonclickListner(R.id.ctrl_pause);
            setButtonclickListner(R.id.ctrl_resume);
            setButtonclickListner(R.id.ctrl_stop);
        }
        {
            RelativeLayout dynamiclayout = (RelativeLayout) findViewById(R.id.test_fragment_dynamic);
            dynamiclayout.setVisibility(isDynamic ? View.VISIBLE : View.GONE);
            RelativeLayout staticlayout = (RelativeLayout) findViewById(R.id.test_fragment_static);
            staticlayout.setVisibility(isDynamic ? View.GONE : View.VISIBLE);

        }


    }

    public void InitScollView(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        Parcelable[] types = bundle.getParcelableArray("video_types");
        if (types == null) {
            return;
        }
        List<CategoryInfoBase> mTabList = new ArrayList<CategoryInfoBase>();
        LinearLayout tabsContainer = new LinearLayout(this);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        int index = 0;

        for (Parcelable item : types) {

            CategoryInfoBase info = (CategoryInfoBase) item;
            mTabList.add(info);

            TextView scoritem = (TextView) getLayoutInflater().inflate(R.layout.videotabsitem, null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (index == 0) {
                params.setMargins(10, 2, 5, 5);
                mLastSelectView = scoritem;
                mLastSelectView.setBackgroundResource(R.color.colorAccent);
            } else {
                params.setMargins(5, 2, 5, 5);
            }
            /*scoritem.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            scoritem.setTextColor(0xFFFFFF);*/
            scoritem.setTag(index);
            scoritem.setLayoutParams(params);
            scoritem.setText(info.name);
            scoritem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer indexitem = (Integer) view.getTag();
                    int intdedex = indexitem.intValue();
                    if (intdedex < 0 || intdedex >= madpter.getCount()) {
                        return;
                    }
                    mPager.setCurrentItem(intdedex, false);
                    mLastSelectView.setBackgroundResource(R.color.colorPrimary);
                    view.setBackgroundResource(R.color.colorAccent);
                    mLastSelectView = (TextView) view;
                    AutoScroll(view);

                }
            });
            tabsContainer.addView(scoritem);
            index++;
        }
        mTabsView.addView(tabsContainer);
        madpter = new DynamicFragmentAdpter(getSupportFragmentManager(), mTabList);
        mPager.setAdapter(madpter);
        mPager.setOffscreenPageLimit(3);
        mPager.setCurrentItem(0);

    }

    public void AutoScroll(View Textview) {
        int screenWitdth = getResources().getDisplayMetrics().widthPixels;

        int left = Textview.getLeft();     //获取点击控件与父控件左侧的距离
        int width = Textview.getMeasuredWidth();   //获得控件本身宽度
        int toX = left + width / 2 - screenWitdth / 2;
        //使条目移动到居中显示
        mTabsView.smoothScrollTo(toX, 0);

    }

    @Override
    protected void onDestroy() {
        VideoHelper.get().OnActivityDestroy(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        boolean iscanback = VideoHelper.get().isCanBack(this);
        if (!iscanback) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int nid = v.getId();
        if (nid == R.id.ctrl_pause) {

            if (mplayctrl == null) {
                mplayctrl = VideoHelper.get().getPlayerControler(this);
            }
            if (mplayctrl != null) {
                mplayctrl.pauseVideo();
            }
        } else if (nid == R.id.ctrl_resume) {
            if (mplayctrl == null) {
                mplayctrl = VideoHelper.get().getPlayerControler(this);
            }
            if (mplayctrl != null) {
                mplayctrl.resumevideo();
            }

        } else if (nid == R.id.ctrl_stop) {
            if (mplayctrl == null) {
                mplayctrl = VideoHelper.get().getPlayerControler(this);
            }
            if (mplayctrl != null) {
                mplayctrl.stopVideo();
            }
        }
    }
}
