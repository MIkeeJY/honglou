package com.hlsp.video.kuaishipin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.lightsky.video.VideoHelper;
import com.lightsky.video.sdk.CategoryInfoBase;

import java.util.List;

/**
 * Created by zheng on 2018/1/9.
 */

public class DynamicFragmentAdpter extends FragmentPagerAdapter {
    private List<CategoryInfoBase> mCateInfoBase;

    public DynamicFragmentAdpter(FragmentManager fm,List<CategoryInfoBase> tabs) {
        super(fm);
        mCateInfoBase=tabs;
    }

    @Override
    public Fragment getItem(int position) {
        if(mCateInfoBase == null){
            return null;
        }
        if(position <0 || position>=mCateInfoBase.size()){
            return null;
        }
        CategoryInfoBase item=mCateInfoBase.get(position);
        return VideoHelper.get().GetVideoFragment(item);
    }

    @Override
    public int getCount() {
        return mCateInfoBase.size();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
        super.destroyItem(container, position, object);
    }

}
