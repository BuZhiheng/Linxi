package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.BalanceDetailAdapter;
import cn.linxi.iu.com.model.BalanceDetail;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.presenter.BalanceDetailPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBalanceDetailPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBalanceDetailView;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;
/**
 * Created by buzhiheng on 2016/8/3.
 * Desc 余额明细页面
 */
public class BalanceDetailActivity extends AppCompatActivity implements IBalanceDetailView{
    private IBalanceDetailPresenter presenter;
    @Bind(R.id.rv_balancedetail)
    RecyclerView rvDetail;
    @Bind(R.id.srl_balance_detail)
    SwipeRefreshLayout refresh;
    private BalanceDetailAdapter adapter;
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balacncedetail);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        refresh.setProgressViewOffset(false, CommonCode.OFFSET_START, CommonCode.OFFSET_END);
        refresh.setRefreshing(true);
        presenter = new BalanceDetailPresenter(this);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("余额明细");
        rvDetail.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BalanceDetailAdapter(this);
        rvDetail.setAdapter(adapter);
        rvDetail.addOnScrollListener(new OnRvScrollListener(){
            @Override
            public void toBottom() {
                super.toBottom();
                page ++;
                presenter.getBalanceList(page);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getBalanceList(page);
            }
        });
        presenter.getBalanceList(page);
//        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
//                rvDetail,
//                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
//                    @Override
//                    public boolean canDismiss(int position) {
//                        return true;
//                    }
//                    @Override
//                    public void onDismiss(View view) {
//                        // Do what you want when dismiss
//                    }
//                })
//                .setIsVertical(false)
//                .setItemTouchCallback(
//                        new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
//                            @Override
//                            public void onTouch(int index) {
//                                // Do what you want when item be touched
//                            }
//                        })
//                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
//                    @Override
//                    public void onClick(int position) {
//                    }
//                }).create();
//    rvDetail.setOnTouchListener(listener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
        }
    }
    @Override
    public void showToast(String toast) {
        refresh.setRefreshing(false);
        ToastUtil.show(toast);
    }
    @Override
    public void getListSuccess(List<BalanceDetail> list) {
        if (refresh.isRefreshing()){
            adapter.setData(list);
        } else {
            adapter.addData(list);
        }
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
}