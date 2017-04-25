package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BalanceDetail;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class BalanceDetailAdapter extends RecyclerView.Adapter<BalanceDetailAdapter.MyViewHolder> {
    private Context context;
    private List<BalanceDetail> data;
    public BalanceDetailAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<BalanceDetail> data) {
        this.data = data;
    }
    public void addData(List<BalanceDetail> data) {
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
                .inflate(R.layout.activity_balancedetail_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BalanceDetail detail = data.get(position);
        holder.tvAction.setText(detail.action);
        holder.tvTime.setText(detail.create_time);
        holder.tvAmount.setText(detail.desc);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAction;
        TextView tvTime;
        TextView tvAmount;
        public MyViewHolder(View view) {
            super(view);
            tvAction = (TextView) view.findViewById(R.id.tv_balance_item_action);
            tvTime = (TextView) view.findViewById(R.id.tv_balance_item_time);
            tvAmount = (TextView) view.findViewById(R.id.tv_balance_item_amount);
        }
    }
}