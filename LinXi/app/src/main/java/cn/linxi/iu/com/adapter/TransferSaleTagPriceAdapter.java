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
import cn.linxi.iu.com.model.TransferSaleTagPrice;
/**
 * Created by BuZhiheng on 2016/3/31.
 *
 * Desc 加油站详情页面上方油价adapter
 */
public class TransferSaleTagPriceAdapter extends RecyclerView.Adapter<TransferSaleTagPriceAdapter.MyViewHolder> {
    private Context context;
    private List<TransferSaleTagPrice> data;
    private List<MyViewHolder> items = new ArrayList<>();

    public TransferSaleTagPriceAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<TransferSaleTagPrice> data) {
        items.clear();
        this.data = data;
    }

    public void addData(List<TransferSaleTagPrice> data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_transfer_sale_price, parent, false));
        items.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TransferSaleTagPrice price = data.get(position);
        holder.tvIndex.setText(position+1+"");
        holder.tvPrice.setText(price.refer_price+"元/L");
        holder.tvPurchase.setText("转让量："+price.refer_purchase+"L");
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex;
        TextView tvPrice;
        TextView tvPurchase;

        public MyViewHolder(View view) {
            super(view);
            tvIndex = (TextView) view.findViewById(R.id.tv_transfer_sale_tag_index);
            tvPrice = (TextView) view.findViewById(R.id.tv_transfer_sale_tag_price);
            tvPurchase = (TextView) view.findViewById(R.id.tv_transfer_sale_tag_purchase);
        }
    }
}