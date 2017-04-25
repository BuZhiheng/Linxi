package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.BossDetailAdapter;
import cn.linxi.iu.com.model.BossDetailMsg;
import cn.linxi.iu.com.model.BossDetailMsgEmployee;
import cn.linxi.iu.com.presenter.BossDetailPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBossDetailPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBossDetailView;
import cn.linxi.iu.com.view.widget.OnRvScrollListener;
/**
 * Created by buzhiheng on 2016/11/10.
 */
public class BossDetailActivity extends AppCompatActivity implements IBossDetailView{
    private IBossDetailPresenter presenter;
    private String type = "all";
    @Bind(R.id.tv_bossdetail_name)
    TextView tvName;
    @Bind(R.id.tv_bossdetail_employeeamount)
    TextView tvAmount;
    @Bind(R.id.tv_bossdetail_employeephone)
    TextView tvPhone;
    @Bind(R.id.iv_bossdetail_all)
    ImageView ivAll;
    @Bind(R.id.iv_bossdetail_cash)
    ImageView ivCash;
    @Bind(R.id.iv_bossdetail_plateform)
    ImageView ivPlateform;
    @Bind(R.id.srl_bossdetail)
    SwipeRefreshLayout refresh;
    @Bind(R.id.rv_bossdetail)
    RecyclerView rv;
    private BossDetailAdapter adapter;
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_detail);
        ButterKnife.bind(this);
        presenter = new BossDetailPresenter(this,getIntent());
        initView();
    }
    private void initView() {
        adapter = new BossDetailAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new OnRvScrollListener() {
            @Override
            public void toBottom() {
                super.toBottom();
                page++;
                presenter.getMsg(type, page);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getMsg(type, page);
            }
        });
        presenter.getMsg(type,page);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_bossdetail_back:
                finish();
                break;
            case R.id.ll_bossdetail_all:
                ivAll.setImageResource(R.drawable.ic_bossdetail_checked);
                ivCash.setImageResource(R.drawable.ic_bossdetail_check);
                ivPlateform.setImageResource(R.drawable.ic_bossdetail_check);
                setType("all");
                break;
            case R.id.ll_bossdetail_cash:
                ivAll.setImageResource(R.drawable.ic_bossdetail_check);
                ivCash.setImageResource(R.drawable.ic_bossdetail_checked);
                ivPlateform.setImageResource(R.drawable.ic_bossdetail_check);
                setType("advance");
                break;
            case R.id.ll_bossdetail_plateform:
                ivAll.setImageResource(R.drawable.ic_bossdetail_check);
                ivCash.setImageResource(R.drawable.ic_bossdetail_check);
                ivPlateform.setImageResource(R.drawable.ic_bossdetail_checked);
                setType("platform");
                break;
        }
    }
    private void setType(String type){
        page = 1;
        this.type = type;
        presenter.getMsg(type, page);
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
        refresh.setRefreshing(false);
    }
    @Override
    public void getMsgEmployee(BossDetailMsg msg, List<BossDetailMsgEmployee> list) {
        tvName.setText(msg.realname);
        tvAmount.setText("今日收益 "+msg.total_amount+" 元");
        tvPhone.setText(msg.mobile);
        if (page == 1){
            adapter.setData(list);
        } else {
            adapter.addData(list);
        }
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
}