package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.Sign;
/**
 * Created by buzhiheng on 2016/8/29.
 */
public class SignAdapter extends RecyclerView.Adapter<SignAdapter.MyViewHolder>{
    private Context context;
    private List<Sign> data;
    public SignAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<Sign> data){
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_sign_item,parent,false));
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvSign.setText(position + 1 + "");
        holder.fl.setBackgroundResource(R.drawable.ic_signitem_bgnone);
        if (position < data.size()){
            holder.fl.setBackgroundResource(R.drawable.ic_signitem_bg);
            holder.tvSign.setTextColor(ContextCompat.getColor(context,R.color.color_white));
            if (data.get(position).is_award == 1){
                holder.ivReward.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public int getItemCount() {
        return 28;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout fl;
        TextView tvSign;
        ImageView ivReward;
        public MyViewHolder(View view) {
            super(view);
            fl = (FrameLayout) view.findViewById(R.id.fl_sign_item);
            tvSign = (TextView) view.findViewById(R.id.tv_sign_item);
            ivReward = (ImageView) view.findViewById(R.id.iv_sign_item);
        }
    }
}