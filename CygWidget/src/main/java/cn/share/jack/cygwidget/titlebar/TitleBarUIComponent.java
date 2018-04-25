package cn.share.jack.cygwidget.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.share.jack.cygwidget.R;


/**
 * Created by jack on 2017/6/13
 */

public class TitleBarUIComponent extends RelativeLayout {
    public TitleBarUIComponent(Context context) {
        super(context);
    }

    public TitleBarUIComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleBarUIComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private TextView tvLeft;
    private TextView tvTitle;
    private TextView tvRight;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvLeft = (TextView) findViewById(R.id.lt_titlebar_left);
        tvTitle = (TextView) findViewById(R.id.lt_titlebar_title);
        tvRight = (TextView) findViewById(R.id.lt_titlebar_right);
    }

    public void initLeft(String text, OnClickListener listener) {
        tvLeft.setText(text);
        tvLeft.setOnClickListener(listener);
    }

    public void setLeftTextContent(String text) {
        tvLeft.setText(String.valueOf(text));
    }

    public void initLeft(int drawableRes, OnClickListener listener) {
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0);
        tvLeft.setOnClickListener(listener);
    }

    public void initLeftBack(OnClickListener listener) {
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_back_white, 0, 0, 0);
        tvLeft.setOnClickListener(listener);
    }

    public void initLeft(String text, int drawableRes, OnClickListener listener) {
        tvLeft.setText(String.valueOf(text));
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0);
        tvLeft.setOnClickListener(listener);
    }

    public void initTitle(String text, int color) {
        tvTitle.setText(String.valueOf(text));
        tvTitle.setTextColor(color);
    }

    public void initTitle(int drawableRes, int color) {
        tvTitle.setBackgroundResource(drawableRes);
        tvTitle.setTextColor(color);
    }

    public void initRight(String text, OnClickListener listener) {
        tvTitle.setText(String.valueOf(text));
        tvTitle.setOnClickListener(listener);
    }

    public void initRight(int drawableRes, OnClickListener listener) {
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0);
        tvRight.setOnClickListener(listener);
    }

    public void setRightTextColor(int color) {
        tvRight.setTextColor(color);
    }

    public void initRight(String text, int drawableRes, OnClickListener listener) {
        tvRight.setText(String.valueOf(text));
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0);
        tvRight.setOnClickListener(listener);
    }

    public void setRightBtnRes(int drawableRes) {
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0);
    }

    public TextView getTvLeft() {
        return tvLeft;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvRight() {
        return tvRight;
    }
}
