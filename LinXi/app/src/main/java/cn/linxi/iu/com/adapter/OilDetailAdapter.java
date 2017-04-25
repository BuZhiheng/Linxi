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
import cn.linxi.iu.com.model.BalanceDetail;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class OilDetailAdapter extends RecyclerView.Adapter<OilDetailAdapter.MyViewHolder> {
    private Context context;
    private List<BalanceDetail> data;
    public OilDetailAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        x.view().inject((Activity) context);
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
        holder.tvType.setText("类型:"+detail.oil_type);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAction;
        TextView tvTime;
        TextView tvAmount;
        TextView tvType;
        public MyViewHolder(View view) {//
            super(view);
            tvAction = (TextView) view.findViewById(R.id.tv_balance_item_action);
            tvTime = (TextView) view.findViewById(R.id.tv_balance_item_time);
            tvAmount = (TextView) view.findViewById(R.id.tv_balance_item_amount);
            tvType = (TextView) view.findViewById(R.id.tv_balance_item_type);
        }
    }
}