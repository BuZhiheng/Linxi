package cn.linxi.iu.com.view.fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.SaleAdapter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.presenter.SalePresenter;
import cn.linxi.iu.com.presenter.ipresenter.ISalePresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ISaleView;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;
/**
 * Created by buzhiheng on 2016/7/15.
 */
public class SaleFragment extends Fragment implements ISaleView{
    private ISalePresenter presenter;
    private final int TIME_OUT = 0X001;
    private View view;
    @Bind(R.id.tv_salefrm_total)
    TextView tvTotal;
    @Bind(R.id.iv_salefrm_open)
    ImageView ivDefault;
    @Bind(R.id.tv_salefrm_err)
    TextView tvErr;
    @Bind(R.id.rv_salefrm)
    RecyclerView rvSale;
    @Bind(R.id.srl_salefrm)
    SwipeRefreshLayout refresh;
    @Bind(R.id.ll_salefrm_default)
    LinearLayout llDefault;
    private SaleAdapter adapter;
    private int page = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_OUT:
                    refresh.setRefreshing(false);
                    showToast("连接超时");
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null){
            return view;
        }
        view = inflater.inflate(R.layout.fragment_sale,container,false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }
    private void initView() {
        refresh.setProgressViewOffset(false, CommonCode.OFFSET_START, CommonCode.OFFSET_END);
        refresh.setRefreshing(true);
        presenter = new SalePresenter(this);
        adapter = new SaleAdapter((AppCompatActivity) getContext());
        rvSale.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSale.setAdapter(adapter);
        rvSale.addOnScrollListener(new OnRvScrollListener(){
            @Override
            public void toBottom() {
                super.toBottom();
                page ++;
                presenter.getSaleCard(page,refresh);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getSaleCard(page, refresh);
            }
        });
        presenter.getSaleCard(page, refresh);
    }
    @Override
    public void showToast(String toast) {
        refresh.setRefreshing(false);
        ToastUtil.show(toast);
    }
    @Override
    public void setSaleOilCard(List<SaleOilCard> list) {
        if (refresh.isRefreshing()){
            adapter.setData(list);
        } else {
            adapter.addData(list);
        }
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
    @Override
    public void setTotal(String total) {
        tvTotal.setText(total);
    }
    @Override
    public void noneOilCard(String err) {
        refresh.setRefreshing(false);
        tvErr.setText(err);
        llDefault.setVisibility(View.VISIBLE);
    }
    @Override
    public void haveOilCard() {
        llDefault.setVisibility(View.GONE);
    }
    @Override
    public void haveNoOpen() {
        refresh.setRefreshing(false);
        tvErr.setText("敬请期待");
        llDefault.setVisibility(View.VISIBLE);
        ivDefault.setImageResource(R.drawable.ic_common_noopen);
    }
    @Override
    public void timeOut() {
        handler.sendEmptyMessage(TIME_OUT);
    }
    public void onRefresh(){
        page = 1;
        refresh.setRefreshing(true);
        presenter.getSaleCard(page, refresh);
    }
}