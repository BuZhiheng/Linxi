package cn.linxi.iu.com.adapter;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BusinessHistoryOrder;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class BusinessOrdersAdapter extends RecyclerView.Adapter<BusinessOrdersAdapter.MyViewHolder> {
    private Context context;
    private List<BusinessHistoryOrder> data;
    public BusinessOrdersAdapter(Context context) {
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
                .inflate(R.layout.activity_business_orders_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BusinessHistoryOrder order = data.get(position);
        holder.tvName.setText(order.user_name);
        holder.tvTime.setText(order.create_time);
        holder.tvPrice.setText(order.oil_type+"/"+order.purchase+"/单价:"+order.price+"/"+order.amount);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvTime;
        TextView tvPrice;
        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_business_history_ordername);
            tvTime = (TextView) view.findViewById(R.id.tv_business_history_ordertime);
            tvPrice = (TextView) view.findViewById(R.id.tv_business_history_price);
        }
    }
}