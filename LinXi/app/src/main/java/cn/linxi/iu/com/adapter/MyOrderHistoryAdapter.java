package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.HistoryOrder;
import cn.linxi.iu.com.model.HistoryOrderItem;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.GsonUtil;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class MyOrderHistoryAdapter extends RecyclerView.Adapter<MyOrderHistoryAdapter.MyViewHolder> {
    private Context context;
    private List<HistoryOrder> data;
    public MyOrderHistoryAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<HistoryOrder> data) {
        this.data = data;
    }
    public void addData(List<HistoryOrder> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
//    public void removeData(){
//        this.data.clear();
//    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_order_item_finish, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final HistoryOrder order = data.get(position);
        holder.tvName.setText("订单号："+order.oid);
        holder.tvAmount.setText(order.amount+"元");
        holder.tvTime.setText(order.create_time);
        holder.llItem.removeAllViews();
        List<HistoryOrderItem> items = GsonUtil.jsonToList(order.list,HistoryOrderItem.class);
        for (int i=0;i<items.size();i++){
            HistoryOrderItem item = items.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_order_item_finish_goodsmsg,null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_order_item_history_title);
            TextView tvNum = (TextView) view.findViewById(R.id.tv_order_item_history_num);
            TextView tvType = (TextView) view.findViewById(R.id.tv_order_item_history_type);
            tvTitle.setText(item.title);
            tvNum.setText("数量："+item.num);
            tvType.setText(item.is_deliver);
            holder.llItem.addView(view);
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvAmount;
        TextView tvTime;
        LinearLayout llItem;
        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_myorder_item_name);
            tvAmount = (TextView) view.findViewById(R.id.tv_myorder_item_amount);
            tvTime = (TextView) view.findViewById(R.id.tv_myorder_item_time);
            llItem = (LinearLayout) view.findViewById(R.id.ll_myorder_history_item);
        }
    }
}