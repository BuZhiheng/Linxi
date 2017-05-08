package cn.linxi.iu.com.view.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.TransferAdapter;
import cn.linxi.iu.com.presenter.TransferPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ITransferPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ITransferView;
/**
 * Created by buzhiheng on 2017/5/4.
 */
public class TransferFragment extends Fragment implements ITransferView, View.OnClickListener {
    private ITransferPresenter presenter;
    private View view;
    @Bind(R.id.srl_transfer)
    SwipeRefreshLayout refresh;
    @Bind(R.id.rv_transfer)
    RecyclerView rvTrans;
    private TransferAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_transfer,container,false);
        }
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView() {
        presenter = new TransferPresenter(this);
        view.findViewById(R.id.tv_transfer_detail).setOnClickListener(this);
        rvTrans.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransferAdapter(getActivity());
        rvTrans.setAdapter(adapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_transfer_detail:
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
}