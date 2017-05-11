package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.image.ImageOptions;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.MyOilCardItem;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.view.activity.TransferSaleActivity;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class MyOilCardAdapter extends RecyclerView.Adapter<MyOilCardAdapter.MyViewHolder> {
    private Context context;
    private List<SaleOilCard> data;
    private ImageOptions option = BitmapUtil.getOptionRadius(30);
    public MyOilCardAdapter(Context context) {
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
                .inflate(R.layout.activity_myoilcard_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SaleOilCard card = data.get(position);
        holder.tvName.setText(card.name);
        holder.tvAddresss.setText(card.address);
        x.image().bind(holder.photo, card.avatar, option);

        List<MyOilCardItem> list = GsonUtil.jsonToList(card.list,MyOilCardItem.class);
        holder.llTransfer.removeAllViews();
        for (int i=0;i<list.size();i++){
            View view = LayoutInflater.from(context).inflate(R.layout.activity_myoilcard_item_transfer,null);
            TextView tvType = (TextView) view.findViewById(R.id.tv_myoilcard_type);
            TextView tvPurchase = (TextView) view.findViewById(R.id.tv_myoilcard_purchase);
            LinearLayout llTransfer = (LinearLayout) view.findViewById(R.id.ll_myoilcard_transfer);
            TextView tvLock = (TextView) view.findViewById(R.id.tv_myoilcard_lock);

            final MyOilCardItem item = list.get(i);
            tvType.setText(item.oil_type);
            tvPurchase.setText(item.purchase);
            tvLock.setText(item.sale_purchase);
            llTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TransferSaleActivity.class);
                    intent.putExtra(CommonCode.INTENT_ORDER_ID,item.card_id);
                    context.startActivity(intent);
                }
            });
            holder.llTransfer.addView(view);
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_myoilcard_item);
            llTransfer = (LinearLayout) view.findViewById(R.id.ll_myoilcard_item_transfer);
            photo = (ImageView) view.findViewById(R.id.iv_myoilcard_photo);
            tvName = (TextView) view.findViewById(R.id.tv_myoilcard_name);
            tvAddresss = (TextView) view.findViewById(R.id.tv_myoilcard_address);
        }
    }
}