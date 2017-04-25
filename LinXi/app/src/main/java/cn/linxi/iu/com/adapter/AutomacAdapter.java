package cn.linxi.iu.com.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.Automac;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.view.activity.AutomacDetailActivity;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class AutomacAdapter extends RecyclerView.Adapter<AutomacAdapter.MyViewHolder> {
    private Activity context;
    private List<Automac> data;
    public AutomacAdapter(Activity context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<Automac> data) {
        this.data = data;
    }
    public void addData(List<Automac> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fragment_oildetail_automac, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Automac auto = data.get(position);
        x.image().bind(holder.iv, auto.pic, BitmapUtil.getOptionCommon());
        String title = auto.title;
        if (title.length() > 25){
            title = title.substring(0,24);
        }
        holder.tvTitle.setText(title);
        holder.tvSale.setText("已售:"+auto.saled);
        holder.tvPrice.setText(auto.now_price);
        holder.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = context.getIntent();
                intent.setClass(context,AutomacDetailActivity.class);
                intent.putExtra(CommonCode.INTENT_STATION_GID,auto.gid);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout fl;
        TextView tvTitle;
        TextView tvSale;
        TextView tvPrice;
        ImageView iv;
        public MyViewHolder(View view) {
            super(view);
            fl = (FrameLayout) view.findViewById(R.id.fl_automac_item);
            tvTitle = (TextView) view.findViewById(R.id.tv_oiltype_automac_title);
            tvSale = (TextView) view.findViewById(R.id.tv_oiltype_automac_sale);
            tvPrice = (TextView) view.findViewById(R.id.tv_oiltype_automac_price);
            iv = (ImageView) view.findViewById(R.id.iv_oiltype_automac);
        }
    }
}