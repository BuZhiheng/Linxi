package cn.linxi.iu.com.view.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.BusinessOrdersAdapter;
import cn.linxi.iu.com.model.BusinessHistoryOrder;
import cn.linxi.iu.com.presenter.BusinessHistoryOrderListPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessHistoryOrderListPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessHistoryOrderListView;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;
/**
 * Created by buzhiheng on 2016/8/9.
 */
public class BusinessOrderHistoryFragment extends Fragment implements IBusinessHistoryOrderListView{
    private IBusinessHistoryOrderListPresenter presenter;
    @Bind(R.id.rv_order_history)
    RecyclerView rvOrders;
    @Bind(R.id.srl_order_history)
    SwipeRefreshLayout refresh;
    @Bind(R.id.ll_include_nodata)
    LinearLayout llNodata;
    private BusinessOrdersAdapter adapter;
    private String status = "1";
    private int page = 1;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_history,container,false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }
    private void initView() {
        presenter = new BusinessHistoryOrderListPresenter(this);
        adapter = new BusinessOrdersAdapter(getContext());
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrders.setAdapter(adapter);
        rvOrders.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                super.toBottom();
                page++;
                presenter.getOrderList(status,page);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getOrderList(status, page);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        refresh.setRefreshing(true);
        presenter.getOrderList(status,page);
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void getOrderListSuccess(List<BusinessHistoryOrder> list) {
        llNodata.setVisibility(View.GONE);
        if (refresh.isRefreshing()){
            adapter.setData(list);
            refresh.setRefreshing(false);
        } else {
            adapter.addData(list);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setNoData() {
        refresh.setRefreshing(false);
        llNodata.setVisibility(View.VISIBLE);
    }
}