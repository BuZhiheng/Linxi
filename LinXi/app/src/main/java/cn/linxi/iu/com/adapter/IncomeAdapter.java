package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.OperateIncomeItem;
import cn.linxi.iu.com.util.StringUtil;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.MyViewHolder> {
    private Context context;
    private List<OperateIncomeItem> data;
    public IncomeAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<OperateIncomeItem> data) {
        this.data = data;
    }
    public void addData(List<OperateIncomeItem> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_business_income_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OperateIncomeItem detail = data.get(position);
        holder.tvTitle.setText(StringUtil.phoneSetMiddleGone(detail.action));
        holder.tvAmount.setText(detail.desc);
        holder.tvTime.setText(detail.create_time);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvAmount;
        TextView tvTime;
        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_business_incomeitem_title);
            tvAmount = (TextView) view.findViewById(R.id.tv_business_incomeitem_amount);
            tvTime = (TextView) view.findViewById(R.id.tv_business_incomeitem_time);
        }
    }
}