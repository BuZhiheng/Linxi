package cn.linxi.iu.com.view.widget;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.UpdateMsg;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ToastUtil;
/**
 * Created by buzhiheng on 2016/8/10.
 */
public class UpdateDialog extends Dialog {
    private Context context;
    private UpdateMsg msg;
    private int ignore = 0;
    public UpdateDialog(Context context, UpdateMsg msg) {
        this(context, R.style.CustomDialog);
        this.context = context;
        this.msg = msg;
        initView();
    }
    private UpdateDialog(Context context, int theme) {
        super(context, theme);
    }
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        ((TextView) view.findViewById(R.id.tv_dialog_update_msg)).setText(msg.content.replace(";","\n"));
        final ImageView iv = (ImageView) view.findViewById(R.id.iv_dialog_update_ignore);
        view.findViewById(R.id.fl_dialog_update_ignore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ignore == 0){
                    iv.setImageResource(R.drawable.ic_station_checked);
                    ignore = 1;
                } else {
                    iv.setImageResource(R.drawable.ic_station_check);
                    ignore = 0;
                }
            }
        });
        view.findViewById(R.id.btn_dialog_update_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg.is_compel == 1){
                    ToastUtil.show("本版本必须更新");
                    return;
                }
                if (ignore == 1){
                    PrefUtil.putString(CommonCode.SP_APP_IGNORE_VERSION,msg.version_number);
                }
                dismiss();
            }
        });
        view.findViewById(R.id.btn_dialog_update_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=cn.linxi.iu.com");
                intent.setData(content_url);
                context.startActivity(intent);
                if (msg.is_compel == 0) {
                    dismiss();
                }
            }
        });
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
    }
}