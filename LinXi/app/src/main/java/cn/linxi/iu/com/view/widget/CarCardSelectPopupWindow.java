package cn.linxi.iu.com.view.widget;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import cn.linxi.iu.com.R;
/**
 * Created by BuZhiheng on 2016/5/16.
 */
public class CarCardSelectPopupWindow extends PopupWindow {
    private AppCompatActivity context;
    private View view;
    public CarCardSelectPopupWindow(AppCompatActivity context){
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_select_carcard,null);
        //设置SelectPicPopupWindow的View
        setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        setHeight(900);
        //设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        setBackgroundDrawable(cd);
        setAnimationStyle(R.style.PopwinTheme);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.ll_popwin_carcard).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
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