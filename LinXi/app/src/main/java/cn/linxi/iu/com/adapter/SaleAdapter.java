package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.view.activity.MyOilCardDetailActivity;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.MyViewHolder> {
    private AppCompatActivity context;
    private List<SaleOilCard> data;
    private ImageOptions option = BitmapUtil.getOptionRadius(30);
    public SaleAdapter(AppCompatActivity context) {
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
//    public void removeData(){
//        this.data.clear();
//    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_salefrm_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SaleOilCard card = data.get(position);
        holder.tvName.setText(card.name+"-#"+card.oil_type);
        holder.tvPurchase.setText("剩余油量:"+card.purchase+"L");
        if (card.balance != null){
            if (card.balance > 0){
                holder.tvGetMoney.setText("+"+card.balance);
            } else {
                holder.tvGetMoney.setText(card.balance+"");
                holder.tvGetMoney.setTextColor(ContextCompat.getColor(context,R.color.color_green));
            }
        }
        x.image().bind(holder.photo,card.avatar, option);
        holder.btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyOilCardDetailActivity.class);
                intent.putExtra(CommonCode.INTENT_REGISTER_USER,card);
                context.startActivityForResult(intent, CommonCode.ACTIVITY_RESULT_CODE_SALE);
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
        TextView tvGetMoney;
        Button btnSale;
        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_sale_item);
            photo = (ImageView) view.findViewById(R.id.iv_saleitem_photo);
            tvName = (TextView) view.findViewById(R.id.tv_saleitem_name);
            tvPurchase = (TextView) view.findViewById(R.id.tv_saleitem_purchase);
            tvGetMoney = (TextView) view.findViewById(R.id.tv_saleitem_getmoney);
            btnSale = (Button) view.findViewById(R.id.btn_sale_item_sale);
        }
    }
}