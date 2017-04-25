package cn.linxi.iu.com.adapter;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.util.WindowUtil;

/**
 * Created by BuZhiheng on 2016/3/31.
 *
 * Desc 加油站详情页面上方油价adapter
 */
public class StationPriceAdapter extends RecyclerView.Adapter<StationPriceAdapter.MyViewHolder> {
    private Context context;
    private List<StationOilType> data;
    private OnItemClick click;
    private List<MyViewHolder> items = new ArrayList<>();
    public StationPriceAdapter(Context context,OnItemClick click) {
        this.context = context;
        this.click = click;
        data = new ArrayList<>();
        x.view().inject((Activity) context);
    }
    public void setData(List<StationOilType> data) {
        items.clear();
        this.data = data;
    }
    public void addData(List<StationOilType> data) {
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
                .inflate(R.layout.activity_station_price_item, parent, false));
        items.add(holder);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final StationOilType price = data.get(position);
        holder.tvName.setText(price.oil_type);
        holder.tvPrice.setText(price.price);
        if ("2".equals(price.type)){
            holder.tvUnit.setText("元/立方");
        }
        int index = position+1;
        int color;
        if (index % 4 == 0){
            holder.ll.setBackgroundResource(R.drawable.ic_station_pricebg4);
            color = ContextCompat.getColor(context,R.color.color_station_price4);
        } else if (index % 3 == 0){
            holder.ll.setBackgroundResource(R.drawable.ic_station_pricebg3);
            color = ContextCompat.getColor(context,R.color.color_station_price3);
        } else if (index % 2 == 0){
            holder.ll.setBackgroundResource(R.drawable.ic_station_pricebg2);
            color = ContextCompat.getColor(context,R.color.color_station_price2);
        } else {
            holder.ll.setBackgroundResource(R.drawable.ic_station_pricebg1);
            color = ContextCompat.getColor(context,R.color.color_station_price1);
        }
        if (position == 0){
            holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg1);
            color = ContextCompat.getColor(context,R.color.color_white);
        }
        holder.tvPrice.setTextColor(color);
        holder.tvUnit.setTextColor(color);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null){
                    initItems(position);
                    click.click(position,price);
                }
            }
        });
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
            llItem = (LinearLayout) view.findViewById(R.id.ll_station_price_item);
            ll = (LinearLayout) view.findViewById(R.id.ll_buyfrm_priceitem_bg);
            tvName = (TextView) view.findViewById(R.id.tv_station_item_name);
            tvPrice = (TextView) view.findViewById(R.id.tv_station_item_price);
            tvUnit = (TextView) view.findViewById(R.id.tv_station_item_unit);
        }
    }
    public interface OnItemClick{
        void click(int i,StationOilType price);
    }
    public void initItems(int position){
        for (int i=0;i<items.size();i++){
            MyViewHolder holder = items.get(i);
            int index = i+1;
            int color;
            if (index % 4 == 0){
                holder.ll.setBackgroundResource(R.drawable.ic_station_pricebg4);
                color = ContextCompat.getColor(context,R.color.color_station_price4);
            } else if (index % 3 == 0){
                holder.ll.setBackgroundResource(R.drawable.ic_station_pricebg3);
                color = ContextCompat.getColor(context,R.color.color_station_price3);
            } else if (index % 2 == 0){
                holder.ll.setBackgroundResource(R.drawable.ic_station_pricebg2);
                color = ContextCompat.getColor(context,R.color.color_station_price2);
            } else {
                holder.ll.setBackgroundResource(R.drawable.ic_station_pricebg1);
                color = ContextCompat.getColor(context,R.color.color_station_price1);
            }
            if (position == i){
                if (index % 4 == 0){
                    holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg4);
                } else if (index % 3 == 0){
                    holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg3);
                } else if (index % 2 == 0){
                    holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg2);
                } else {
                    holder.ll.setBackgroundResource(R.drawable.ic_buyfrm_pricebg1);
                }
                color = ContextCompat.getColor(context,R.color.color_white);
            }
            holder.tvPrice.setTextColor(color);
            holder.tvUnit.setTextColor(color);
        }
    }
}