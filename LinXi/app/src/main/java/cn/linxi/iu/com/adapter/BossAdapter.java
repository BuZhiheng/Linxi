package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BossMsgEmployeeDetail;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.view.activity.BossDetailActivity;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class BossAdapter extends RecyclerView.Adapter<BossAdapter.MyViewHolder> {
    private Context context;
    private List<BossMsgEmployeeDetail> data;
    private String selectTime;
    public BossAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<BossMsgEmployeeDetail> data,String time) {
        this.selectTime = time;
        this.data = data;
    }
    public void addData(List<BossMsgEmployeeDetail> data,String time) {
        this.selectTime = time;
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_boss_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BossMsgEmployeeDetail detail = data.get(position);
        if (position%2 == 1){
            holder.ll.setBackgroundColor(ContextCompat.getColor(context, R.color.color_blue_bg));
        } else {
            holder.ll.setBackgroundColor(ContextCompat.getColor(context, R.color.color_bg));
        }
        holder.tvName.setText(detail.realname);
        holder.tvPurchase.setText(detail.purchase);
        holder.tvPro.setText(detail.promote_count+"");
        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BossDetailActivity.class);
                intent.putExtra(CommonCode.INTENT_COMMON, selectTime);
                intent.putExtra(CommonCode.INTENT_STATION_ID,detail.operat_id+"");
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
        TextView tvName;
        TextView tvPurchase;
        TextView tvPro;
        TextView tvDetail;
        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_bossitem);
            tvName = (TextView) view.findViewById(R.id.tv_bossitem_empname);
            tvPurchase = (TextView) view.findViewById(R.id.tv_bossitem_emppurchase);
            tvPro = (TextView) view.findViewById(R.id.tv_bossitem_promote);
            tvDetail = (TextView) view.findViewById(R.id.tv_bossitem_empdetail);
        }
    }
}