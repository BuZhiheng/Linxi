package cn.linxi.iu.com.view.widget;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.linxi.iu.com.R;
/**
 * Created by BuZhiheng on 2016/5/16.
 */
public class TransferDetailPopupWindow extends PopupWindow {
    private AppCompatActivity context;
    private View view;
    private TextView tv1;
    private TextView tv2;
    public TransferDetailPopupWindow(AppCompatActivity context, View.OnClickListener listener){
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_popwin_transfer,null);
        tv1 = (TextView) view.findViewById(R.id.tv_dialog_popwin_transfer);
        tv2 = (TextView) view.findViewById(R.id.tv_dialog_popwin_transfer_oil);
        tv1.setOnClickListener(listener);
        tv2.setOnClickListener(listener);
        //设置SelectPicPopupWindow的View
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        // TODO: 2016/5/17 设置可以获取焦点
        setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        setOutsideTouchable(true);
        // TODO：更新popupwindow的状态
        update();
        setAnimationStyle(R.style.PopwinTheme);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                int height = view.findViewById(R.id.ll_dialog_popwin_transfer).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.4f;
        context.getWindow().setAttributes(lp);
    }
    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1f;
        context.getWindow().setAttributes(lp);
    }
}