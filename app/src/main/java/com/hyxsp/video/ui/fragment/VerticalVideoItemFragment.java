package com.hyxsp.video.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyxsp.video.R;

/**
 * Created by hackest on 2018/3/5.
 */

public class VerticalVideoItemFragment extends Fragment {


    public static VerticalVideoItemFragment newInstance() {
        Bundle args = new Bundle();
        VerticalVideoItemFragment fragment = new VerticalVideoItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        return view;
    }



}
