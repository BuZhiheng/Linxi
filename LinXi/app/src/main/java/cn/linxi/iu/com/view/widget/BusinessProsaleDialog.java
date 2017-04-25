package cn.linxi.iu.com.view.widget;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import cn.linxi.iu.com.R;
/**
 * Created by buzhiheng on 2016/8/10.
 */
public class BusinessProsaleDialog extends Dialog {
    private Context context;
    private String id;
    private String money;
    public BusinessProsaleDialog(Context context, String id,String money,View.OnClickListener listener) {
        this(context, R.style.CustomDialog);
        this.context = context;
        this.id = id;
        this.money = money;
        initView(listener);
    }
    private BusinessProsaleDialog(Context context, int theme) {
        super(context, theme);
    }
    private void initView(View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_business_prosale, null);
        view.findViewById(R.id.btn_dialog_prosale_sure).setOnClickListener(listener);
        view.findViewById(R.id.btn_dialog_prosale_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        getWindow().setGravity(Gravity.CENTER);
        ((TextView) view.findViewById(R.id.tv_dialog_prosale_card)).setText(id);
        ((TextView) view.findViewById(R.id.tv_dialog_prosale_amount)).setText(money);
    }
}