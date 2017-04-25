package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.CouponCard;
import cn.linxi.iu.com.view.activity.QRCodeShowActivity;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {
    private Context context;
    private List<CouponCard> data;
    public CouponAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<CouponCard> data) {
        this.data = data;
    }
    public void addData(List<CouponCard> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_coupon_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CouponCard card = data.get(position);
        holder.tvOilType.setText(card.oil_type+"#");
        holder.tvPurchase.setText(card.purchase+"L");
        holder.btnAddOil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (card.id != null){
                    Intent intent = new Intent(context, QRCodeShowActivity.class);
                    intent.putExtra(CommonCode.INTENT_COMMON,card.id);
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        ImageView photo;
        TextView tvOilType;
        TextView tvPurchase;
        Button btnAddOil;
        public MyViewHolder(View view) {
            super(view);
            tvOilType = (TextView) view.findViewById(R.id.tv_coupon_item_oiltype);
            tvPurchase = (TextView) view.findViewById(R.id.tv_coupon_item_purchase);
            btnAddOil = (Button) view.findViewById(R.id.btn_coupon_item_addoil);
        }
    }
}