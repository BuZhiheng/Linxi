package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BossDetailMsgEmployee;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class BossDetailAdapter extends RecyclerView.Adapter<BossDetailAdapter.MyViewHolder> {
    private Context context;
    private List<BossDetailMsgEmployee> data;
    public BossDetailAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<BossDetailMsgEmployee> data) {
        this.data = data;
    }
    public void addData(List<BossDetailMsgEmployee> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_bossdetail_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BossDetailMsgEmployee detail = data.get(position);
        if (position%2 == 1){
            holder.ll.setBackgroundColor(ContextCompat.getColor(context, R.color.color_blue_bg));
        } else {
            holder.ll.setBackgroundColor(ContextCompat.getColor(context, R.color.color_bg));
        }
        holder.tvTime.setText(detail.create_time);
        holder.tvType.setText(detail.oil_type);
        holder.tvPurchase.setText(detail.purchase);
        holder.tvAmount.setText(detail.amount);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        TextView tvTime;
        TextView tvType;
        TextView tvPurchase;
        TextView tvAmount;
        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_bossdetail_item);
            tvTime = (TextView) view.findViewById(R.id.tv_bossitem_time);
            tvType = (TextView) view.findViewById(R.id.tv_bossitem_type);
            tvPurchase = (TextView) view.findViewById(R.id.tv_bossitem_purchase);
            tvAmount = (TextView) view.findViewById(R.id.tv_bossitem_amount);
        }
    }
}