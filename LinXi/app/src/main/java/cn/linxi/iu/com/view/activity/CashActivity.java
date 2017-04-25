package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventCashBindSuccess;
import cn.linxi.iu.com.presenter.CashPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ICashPresenter;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ICashView;
import cn.linxi.iu.com.view.widget.MyDialog;
/**
 * Created by buzhiheng on 2016/8/3.
 * Desc 余额提现页面
 */
public class CashActivity extends AppCompatActivity implements ICashView{
    private ICashPresenter presenter;
    @Bind(R.id.iv_cash_checkzfb)
    ImageView ivCheckZFB;
    @Bind(R.id.iv_cash_checkwx)
    ImageView ivCheckWx;
    @Bind(R.id.et_cash_cout)
    EditText etCout;
    @Bind(R.id.tv_cash_balance)
    TextView tvBalance;
    @Bind(R.id.tv_cash_account)
    TextView tvAccountTips;
    @Bind(R.id.tv_cash_ali)
    TextView tvAccountAli;
    @Bind(R.id.tv_cash_bank)
    TextView tvAccountUn;
    private String balance;
    private String channel = CommonCode.HTTP_CASH_CARDALI;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        EventBus.getDefault().register(this);
        presenter = new CashPresenter(this);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("提现");
        tvAccountAli.setText(PrefUtil.getString(CommonCode.SP_USER_LAST_ALIACCOUNT, ""));
        tvAccountUn.setText(PrefUtil.getString(CommonCode.SP_USER_LAST_BANKACCOUNT,""));
        balance = PrefUtil.getString(CommonCode.SP_USER_BALANCE, "0");
        tvBalance.setText("现金余额 ￥" + balance);
        dialog = MyDialog.getNoticeDialog(this,"正在提现...");
        dialog.setCancelable(true);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.fl_cash_zfb:
                ivCheckZFB.setImageResource(R.drawable.ic_station_checked);
                ivCheckWx.setImageResource(R.drawable.ic_station_check);
                tvAccountTips.setText("修改支付宝提现账户");
                channel = CommonCode.HTTP_CASH_CARDALI;
                break;
            case R.id.fl_cash_wx:
                ivCheckZFB.setImageResource(R.drawable.ic_station_check);
                ivCheckWx.setImageResource(R.drawable.ic_station_checked);
                tvAccountTips.setText("修改银行卡提现账户");
                channel = CommonCode.HTTP_CASH_CARDBANK;
                break;
            case R.id.tv_balance_cashall:
                etCout.setText(balance + "");
                break;
            case R.id.btn_cash_commit:
                dialog.show();
                presenter.cash(channel,etCout);
//                Uri uri = Uri.parse("https://www.pgyer.com/zveu");
//                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(it);
                break;
            case R.id.fl_cash_account:
                if (channel == CommonCode.HTTP_CASH_CARDALI){
                    Intent intent = new Intent(this,BindAliAccountActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(this,BindUNCardActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventCashBindSuccess bind){//获取银行卡号/ali账号
        if (!StringUtil.isNull(bind.union)){
            tvAccountUn.setText(bind.union);
        }
        if (!StringUtil.isNull(bind.ali)){
            tvAccountAli.setText(bind.ali);
        }
    }
    @Override
    public void showToast(String toast) {
        dialog.dismiss();
        ToastUtil.show(toast);
    }
    @Override
    public void cashSuccess() {
        dialog.dismiss();
        finish();
    }
    @Override
    public void toBindAli() {
        Intent intent = new Intent(this,BindAliAccountActivity.class);
        startActivity(intent);
    }
    @Override
    public void toBindBank() {
        Intent intent = new Intent(this,BindUNCardActivity.class);
        startActivity(intent);
    }
}