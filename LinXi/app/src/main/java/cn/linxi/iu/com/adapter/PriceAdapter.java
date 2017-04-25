package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.NormalPrice;
import cn.linxi.iu.com.util.WindowUtil;
/**
 * Created by BuZhiheng on 2016/3/31.
 * Desc 买油主页上方今日油价adapter
 */
public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.MyViewHolder> {
    private Context context;
    private List<NormalPrice> data;
    public PriceAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<NormalPrice> data) {
        this.data = data;
    }
    public void addData(List<NormalPrice> data) {
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
                .inflate(R.layout.activity_mainfrm_price_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NormalPrice price = data.get(position);
        holder.tvName.setText(price.oil_type);
        holder.tvPrice.setText(price.price);
        if ("2".equals(price.type)){
            holder.tvUnit.setText("元/立方");
        } else {
            holder.tvUnit.setText("元/升");
        }
        int index = position+1;
        if (index % 5 == 0){
            holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg4);
        } else if (index % 4 == 0){
            holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg5);
        } else if (index % 3 == 0){
            holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg3);
        } else if (index % 2 == 0){
            holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg2);
        } else {
            holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg1);
        }
        if (data.size() == 2 || data.size() == 3){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.llItem.getLayoutParams();
            params.width = WindowUtil.dp2px((AppCompatActivity) context, 119);
            holder.llItem.setLayoutParams(params);
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        LinearLayout ll;
        TextView tvName;
        TextView tvPrice;
        TextView tvUnit;
        public MyViewHolder(View view) {
            super(view);
            llItem = (LinearLayout) view.findViewById(R.id.ll_mainfrm_priceitem);
            ll = (LinearLayout) view.findViewById(R.id.ll_buyfrm_priceitem_bg);
            tvName = (TextView) view.findViewById(R.id.tv_mainfrm_priceitem_name);
            tvPrice = (TextView) view.findViewById(R.id.tv_mainfrm_priceitem_price);
            tvUnit = (TextView) view.findViewById(R.id.tv_mainfrm_priceitem_unit);
        }
    }
}