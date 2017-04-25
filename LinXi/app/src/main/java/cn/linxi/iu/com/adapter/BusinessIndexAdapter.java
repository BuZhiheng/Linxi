package cn.linxi.iu.com.adapter;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BusinessIndexDataItem;
import cn.linxi.iu.com.model.BusinessIndexDataOrder;
import cn.linxi.iu.com.util.GsonUtil;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class BusinessIndexAdapter extends RecyclerView.Adapter<BusinessIndexAdapter.MyViewHolder> {
    private Activity context;
    private List<BusinessIndexDataItem> data;
    public BusinessIndexAdapter(Activity context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<BusinessIndexDataItem> data) {
        this.data = data;
    }
    public void addData(List<BusinessIndexDataItem> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_business_index_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BusinessIndexDataItem item = data.get(position);
        holder.tvTime.setText(item.create_time);
        List<BusinessIndexDataOrder> listOrder = GsonUtil.jsonToList(item.order,BusinessIndexDataOrder.class);
        if (listOrder.size() == 0){
            return;
        }
        String sName = listOrder.get(0).name;
        String sNum = listOrder.get(0).num;
        String sMoney = listOrder.get(0).total_amount;
        for (int i=1;i<listOrder.size();i++){
            BusinessIndexDataOrder order = listOrder.get(i);
            sName = sName+"\n"+order.name;
            sNum = sNum+"\n"+order.num;
            sMoney = sMoney+"\n"+order.total_amount;
        }
        holder.tvName.setText(sName);
        holder.tvNum.setText(sNum);
        holder.tvMoney.setText(sMoney);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvNum;
        TextView tvName;
        TextView tvMoney;
        public MyViewHolder(View view) {
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tv_business_index_item_time);
            tvNum = (TextView) view.findViewById(R.id.tv_business_index_item_num);
            tvName = (TextView) view.findViewById(R.id.tv_business_index_item_type);
            tvMoney = (TextView) view.findViewById(R.id.tv_business_index_item_money);
        }
    }
}