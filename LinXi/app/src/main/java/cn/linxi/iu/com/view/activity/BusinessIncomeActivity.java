package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.adapter.IncomeAdapter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.OperateIncome;
import cn.linxi.iu.com.model.OperateIncomeItem;
import cn.linxi.iu.com.presenter.BusinessIncomePresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessIncomePresenter;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBusinessIncomeView;
/**
 * Created by buzhiheng on 2016/12/4.
 */
public class BusinessIncomeActivity extends AppCompatActivity implements IBusinessIncomeView{
    private IBusinessIncomePresenter presenter;
    @Bind(R.id.srl_business_income)
    SwipeRefreshLayout refresh;
    @Bind(R.id.rv_business_income)
    RecyclerView rvIncome;
    @Bind(R.id.tv_business_income_balance)
    TextView tvBalance;
    private IncomeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_income);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new BusinessIncomePresenter(this);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("推广收益");
        adapter = new IncomeAdapter(this);
        rvIncome.setLayoutManager(new LinearLayoutManager(this));
        rvIncome.setAdapter(adapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getIncomeInfo();
            }
        });
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.btn_business_income_cash:
                Intent intent = new Intent(this,CashActivity.class);
                PrefUtil.putInt(CommonCode.SP_USER_USERID, PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID, 0));
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        presenter.getIncomeInfo();
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
        refresh.setRefreshing(false);
    }
    @Override
    public void getInfoSuccess(OperateIncome income, List<OperateIncomeItem> list) {
        PrefUtil.putString(CommonCode.SP_USER_BALANCE,income.balance);
        tvBalance.setText(income.balance);
        adapter.setData(list);
        adapter.notifyDataSetChanged();
        refresh.setRefreshing(false);
    }
}