package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.TransferOrderDetail;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class TransferOrderDetailAdapter extends RecyclerView.Adapter<TransferOrderDetailAdapter.MyViewHolder> {
    private Context context;
    private List<TransferOrderDetail> data;
    public String type;
    public TransferOrderDetailAdapter(Context context,String type) {
        this.context = context;
        this.type = type;
        data = new ArrayList<>();
    }
    public void setData(List<TransferOrderDetail> data) {
        this.data = data;
    }
    public void addData(List<TransferOrderDetail> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_transfer_order_detail, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TransferOrderDetail detail = data.get(position);
        holder.tvName.setText(detail.name);
        holder.tvPurchase.setText(detail.purchase);
        holder.tvType.setText(detail.oil_type + "");
        holder.tvTime.setText(detail.create_time);
        if ("1".equals(type)){
            holder.button.setVisibility(View.GONE);
        } else {
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout fl;
        TextView tvName;
        TextView tvPurchase;
        TextView tvType;
        TextView tvTime;
        Button button;
        public MyViewHolder(View view) {
            super(view);
            fl = (FrameLayout) view.findViewById(R.id.fl_transfer_order_detail);
            tvName = (TextView) view.findViewById(R.id.tv_transfer_order_detail_name);
            tvPurchase = (TextView) view.findViewById(R.id.tv_transfer_order_detail_purchase);
            tvType = (TextView) view.findViewById(R.id.tv_transfer_order_detail_type);
            tvTime = (TextView) view.findViewById(R.id.tv_transfer_order_detail_time);
            button = (Button) view.findViewById(R.id.btn_transfer_order_detail_cancel);
        }
    }
}