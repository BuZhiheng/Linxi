package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.RechargeDiscount;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class RechargeDiscountAdapter extends RecyclerView.Adapter<RechargeDiscountAdapter.MyViewHolder> {
    private Context context;
    private List<RechargeDiscount> data;
    private ItemClickListener listener;
    private List<MyViewHolder> items = new ArrayList<>();
    public RechargeDiscountAdapter(Context context,ItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        data = new ArrayList<>();
    }
    public void setData(List<RechargeDiscount> data) {
        this.data = data;
    }
    public void addData(List<RechargeDiscount> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_recharge_discount_item, parent, false));
        items.add(holder);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RechargeDiscount recharge = data.get(position);
        holder.tvMoney.setText(recharge.money);
        holder.tvDiscount.setText(recharge.percent+"折");
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onItemClick(recharge);
                    initCheck(position);
                }
            }
        });
        if (position == 0){
            holder.ivCheck.setVisibility(View.VISIBLE);
        }
        int index = position+1;
        int bg;
        if (index % 4 == 0){
            bg = R.drawable.ic_recharge_discount_bg1;
        } else if (index % 3 == 0){
            bg = R.drawable.ic_recharge_discount_bg2;
        } else if (index % 2 == 0){
            bg = R.drawable.ic_recharge_discount_bg3;
        } else {
            bg = R.drawable.ic_recharge_discount_bg4;
        }
        holder.ll.setBackgroundResource(bg);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void initCheck(int position){
        for (int i=0;i<items.size();i++){
            MyViewHolder holder = items.get(i);
            if (i == position){
                holder.ivCheck.setVisibility(View.VISIBLE);
            } else {
                holder.ivCheck.setVisibility(View.GONE);
            }
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        TextView tvMoney;
        TextView tvDiscount;
        ImageView ivCheck;
        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_recharge_discount_item);
            tvMoney = (TextView) view.findViewById(R.id.tv_recharge_discount_item_money);
            tvDiscount = (TextView) view.findViewById(R.id.tv_recharge_discount_item_discount);
            ivCheck = (ImageView) view.findViewById(R.id.iv_recharge_check);
        }
    }
    public interface ItemClickListener{
        void onItemClick(RechargeDiscount discount);
    }
}