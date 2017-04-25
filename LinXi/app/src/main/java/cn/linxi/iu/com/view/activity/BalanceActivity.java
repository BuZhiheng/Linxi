package cn.linxi.iu.com.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.UserCenterInfo;
import cn.linxi.iu.com.presenter.BalancePresenter;
import cn.linxi.iu.com.presenter.ipresenter.IBalancePresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IBalanceView;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public class BalanceActivity extends AppCompatActivity implements IBalanceView {
    private IBalancePresenter presenter;
    @Bind(R.id.tv_balance_balance)
    TextView tvBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        presenter = new BalancePresenter(this);
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("余额");
        ((TextView)findViewById(R.id.tv_titlebar_right)).setText("余额明细");
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        presenter.getUserInfo();
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
                Intent intentDetail = new Intent(this,BalanceDetailActivity.class);
                startActivity(intentDetail);
                break;
            case R.id.btn_balance_recharge:
                Intent intentRecharge = new Intent(this,RechargeActivity.class);
                startActivity(intentRecharge);
                break;
            case R.id.btn_balance_cash:
                Intent intentCash = new Intent(this,CashActivity.class);
                startActivity(intentCash);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void getInfoSuccess(UserCenterInfo info) {
        tvBalance.setText("￥"+info.balance);
    }
}