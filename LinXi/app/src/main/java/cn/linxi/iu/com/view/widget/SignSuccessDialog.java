package cn.linxi.iu.com.view.widget;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.SignReward;
/**
 * Created by buzhiheng on 2016/8/10.
 */
public class SignSuccessDialog extends Dialog {
    private Context context;
    private SignReward reward;
    public SignSuccessDialog(Context context, SignReward reward) {
        this(context, R.style.CustomDialog);
        this.context = context;
        this.reward = reward;
        initView();
    }
    private SignSuccessDialog(Context context, int theme) {
        super(context, theme);
    }
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_success, null);
        ((TextView) view.findViewById(R.id.tv_dialog_signsuccess_purchase)).setText(reward.purchase+"L");
        ((TextView) view.findViewById(R.id.tv_dialog_signsuccess_desc)).setText("您获得"+reward.purchase+"L油卡");
        view.findViewById(R.id.btn_dialog_signsuccess_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        getWindow().setGravity(Gravity.CENTER);
    }
}