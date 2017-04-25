package cn.linxi.iu.com.view.activity;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.TIModelCustomerOrderSure;
import cn.linxi.iu.com.presenter.CustomerOrderSurePresenter;
import cn.linxi.iu.com.presenter.ipresenter.ICustomerOrderSurePresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ICustomerOrderSureView;
import cn.linxi.iu.com.view.widget.MyDialog;
/**
 * Created by buzhiheng on 2016/9/8.
 */
public class CustomerOrderSureActivity extends Activity implements ICustomerOrderSureView{
    private ICustomerOrderSurePresenter presenter;
    @Bind(R.id.tv_customersure_station)
    TextView tvSation;
    @Bind(R.id.tv_customersure_money)
    TextView tvMoney;
    @Bind(R.id.tv_customersure_purchase)
    TextView tvPurchase;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ordersure);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView() {
        presenter = new CustomerOrderSurePresenter(this);
        presenter.getOrder(getIntent());
        dialog = MyDialog.getNoticeDialog(this,"正在确认订单...");
        dialog.setCancelable(false);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_customerorder_sure:
                dialog.show();
                presenter.orderConfirm();
                break;
            case R.id.btn_customerorder_cancel:
                finish();
                break;
        }
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TIModelCustomerOrderSure sure){
        //TIMPresenter 收到TIM新推送信息本页面会无限增加,
        // 所以发event关闭已经存在的弹窗,然而并不能设置本activity单例
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void showToast(String toast) {
        dialog.dismiss();
        ToastUtil.show(toast);
    }
    @Override
    public void setIntentOrder(TIModelCustomerOrderSure order) {
        if (order != null){
            tvSation.setText(order.name);
            tvMoney.setText(order.amount + "元");
            tvPurchase.setText(order.purchase);
        }
    }
    @Override
    public void orderConfirmSuccess() {
        dialog.dismiss();
        finish();
    }
}