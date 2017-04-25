package cn.linxi.iu.com.adapter;
import android.content.Context;
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
import cn.linxi.iu.com.model.AutomacType;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class AutomacTypeAdapter extends RecyclerView.Adapter<AutomacTypeAdapter.MyViewHolder> {
    private Context context;
    private List<AutomacType> data;
    private OnAutomacTypeClickListener listener;
    private List<MyViewHolder> items = new ArrayList<>();
    public AutomacTypeAdapter(Context context,OnAutomacTypeClickListener listener) {
        this.context = context;
        this.listener = listener;
        data = new ArrayList<>();
    }
    public void setData(List<AutomacType> data) {
        this.data = data;
    }
    public void addData(List<AutomacType> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.fragment_oildetail_type_automac, parent, false));
        items.add(holder);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AutomacType type = data.get(position);
        x.image().bind(holder.iv,type.avatar);
        holder.tvName.setText(type.name);
        holder.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onTypeClick(type);
                    initCheck(position);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    private void initCheck(int position){
        for (int i=0;i<items.size();i++){
            MyViewHolder holder = items.get(i);
            if (position == i){
                holder.ivCheck.setVisibility(View.VISIBLE);
            } else {
                holder.ivCheck.setVisibility(View.GONE);
            }
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout fl;
        TextView tvName;
        ImageView iv;
        ImageView ivCheck;
        public MyViewHolder(View view) {
            super(view);
            fl = (FrameLayout) view.findViewById(R.id.fl_automac_type);
            tvName = (TextView) view.findViewById(R.id.tv_oiltype_automac_type);
            iv = (ImageView) view.findViewById(R.id.iv_oiltype_automac_type);
            ivCheck = (ImageView) view.findViewById(R.id.iv_oiltype_check);
        }
    }
    public interface OnAutomacTypeClickListener{
        void onTypeClick(AutomacType type);
    }
}