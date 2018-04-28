package com.hlsp.video.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hlsp.video.R;


/**
 * Created by hackest on 2015/7/17.
 */
public class CommonDialog extends Dialog {
    private Button bt_dialog_left, bt_dialog_right;
    private View line_3;
    private TextView tv_dialog_content, tv_dialog_title;
    private RightButtonOnClickListener rightButtonOnClickListener;
    private LeftButtonOnClickListener leftButtonOnClickListener;
    private boolean autoDismiss = true;

    public CommonDialog(Context context) {
        super(context, R.style.Dialog_loading);
        init();
    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.common_dialog_layout);
        bt_dialog_left = (Button) findViewById(R.id.bt_dialog_left);
        bt_dialog_right = (Button) findViewById(R.id.bt_dialog_right);
        line_3 = findViewById(R.id.line_3);
        tv_dialog_content = (TextView) findViewById(R.id.tv_dialog_content);
        tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);

        bt_dialog_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoDismiss)
                    dismiss();
                if (leftButtonOnClickListener != null) {
                    leftButtonOnClickListener.onLeftButtonOnClick();
                }
            }
        });
        bt_dialog_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoDismiss)
                    dismiss();
                if (rightButtonOnClickListener != null) {
                    rightButtonOnClickListener.onRightButtonOnClick();
                }
            }
        });
    }

    /**
     * 设置对话框的标题头,默认为"提示"
     *
     * @param titleMsg
     */
    public void setTitleMsg(String titleMsg) {
        tv_dialog_title.setText(titleMsg);
        tv_dialog_title.setVisibility(View.VISIBLE);
    }

    public void hideTitle() {
        tv_dialog_title.setVisibility(View.GONE);
    }

    /**
     * 设置对话框的内容,默认为空
     *
     * @param contentMsg
     */
    public void setContentMsg(String contentMsg) {
        tv_dialog_content.setText(contentMsg);
    }

    /**
     * 设置左边的button的提示信息,默认是"取消"
     *
     * @param leftButtonMsg
     */
    public void setLeftButtonMsg(String leftButtonMsg) {
        bt_dialog_left.setText(leftButtonMsg);
    }

    /**
     * 设置右边的button的提示信息,默认是"确定"
     *
     * @param rightButtonMsg
     */
    public void setRightButtonMsg(String rightButtonMsg) {
        bt_dialog_right.setText(rightButtonMsg);
    }

    /**
     * 当只需要一个按钮的时候调用此方法,来隐藏右边的按钮,这个按钮默认是显示的
     */
    public void hideRightButton() {
        line_3.setVisibility(View.GONE);
        bt_dialog_left.setBackgroundResource(R.drawable.selector_dialog_single);
        bt_dialog_right.setVisibility(View.GONE);
    }

    /**
     * 显示右边的按钮,这个按钮默认是显示的
     */
    public void showRightButton() {
        line_3.setVisibility(View.VISIBLE);
        bt_dialog_left.setBackgroundResource(R.drawable.selector_dialog_left);
        bt_dialog_right.setVisibility(View.VISIBLE);
    }

    public void setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
    }

    public interface RightButtonOnClickListener {
        void onRightButtonOnClick();
    }

    public interface LeftButtonOnClickListener {
        void onLeftButtonOnClick();
    }

    /**
     * 设置右边按钮的点击监听,当右边按钮点击的时候就回调这个.
     * 不用调用 dismiss()已经调用过了
     *
     * @param rightButtonOnClickListener
     */
    public void setOnRightButtonOnClickListener(RightButtonOnClickListener rightButtonOnClickListener) {
        this.rightButtonOnClickListener = rightButtonOnClickListener;
    }

    /**
     * 设置左边按钮的点击监听,当左边按钮点击的时候就回调这个.
     * 不用调用 dismiss()已经调用过了
     *
     * @param leftButtonOnClickListener
     */
    public void setOnLeftButtonOnClickListener(LeftButtonOnClickListener leftButtonOnClickListener) {
        this.leftButtonOnClickListener = leftButtonOnClickListener;
    }

}
