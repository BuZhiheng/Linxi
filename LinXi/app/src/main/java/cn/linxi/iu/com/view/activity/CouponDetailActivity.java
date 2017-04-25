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
import cn.linxi.iu.com.adapter.OilDetailAdapter;
import cn.linxi.iu.com.model.BalanceDetail;
import cn.linxi.iu.com.presenter.CouponDetailPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ICouponDetailPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IOilDetailView;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;
/**
 * Created by buzhiheng on 2016/8/3.
 */
public class CouponDetailActivity extends AppCompatActivity implements IOilDetailView{
    private ICouponDetailPresenter presenter;
    @Bind(R.id.rv_oildetail)
    RecyclerView rvDetail;
    @Bind(R.id.srl_oildetail)
    SwipeRefreshLayout refresh;
    private int page = 1;
    OilDetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oildetail);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new CouponDetailPresenter(this);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("红包油卡明细");
        rvDetail.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OilDetailAdapter(this);
        rvDetail.setAdapter(adapter);
        rvDetail.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                super.toBottom();
                page++;
                presenter.getOilDetailList(page);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getOilDetailList(page);
            }
        });
        presenter.getOilDetailList(page);
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
    public void getDetailSuccess(List<BalanceDetail> list) {
        if (refresh.isRefreshing()){
            adapter.setData(list);
        } else {
            adapter.addData(list);
        }
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
}