package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventUserMsgChanged;
import cn.linxi.iu.com.model.EventWxPaySuccess;
import cn.linxi.iu.com.presenter.RechargePresenter;
import cn.linxi.iu.com.presenter.ipresenter.IRechargePresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IRechargeView;
/**
 * Created by buzhiheng on 2016/8/3.
 * Desc 充值页面
 */
public class RechargeActivity extends AppCompatActivity implements IRechargeView {
    private IRechargePresenter presenter;
    @Bind(R.id.iv_recharge_checkzfb)
    ImageView ivCheckZFB;
    @Bind(R.id.iv_recharge_checkwx)
    ImageView ivCheckWx;
    @Bind(R.id.et_recharge_cout)
    EditText etCout;
    private int type = CommonCode.PAY_BY_ZFB;//0支付宝 1微信
    private Map<String,String> result;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String resultStatus = result.get("resultStatus");
            String memo = result.get("memo");
            if ("9000".equals(resultStatus)){
                EventBus.getDefault().post(new EventUserMsgChanged());
                finish();
            } else {
                ToastUtil.show(memo);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        presenter = new RechargePresenter(this);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView() {
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("充值");
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
            case R.id.fl_recharge_zfb:
                ivCheckZFB.setImageResource(R.drawable.ic_station_checked);
                ivCheckWx.setImageResource(R.drawable.ic_station_check);
                type = CommonCode.PAY_BY_ZFB;
                break;
            case R.id.fl_recharge_wx:
                ivCheckZFB.setImageResource(R.drawable.ic_station_check);
                ivCheckWx.setImageResource(R.drawable.ic_station_checked);
                type = CommonCode.PAY_BY_WX;
                break;
            case R.id.btn_recharge_commit:
                presenter.recharge(type,etCout);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void rechargeSuccess() {
        finish();
    }

    @Override
    public void aliPayResult(Map<String, String> result) {
        this.result = result;
        handler.sendEmptyMessage(0x001);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventWxPaySuccess paySuccess){//支付成功
        //EventBus事件方法,充值成功finish
        showToast("充值成功");
        finish();
    }
}
