package cn.linxi.iu.com.view.widget;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.OperatUser;
/**
 * Created by buzhiheng on 2016/8/10.
 */
public class BusinessWorkOutDialog extends Dialog {
    private Context context;
    private OperatUser user;
    public BusinessWorkOutDialog(Context context, OperatUser user, View.OnClickListener listener) {
        this(context, R.style.CustomDialog);
        this.context = context;
        this.user = user;
        initView(listener);
    }
    private BusinessWorkOutDialog(Context context, int theme) {
        super(context, theme);
    }
    private void initView(View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_business_workout, null);
        view.findViewById(R.id.btn_dialog_workout_sure).setOnClickListener(listener);
        view.findViewById(R.id.btn_dialog_workout_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        getWindow().setGravity(Gravity.CENTER);
        if (user == null){
            return;
        }
        ((TextView) view.findViewById(R.id.tv_dialog_workout_oil)).setText(user.station_purchase);
        ((TextView) view.findViewById(R.id.tv_dialog_workout_gas)).setText(user.station_gas);
        ((TextView) view.findViewById(R.id.tv_dialog_workout_amount)).setText(user.advance_amount);
    }
}