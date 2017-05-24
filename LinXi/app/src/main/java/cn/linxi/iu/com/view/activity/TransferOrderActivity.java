package cn.linxi.iu.com.view.activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import cn.linxi.iu.com.model.TransferOrder;
import cn.linxi.iu.com.model.TransferOrderDetail;
import cn.linxi.iu.com.presenter.TransferOrderPresenter;
import cn.linxi.iu.com.presenter.ipresenter.ITransferOrderPresenter;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ITransferOrderView;
/**
 * Created by buzhiheng on 2017/5/22.
 */
public class TransferOrderActivity extends AppCompatActivity implements ITransferOrderView, View.OnClickListener {
    private ITransferOrderPresenter presenter;
    @Bind(R.id.tv_transfer_order_type)
    TextView tvType;
    @Bind(R.id.tv_transfer_order_total)
    TextView tvTotal;
    @Bind(R.id.ll_transfer_order_item)
    LinearLayout llItem;
    @Bind(R.id.iv_orderpy_checkzfb)
    ImageView ivCheckZFB;
    @Bind(R.id.iv_orderpy_checkwx)
    ImageView ivCheckWx;
    private int payType = CommonCode.PAY_BY_ZFB;
    private Map<String,String> result;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String resultStatus = result.get("resultStatus");
            String memo = result.get("memo");
            if ("9000".equals(resultStatus)){
                EventBus.getDefault().post(new EventUserMsgChanged());
                showToast("支付成功");
                finish();
            } else {
                showToast(memo);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_order);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        presenter = new TransferOrderPresenter(this,getIntent());
        initView();
    }
    private void initView() {
        ((TextView) findViewById(R.id.tv_titlebar_title)).setText("转让市场");
        presenter.getOrder();
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
    }
    @Override
    public void setOrderData(TransferOrder order) {
        tvType.setText("  购买明细("+order.oil_type+")");
        tvTotal.setText("¥" + order.total);
    }
    @Override
    public void setOrderItem(TransferOrderDetail detail) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_transfer_order_item,null);
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_transfer_order_item_price);
        TextView tvPurchase = (TextView) view.findViewById(R.id.tv_transfer_order_item_purchase);
        TextView tvAmount = (TextView) view.findViewById(R.id.tv_transfer_order_item_amount);
        tvPrice.setText(detail.price);
        tvPurchase.setText(detail.purchase);
        tvAmount.setText(detail.amount);
        llItem.addView(view);
    }
    @Override
    public void aliPayResult(Map<String, String> stringStringMap) {
        this.result = stringStringMap;
        handler.sendEmptyMessage(0x001);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventWxPaySuccess paySuccess){//支付成功
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.fl_order_pay_zfb:
                ivCheckZFB.setImageResource(R.drawable.ic_station_checked);
                ivCheckWx.setImageResource(R.drawable.ic_station_check);
                payType = CommonCode.PAY_BY_ZFB;
                break;
            case R.id.fl_order_pay_wx:
                ivCheckZFB.setImageResource(R.drawable.ic_station_check);
                ivCheckWx.setImageResource(R.drawable.ic_station_checked);
                payType = CommonCode.PAY_BY_WX;
                break;
            case R.id.btn_transfer_order_pay:
                presenter.pay(payType);
                break;
        }
    }
}