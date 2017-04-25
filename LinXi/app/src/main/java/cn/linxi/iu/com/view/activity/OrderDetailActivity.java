package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.EventWxPaySuccess;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.presenter.OrderDetailPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IOrderDetailPresenter;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IOrderDetailView;
import cn.linxi.iu.com.view.widget.MyDialog;
import cn.linxi.iu.com.view.widget.PayPasswordView;
import cn.linxi.iu.com.view.widget.PayPsdDialog;
/**
 * Created by buzhiheng on 2016/7/27.
 * Desc 订单详情页面
 */
public class OrderDetailActivity extends AppCompatActivity implements IOrderDetailView{
    private IOrderDetailPresenter presenter;
    private final int PAY_SUCCESS = 0X001;
    @Bind(R.id.iv_orderpy_checkbalance)
    ImageView ivCheckBalance;
    @Bind(R.id.iv_orderpy_checkzfb)
    ImageView ivCheckZFB;
    @Bind(R.id.iv_orderpy_checkwx)
    ImageView ivCheckWx;
    @Bind(R.id.iv_orderdetail_photo)
    ImageView ivPhoto;
    @Bind(R.id.tv_orderdetail_address)
    TextView tvAddress;
    @Bind(R.id.tv_orderdetail_stationname)
    TextView tvName;
    @Bind(R.id.tv_orderdetail_purchase)
    TextView tvPurchase;
    @Bind(R.id.tv_orderdetail_type)
    TextView tvType;
    @Bind(R.id.tv_orderdetail_orderid)
    TextView tvOrderId;
    @Bind(R.id.tv_orderdetail_amount)
    TextView tvAmount;
    @Bind(R.id.tv_orderdetail_balance)
    TextView tvBalance;
    @Bind(R.id.tv_orderdetail_ordertime)
    TextView tvOrderTime;
    @Bind(R.id.tv_orderdetail_pay)
    TextView tvPay;
    private PayPsdDialog payPsdDialog;
    private Dialog dialog;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PAY_SUCCESS:
                    dialog.dismiss();
                    showToast("支付成功");
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        presenter = new OrderDetailPresenter(this,this);
        initView();
    }
    private void initView() {
        presenter.getOrderDetail(getIntent());
        ((TextView)findViewById(R.id.tv_titlebar_title)).setText("订单详情");
        dialog = MyDialog.getNoticeDialog(this,"请稍后...");
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                MyDialog.getAlertDialog(this, "该订单未支付,要取消订单吗?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        presenter.cancelOrder();
                        dialog.show();
                    }
                });
                break;
            case R.id.fl_order_pay_balance:
                presenter.onBalanceClick();
                break;
            case R.id.fl_order_pay_zfb:
                presenter.onAliClick();
                break;
            case R.id.fl_order_pay_wx:
                presenter.onWxClick();
                break;
            case R.id.btn_orderdetail_pay:
                presenter.payOrder();
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        dialog.dismiss();
        ToastUtil.show(toast);
    }
    @Override
    public void setOrder(OrderDetail order) {
        if (order == null){
            return;
        }
        x.image().bind(ivPhoto,order.avatar, BitmapUtil.getOptionRadius(30));
        tvName.setText(order.name);
        tvAmount.setText("支付金额: "+order.amount);
        tvOrderId.setText("订单编号: " + order.out_trade_no);
        tvAddress.setText("  " + order.address);
//        tvAttention.setText("  " + order.desc);
        tvOrderTime.setText(order.create_time);
        tvBalance.setText("账户余额: " + order.balance);
        tvPurchase.setText(order.purchase);
        tvType.setText("油品型号："+order.oil_type);
    }
    @Override
    public void setPayTv(String payTv) {
        tvPay.setText(payTv);
    }
    @Override
    public void setBalance(int id) {
        ivCheckBalance.setImageResource(id);
    }
    @Override
    public void setAlipay(int id) {
        ivCheckZFB.setImageResource(id);
    }
    @Override
    public void setWxpay(int id) {
        ivCheckWx.setImageResource(id);
    }
    @Override
    public void setBalanceCantPay() {
        findViewById(R.id.fl_order_pay_balance).setClickable(false);
    }
    @Override
    public void showPayPsdDialog() {
        payPsdDialog = new PayPsdDialog(this, new PayPasswordView.OnPayListener() {
            @Override
            public void onCancelPay() {
                payPsdDialog.dismiss();
            }
            @Override
            public void onSurePay(String password) {
                payPsdDialog.dismiss();
                presenter.payByPsd(password);
                dialog.show();
            }
            @Override
            public void onForgetPayPsd() {
                payPsdDialog.dismiss();
                Intent intent = new Intent(OrderDetailActivity.this,SafeCenterActivity.class);
                startActivity(intent);
            }
        });
        payPsdDialog.show();
    }
    @Override
    public void showNotBindPayPsd() {
        ToastUtil.show("您还没有设置支付密码");
        Intent intent = new Intent(this,ChangePayPsdActivity.class);
        startActivity(intent);
    }
    @Override
    public void showNotBindPhone() {
        ToastUtil.show("您还没有绑定手机");
        Intent intent = new Intent(this,BindPhoneActivity.class);
        startActivity(intent);
    }
    @Override
    public void payOrderSuccess() {
        handler.sendEmptyMessage(PAY_SUCCESS);
    }
    @Override
    public void cancelOrderSuccess() {
        dialog.dismiss();
        showToast("订单取消成功");
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventWxPaySuccess paySuccess){//支付成功
        //EventBus事件方法,支付成功finish
//        showToast("支付成功");
        finish();
    }
}