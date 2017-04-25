package cn.linxi.iu.com.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import cn.linxi.iu.com.R;
import cn.linxi.iu.com.util.WindowUtil;

/**
 * Created by buzhiheng on 2016/8/15.
 */
public class PayPsdDialog extends Dialog {
    private Context context;
    private PayPasswordView.OnPayListener listener;
    public PayPsdDialog(Context context) {
        super(context);
    }
    public PayPsdDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    protected PayPsdDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public PayPsdDialog(Context context, PayPasswordView.OnPayListener listener) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(PayPasswordView.getInstance(context,listener).getView());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowUtil.getWindowsWidth();
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
    }
}