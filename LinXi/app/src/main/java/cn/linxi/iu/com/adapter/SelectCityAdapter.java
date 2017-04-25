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
import cn.linxi.iu.com.model.SelectCity;
/**
 * Created by BuZhiheng on 2016/3/31.
 */
public class SelectCityAdapter extends RecyclerView.Adapter<SelectCityAdapter.MyViewHolder> {
    private Context context;
    private List<SelectCity> data;
    private ItemClickListener click;
    public SelectCityAdapter(Context context,ItemClickListener click) {
        this.context = context;
        this.click = click;
        data = new ArrayList<>();
    }
    public void setData(List<SelectCity> data) {
        this.data = data;
    }
    public List<SelectCity> getData(){
        return data;
    }
    public void addData(List<SelectCity> data) {
        if (this.data == null){
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.activity_selectcity_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SelectCity city = data.get(position);
        holder.tvCity.setText(position+1+city.city_name);
        holder.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click != null){
                    click.click(city);
                }
            }
        });
    }
    public interface ItemClickListener{
        void click(SelectCity city);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        public MyViewHolder(View view) {
            super(view);
            tvCity = (TextView) view.findViewById(R.id.tv_selectcity_item);
        }
    }
}