package com.hlsp.video.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hlsp.video.R;
import com.jack.mc.cyg.cygptr.PtrFrameLayout;
import com.jack.mc.cyg.cygptr.PtrUIHandler;
import com.jack.mc.cyg.cygptr.indicator.PtrIndicator;
import com.jack.mc.cyg.cygptr.indicator.PtrTensionIndicator;

/**
 * Created by hackest on 2018/4/10.
 */

public class MyCustomHeader extends FrameLayout implements PtrUIHandler {
    ImageView iv_refresh;
    ProgressBar progressbar_pull;
    TextView tv_pull_title;
    private Matrix mMatrix = new Matrix();

    private PtrFrameLayout mPtrFrameLayout;
    private PtrTensionIndicator mPtrTensionIndicator;
    private Handler mHandler = new Handler();

    private Animation mScale;

    private Runnable mPullRRunnable = new Runnable() {
        @Override
        public void run() {
            progressbar_pull.setVisibility(GONE);
            tv_pull_title.setVisibility(VISIBLE);
            tv_pull_title.startAnimation(mScale);
        }
    };

    public MyCustomHeader(@NonNull Context context) {
        this(context, null);
    }

    public MyCustomHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header_layout, this);
        iv_refresh = view.findViewById(R.id.iv_refresh);
        progressbar_pull = view.findViewById(R.id.progressbar_pull);
        tv_pull_title = view.findViewById(R.id.tv_pull_title);
        progressbar_pull.setVisibility(GONE);
        tv_pull_title.setVisibility(GONE);

        mScale = new ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1);
        mScale.setDuration(200);


    }

    /**
     * 刷新重置 : Content重新回到顶部Header消失，重置 View。
     *
     * @param frame
     */
    @Override
    public void onUIReset(PtrFrameLayout frame) {
        iv_refresh.setVisibility(GONE);
        progressbar_pull.setVisibility(GONE);
        tv_pull_title.setVisibility(GONE);
        tv_pull_title.clearAnimation();
    }

    /**
     * 刷新准备 : Header 将要出现时调用。
     *
     * @param frame
     */
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mHandler.removeCallbacks(mPullRRunnable);
        tv_pull_title.clearAnimation();
        pullStep0(0.0f);
    }

    /**
     * 开始刷新 : Header 进入刷新状态之前调用。
     *
     * @param frame
     */
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mPtrFrameLayout.setEnabled(false);

        iv_refresh.setVisibility(GONE);
        tv_pull_title.setVisibility(GONE);
        progressbar_pull.setVisibility(VISIBLE);
        mHandler.postDelayed(mPullRRunnable, 500);
    }


    /**
     * 刷新结束 : Header 开始向上移动之前调用。
     *
     * @param frame
     */
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        iv_refresh.setVisibility(GONE);
        progressbar_pull.setVisibility(GONE);
        tv_pull_title.setVisibility(VISIBLE);

        mPtrFrameLayout.setEnabled(true);

    }


    /**
     * isUnderTouch: 是否松手（true、false）
     * status: 状态（1:init、2:prepare、3:loading、4：complete）
     */
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status,
                                   PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();//可释放刷新位置
        final int currentPos = ptrIndicator.getCurrentPosY();//当前位置
        final int lastPos = ptrIndicator.getLastPosY();//前一个位置

        if (isUnderTouch) {//下拉过程的两个阶段：到达可释放刷新位置前后
            if (lastPos < currentPos && currentPos < mOffsetToRefresh) {//调用2
                float scale = lastPos * 5 / 4 / (float) mOffsetToRefresh;
                if (scale > 1.0f) {
                    scale = 1.0f;
                }
                pullStep0(scale);
            } else {
                float scale = lastPos / (float) mOffsetToRefresh;
                if (scale > 1.0f) {
                    scale = 1.0f;
                }
                pullStep0(scale);

            }
        }


    }

    private void pullStep0(float scale) {
        iv_refresh.setVisibility(VISIBLE);
        progressbar_pull.setVisibility(GONE);
        tv_pull_title.setVisibility(GONE);

        scaleImage(scale);
    }


    private void scaleImage(float scale) {
        mMatrix.setScale(scale, scale, iv_refresh.getWidth() / 2, iv_refresh.getHeight() / 2);
        iv_refresh.setImageMatrix(mMatrix);
    }


    public void setUp(PtrFrameLayout ptrFrameLayout) {
        mPtrFrameLayout = ptrFrameLayout;
        mPtrTensionIndicator = new PtrTensionIndicator();
        mPtrFrameLayout.setPtrIndicator(mPtrTensionIndicator);
    }


    public TextView getTvtitle() {
        return tv_pull_title;
    }
}

