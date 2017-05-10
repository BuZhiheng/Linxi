package cn.linxi.iu.com.view.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.TransferMarketAdapter;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.presenter.TransferMarketPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ITransferMarketPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ITransferMarketView;
/**
 * Created by buzhiheng on 2017/5/4.
 */
public class TransferMarketFragment extends Fragment implements ITransferMarketView, View.OnClickListener {
    private ITransferMarketPresenter presenter;
    private View view;
    @Bind(R.id.srl_transfer_market)
    SwipeRefreshLayout refresh;
    @Bind(R.id.rv_transfer_market)
    RecyclerView rvTrans;
    private TransferMarketAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_transfer_market,container,false);
        }
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        presenter = new TransferMarketPresenter(this);
        rvTrans.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransferMarketAdapter(getActivity());
        rvTrans.setAdapter(adapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
        presenter.getData();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }

    @Override
    public void setTransferMarket(List<SaleOilCard> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }
}