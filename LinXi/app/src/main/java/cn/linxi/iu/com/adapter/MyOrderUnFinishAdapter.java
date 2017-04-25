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
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HistoryOrder;
import cn.linxi.iu.com.model.HistoryOrderItem;
import cn.linxi.iu.com.presenter.ipresenter.IOrderUnFinishPresenter;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.view.activity.OrderActivity;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class MyOrderUnFinishAdapter extends RecyclerView.Adapter<MyOrderUnFinishAdapter.MyViewHolder> {
    private Context context;
    private List<HistoryOrder> data;
    private IOrderUnFinishPresenter presenter;
    public MyOrderUnFinishAdapter(IOrderUnFinishPresenter presenter, Context context) {
        this.context = context;
        this.presenter = presenter;
        data = new ArrayList<>();
    }
    public void setData(List<HistoryOrder> data) {
        this.data = data;
    }
    public void addData(List<HistoryOrder> data) {
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
                .inflate(R.layout.activity_order_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final HistoryOrder order = data.get(position);
        holder.tvName.setText("订单号："+order.oid);
        holder.tvAmount.setText(order.amount+"元");
        final List<HistoryOrderItem> list = GsonUtil.jsonToList(order.list,HistoryOrderItem.class);
        String title = list.get(0).title;
        if (title.length() > 25){
            title = title.substring(0,24);
        }
        if (list.size() > 1){
            holder.tvPurchase.setText(title+"等");
        } else {
            holder.tvPurchase.setText(title);
        }
        holder.tvTime.setText(order.create_time);
        x.image().bind(holder.photo, "", BitmapUtil.getOptionRadius(30));
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra(CommonCode.INTENT_ORDER_ID, order.oid);
                context.startActivity(intent);
            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.removeOrder(order);
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
        TextView tvName;//加油站名称
        TextView tvAmount;//金额
        TextView tvPurchase;//购买油量
        TextView tvTime;
        Button btnCancel;
        public MyViewHolder(View view) {
            super(view);
            ll = (LinearLayout) view.findViewById(R.id.ll_myorder_item);
            photo = (ImageView) view.findViewById(R.id.iv_myorder_item_photo);
            tvName = (TextView) view.findViewById(R.id.tv_myorder_item_name);
            tvAmount = (TextView) view.findViewById(R.id.tv_myorder_item_amount);
            tvPurchase = (TextView) view.findViewById(R.id.tv_myorder_item_purchase);
            tvTime = (TextView) view.findViewById(R.id.tv_myorder_item_time);
            btnCancel = (Button) view.findViewById(R.id.btn_myorder_unfinish_cancel);
        }
    }
}