package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.image.ImageOptions;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.NAVIUtil;
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
        if ("1".equals(card.type)){
            holder.tvPurchase.setText(card.purchase+"L");
        } else {
            holder.tvPurchase.setText(card.purchase+"m³");
        }
        holder.tvNotice.setText(card.oil_type);
        x.image().bind(holder.photo, card.avatar, option);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.btnGothere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NAVIUtil.toNAVIActivity(context, card.latitude, card.longitude);
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
        TextView tvName;
        TextView tvPurchase;
        TextView tvNotice;
        Button btnGothere;
        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_myoilcard_item);
            photo = (ImageView) view.findViewById(R.id.iv_myoilcard_photo);
            tvName = (TextView) view.findViewById(R.id.tv_myoilcard_name);
            tvPurchase = (TextView) view.findViewById(R.id.tv_myoilcard_purchase);
            tvNotice = (TextView) view.findViewById(R.id.tv_myoilcard_notice);
            btnGothere = (Button) view.findViewById(R.id.btn_myoilcard_gothere);
        }
    }
}