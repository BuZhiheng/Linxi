package cn.linxi.iu.com.view.widget;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.Automac;
import cn.linxi.iu.com.model.TIModelCustomerOrderSure;
/**
 * Created by buzhiheng on 2016/8/10.
 */
public class BusinessAfterScanSureDialog extends Dialog {
    private Context context;
    private List<Automac> list;
    public BusinessAfterScanSureDialog(Context context, List<Automac> list, View.OnClickListener listener) {
        this(context, R.style.CustomDialog);
        this.context = context;
        this.list = list;
        initView(listener);
    }
    private BusinessAfterScanSureDialog(Context context, int theme) {
        super(context, theme);
    }
    private void initView(View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_business_afterscan, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_business_afterscan_suremsg);
        for (int i=0;i<list.size();i++){
            View vContent = LayoutInflater.from(context).inflate(R.layout.activity_business_afterscan_item, null);
            TextView tvName = (TextView) vContent.findViewById(R.id.tv_business_afterscan_name);
            TextView tvNum = (TextView) vContent.findViewById(R.id.tv_business_afterscan_num);
            Automac automac = list.get(i);
            tvName.setText(automac.name);
            tvNum.setText("x"+automac.num);
            layout.addView(vContent);
        }
        view.findViewById(R.id.btn_business_order_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_business_order_sure).setOnClickListener(listener);
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        getWindow().setGravity(Gravity.CENTER);
    }
}