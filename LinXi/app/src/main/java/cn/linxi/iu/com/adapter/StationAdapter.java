package cn.linxi.iu.com.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BuyFrmModelList;
import cn.linxi.iu.com.model.Station;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.view.activity.OilDetailTypeActivity;

/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {
    private Context context;
    private List<Station> data;
    public StationAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }
    public void setData(List<Station> data) {
        this.data = data;
    }
    public void addData(List<Station> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    public void removeData(){
        this.data.clear();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_mainfrm_station_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Station station = data.get(position);
        holder.tvDistance.setText(station.distance);
        x.image().bind(holder.photo, station.avatar, BitmapUtil.getOptionRadius(5));
        holder.tvName.setText(station.name);
        holder.tvCity.setText(station.city);
        holder.ll.removeAllViews();
        List<BuyFrmModelList> list = GsonUtil.jsonToList(station.model_list,BuyFrmModelList.class);
        if (list != null && list.size()>0){
            for (int i=0;i<list.size();i++){
                if (i == 2){
                    break;
                }
                View view = LayoutInflater.from(context).inflate(R.layout.fragment_buy_item_text,null);
                LinearLayout llItem = (LinearLayout) view.findViewById(R.id.ll_mainfrm_stationitem_model);
                TextView tvItem = (TextView) view.findViewById(R.id.tv_mainfrm_stationitem_item);
                TextView tvPrice = (TextView) view.findViewById(R.id.tv_mainfrm_stationitem_price);
                int index = i+1;
                int color;
                if (index % 4 == 0){
                    llItem.setBackgroundResource(R.drawable.bg_ll_buyfrm_text_item4);
                    color = ContextCompat.getColor(context, R.color.color_station_price4);
                } else if (index % 3 == 0){
                    llItem.setBackgroundResource(R.drawable.bg_ll_buyfrm_text_item3);
                    color = ContextCompat.getColor(context,R.color.color_station_price3);
                } else if (index % 2 == 0){
                    llItem.setBackgroundResource(R.drawable.bg_ll_buyfrm_text_item2);
                    color = ContextCompat.getColor(context,R.color.color_station_price2);
                } else {
                    llItem.setBackgroundResource(R.drawable.bg_ll_buyfrm_text_item1);
                    color = ContextCompat.getColor(context,R.color.color_station_price1);
                }
                tvItem.setText(list.get(i).item);
                tvPrice.setText("  "+list.get(i).item_price);
                tvPrice.setTextColor(color);
                holder.ll.addView(view);
            }
        }
        if (StringUtil.isNull(station.oil)){
            holder.llOil.setVisibility(View.GONE);
        } else {
            holder.llOil.setVisibility(View.VISIBLE);
            holder.tvOil.setText(station.oil);
        }
        if (StringUtil.isNull(station.goods)){
            holder.llLube.setVisibility(View.GONE);
        } else {
            holder.llLube.setVisibility(View.VISIBLE);
            holder.tvLube.setText(station.goods);
        }
        if (StringUtil.isNull(station.wash)){
            holder.llWash.setVisibility(View.GONE);
        } else {
            holder.llWash.setVisibility(View.VISIBLE);
            holder.tvWash.setText(station.wash);
        }
        int type = CommonCode.STATION_TYPE_BOTH;
        if ((!StringUtil.isNull(station.oil)) && StringUtil.isNull(station.goods)){
            type = CommonCode.STATION_TYPE_OIL;
        } else if (StringUtil.isNull(station.oil) && (!StringUtil.isNull(station.goods))){
            type = CommonCode.STATION_TYPE_GOODS;
        } else if ((!StringUtil.isNull(station.oil)) && (!StringUtil.isNull(station.goods))){
            type = CommonCode.STATION_TYPE_BOTH;
        }
        final int finalType = type;
        holder.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OilDetailTypeActivity.class);
                intent.putExtra(CommonCode.INTENT_STATION_ID,station.station_id);
                intent.putExtra(CommonCode.INTENT_STATION_CID, station.cid);
                intent.putExtra(CommonCode.INTENT_COMMON, finalType);
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
        LinearLayout ll;
        LinearLayout llOil;
        LinearLayout llLube;
        LinearLayout llWash;
        ImageView photo;
        TextView tvName;
//        TextView tvModel;
        TextView tvDistance;
        TextView tvCity;
        TextView tvOil;
        TextView tvLube;
        TextView tvWash;
        public MyViewHolder(View view) {
            super(view);
            fl = (FrameLayout) view.findViewById(R.id.fl_mainfrm_station_item);
            ll = (LinearLayout) view.findViewById(R.id.ll_mainfrm_stationitem_model);
            llOil = (LinearLayout) view.findViewById(R.id.ll_mainfrm_stationitem_oil);
            llLube = (LinearLayout) view.findViewById(R.id.ll_mainfrm_stationitem_lube);
            llWash = (LinearLayout) view.findViewById(R.id.ll_mainfrm_stationitem_wash);
            photo = (ImageView) view.findViewById(R.id.iv_mainfrm_stationitem_photo);
            tvName = (TextView) view.findViewById(R.id.tv_mainfrm_stationitem_name);
//            tvModel = (TextView) view.findViewById(R.id.tv_mainfrm_stationitem_model);
            tvDistance = (TextView) view.findViewById(R.id.tv_mainfrm_stationitem_distance);
            tvCity = (TextView) view.findViewById(R.id.tv_mainfrm_stationitem_city);
            tvOil = (TextView) view.findViewById(R.id.tv_mainfrm_stationitem_oil);
            tvLube = (TextView) view.findViewById(R.id.tv_mainfrm_stationitem_lube);
            tvWash = (TextView) view.findViewById(R.id.tv_mainfrm_stationitem_wash);
        }
    }
}