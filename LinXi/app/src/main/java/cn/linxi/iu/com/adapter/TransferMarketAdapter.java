package cn.linxi.iu.com.adapter;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.view.activity.TransferActivity;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class TransferMarketAdapter extends RecyclerView.Adapter<TransferMarketAdapter.MyViewHolder> {
    private Activity context;
    private List<SaleOilCard> data;
    public TransferMarketAdapter(Activity context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<SaleOilCard> data) {
        this.data = data;
    }
    public void addData(List<SaleOilCard> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fragment_transfer_market_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SaleOilCard card = data.get(position);
        holder.tvName.setText(card.name);
        holder.tvAddresss.setText(card.address);
        x.image().bind(holder.photo, card.avatar, BitmapUtil.getOptionCommon());

        List<StationOilType> list = GsonUtil.jsonToList(card.item,StationOilType.class);
        holder.llTransfer.removeAllViews();
        for (int i=0;i<list.size();i++){
            View view = LayoutInflater.from(context).inflate(R.layout.activity_transfer_market_item,null);
            TextView tvType = (TextView) view.findViewById(R.id.tv_market_item_type);
            TextView tvPurchase = (TextView) view.findViewById(R.id.tv_market_item_purchase);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_market_item_price);

            final StationOilType item = list.get(i);
            tvType.setText(item.oil_type);
            tvPurchase.setText(item.purchase);
            tvPrice.setText(item.price);
            holder.llTransfer.addView(view);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransferActivity.class);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        LinearLayout llTransfer;
        ImageView photo;
        TextView tvName;
        TextView tvAddresss;
        Button button;
        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_transfer_market_item);
            llTransfer = (LinearLayout) view.findViewById(R.id.ll_transfer_market_item_oil);
            photo = (ImageView) view.findViewById(R.id.iv_transfer_market_photo);
            tvName = (TextView) view.findViewById(R.id.tv_transfer_market_name);
            tvAddresss = (TextView) view.findViewById(R.id.tv_transfer_market_address);
            button = (Button) view.findViewById(R.id.btn_transfer_market_enter);
        }
    }
}