package cn.linxi.iu.com.view.activity;

import android.content.Intent;
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
import cn.linxi.iu.com.adapter.CouponAdapter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.CouponCard;
import cn.linxi.iu.com.presenter.CouponPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ICouponPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ICouponView;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;

/**
 * Created by buzhiheng on 2016/8/4.
 */
public class CouponActivity extends AppCompatActivity implements ICouponView{
    private ICouponPresenter presenter;
    @Bind(R.id.rv_coupon)
    RecyclerView rvCoupon;
    @Bind(R.id.srl_coupon)
    SwipeRefreshLayout refresh;
    private CouponAdapter adapter;
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ButterKnife.bind(this);
        presenter = new CouponPresenter(this);
        initView();
    }

    private void initView() {
        refresh.setProgressViewOffset(false, CommonCode.OFFSET_START, CommonCode.OFFSET_END);
        refresh.setRefreshing(true);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("红包油卡");
        ((TextView)findViewById(R.id.tv_titlebar_right)).setText("红包油卡明细");
        adapter = new CouponAdapter(this);
        rvCoupon.setLayoutManager(new LinearLayoutManager(this));
        rvCoupon.setAdapter(adapter);
        rvCoupon.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                super.toBottom();
                page++;
                presenter.getCardList(page);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getCardList(page);
            }
        });
        presenter.getCardList(page);
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
                break;
            case R.id.tv_titlebar_right:
                Intent intent = new Intent(this,CouponDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        refresh.setRefreshing(false);
        ToastUtil.show(toast);
    }

    @Override
    public void getListSuccess(List<CouponCard> list) {
        if (refresh.isRefreshing()){
            adapter.setData(list);
        } else {
            adapter.addData(list);
        }
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
}