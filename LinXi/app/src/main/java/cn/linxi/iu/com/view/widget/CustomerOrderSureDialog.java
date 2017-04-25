package cn.linxi.iu.com.view.widget;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.TIModelCustomerOrderSure;
/**
 * Created by buzhiheng on 2016/8/10.
 */
public class CustomerOrderSureDialog extends Dialog {
    private Context context;
    private TIModelCustomerOrderSure order;
    public CustomerOrderSureDialog(Context context, TIModelCustomerOrderSure order, View.OnClickListener listener) {
        this(context, R.style.CustomDialog);
        this.context = context;
        this.order = order;
        initView(listener);
    }
    private CustomerOrderSureDialog(Context context, int theme) {
        super(context, theme);
    }
    private void initView(View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_customer_ordersure, null);
        ((TextView) view.findViewById(R.id.tv_customersure_station)).setText(order.name);
        ((TextView) view.findViewById(R.id.tv_customersure_money)).setText(order.amount+"元");
        ((TextView) view.findViewById(R.id.tv_customersure_purchase)).setText(order.purchase+"L");
        view.findViewById(R.id.btn_customerorder_sure).setOnClickListener(listener);
        view.findViewById(R.id.btn_customerorder_cancel).setOnClickListener(listener);
        setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        getWindow().setGravity(Gravity.CENTER);
    }
}