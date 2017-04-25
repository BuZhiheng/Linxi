package cn.linxi.iu.com.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BusinessHistoryOrder;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.view.activity.BusinessResetOrderActivity;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class BusinessOrdersUnfinishAdapter extends RecyclerView.Adapter<BusinessOrdersUnfinishAdapter.MyViewHolder> {
    private Context context;
    private List<BusinessHistoryOrder> data;
    public BusinessOrdersUnfinishAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        x.view().inject((Activity) context);
    }
    public void setData(List<BusinessHistoryOrder> data) {
        this.data = data;
    }
    public void addData(List<BusinessHistoryOrder> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_business_orderunfinish_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BusinessHistoryOrder order = data.get(position);
        holder.tvName.setText(order.user_name);
        holder.tvTime.setText(order.create_time);
        holder.tvPur.setText(order.purchase+"/"+order.amount+order.oil_type+"/单价"+order.price);
        holder.btnFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessResetOrderActivity.class);
                intent.putExtra(CommonCode.INTENT_REGISTER_USER,order);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvTime;
        TextView tvPur;
        Button btnFix;
        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_business_unfinish_ordername);
            tvTime = (TextView) view.findViewById(R.id.tv_business_unfinish_ordertime);
            tvPur = (TextView) view.findViewById(R.id.tv_business_unfinish_orderpur);
            btnFix = (Button) view.findViewById(R.id.btn_business_unfinish_button);
        }
    }
}