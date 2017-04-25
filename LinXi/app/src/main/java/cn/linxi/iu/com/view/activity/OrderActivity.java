package cn.linxi.iu.com.view.activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.EventUserMsgChanged;
import cn.linxi.iu.com.model.EventWxPaySuccess;
import cn.linxi.iu.com.model.Order;
import cn.linxi.iu.com.model.OrderGoods;
import cn.linxi.iu.com.model.OrderGoodsItem;
import cn.linxi.iu.com.model.OrderOil;
import cn.linxi.iu.com.model.OrderOilItem;
import cn.linxi.iu.com.presenter.OrderPresenter;
import cn.linxi.iu.com.presenter.ipresenter.IOrderPresenter;
import cn.linxi.iu.com.util.BitmapUtil;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.IOrderView;
import cn.linxi.iu.com.view.widget.MyDialog;
import cn.linxi.iu.com.view.widget.PayPasswordView;
import cn.linxi.iu.com.view.widget.PayPsdDialog;

/**
 * Created by buzhiheng on 2017/4/17.
 */
public class OrderActivity extends AppCompatActivity implements IOrderView, View.OnClickListener{
    private IOrderPresenter presenter;
    @Bind(R.id.tv_order_id)
    TextView tvOrderId;
    @Bind(R.id.tv_order_total)
    TextView tvTotal;
    @Bind(R.id.tv_order_balance)
    TextView tvBalance;
    @Bind(R.id.tv_order_balance_use)
    TextView tvBalanceUse;
    @Bind(R.id.tv_order_balance_percent)
    TextView tvBalancePercent;
    @Bind(R.id.ll_order_oil_content)
    LinearLayout llOilContent;
    @Bind(R.id.ll_order_auto_content)
    LinearLayout llGoodsContent;
    @Bind(R.id.ll_order_auto_content_item)
    LinearLayout llOilItem;
    @Bind(R.id.ll_order_balance_content)
    LinearLayout llBalance;
    @Bind(R.id.iv_orderpy_checkbalance)
    ImageView ivCheckBalance;
    @Bind(R.id.iv_orderpy_checkzfb)
    ImageView ivCheckZFB;
    @Bind(R.id.iv_orderpy_checkwx)
    ImageView ivCheckWx;
    private PayPsdDialog payPsdDialog;
    private Dialog dialog;
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
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }
    private void initView() {
        ((TextView) findViewById(R.id.tv_titlebar_title)).setText("订单详情");
        dialog = MyDialog.getNoticeDialog(this, "请稍后...");
        presenter = new OrderPresenter(this);
        presenter.getOrderDetail(getIntent());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_titlebar_back:
                finish();
                break;
            case R.id.fl_order_pay_zfb:
                presenter.onAliClick();
                break;
            case R.id.fl_order_pay_wx:
                presenter.onWxClick();
                break;
            case R.id.btn_order_pay:
                dialog.show();
                presenter.payOrder(llOilItem,ivCheckBalance);
                break;
            case R.id.ll_order_balance_content:
                presenter.onBalanceClick(ivCheckBalance);
                break;
        }
    }
    @Override
    public void showToast(String toast) {
        ToastUtil.show(toast);
        dialog.dismiss();
    }
    @Override
    public void showNotBindPayPsd() {
        showToast("您还没有设置支付密码");
        Intent intent = new Intent(this,ChangePayPsdActivity.class);
        startActivity(intent);
    }
    @Override
    public void payOrderSuccess() {
        showToast("支付成功");
        finish();
    }
    @Override
    public void showNotBindPhone() {
        showToast("您还没有绑定手机");
        Intent intent = new Intent(this,BindPhoneActivity.class);
        startActivity(intent);
    }
    @Override
    public void setOrderId(String orderId) {
        tvOrderId.setText(orderId);
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
    public void setTotalAmount(String s) {
        tvTotal.setText(s);
    }
    @Override
    public void addItemOil(final OrderOil orderOil) {
        View viewStation = LayoutInflater.from(this).inflate(R.layout.activity_order_station_oil,null);
        TextView tvName = (TextView) viewStation.findViewById(R.id.tv_orderoil_station_name);
        tvName.setText(orderOil.name);

        TextView tvCardmoney = (TextView) viewStation.findViewById(R.id.tv_orderoil_station_cardmoney);
        tvCardmoney.setText("储值卡余额："+orderOil.paid_balance+"元");

        TextView tvMoney = (TextView) viewStation.findViewById(R.id.tv_orderoil_station_money);
        tvMoney.setText("储值卡可支付:"+orderOil.paid_amount+"元");

        final TextView tvPay = (TextView) viewStation.findViewById(R.id.tv_orderoil_station_pay);
        tvPay.setText(orderOil.pay_amount+"元");

        final ImageView ivSelect = (ImageView) viewStation.findViewById(R.id.iv_order_station_select_card);
        FrameLayout fSelect = (FrameLayout) viewStation.findViewById(R.id.fl_order_station_select_card);
        fSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.updateOrder(orderOil, ivSelect, new OrderPresenter.UpdateOrderListener() {
                    @Override
                    public void success(int drawable, String pay) {
                        ivSelect.setImageResource(drawable);
                        tvPay.setText(pay + "元");
                    }
                });
            }
        });
        LinearLayout ll = (LinearLayout) viewStation.findViewById(R.id.ll_order_station_oil_item);
        List<OrderOilItem> oilItems = GsonUtil.jsonToList(orderOil.list,OrderOilItem.class);
        for (int i=0;i<oilItems.size();i++){
            View viewOil = LayoutInflater.from(this).inflate(R.layout.activity_order_item_oil,null);
            OrderOilItem item = oilItems.get(i);
            TextView tvOilName = (TextView) viewOil.findViewById(R.id.tv_order_item_oil_name);
            tvOilName.setText("油品型号："+item.oil_type+" "+item.num);

            TextView tvTotal = (TextView) viewOil.findViewById(R.id.tv_order_item_oil_total);
            tvTotal.setText(item.total);//原价
            ll.addView(viewOil);
        }
        llOilItem.addView(viewStation);
    }
    @Override
    public void addItemGoods(OrderGoods orderGoods) {
        View viewStation = LayoutInflater.from(this).inflate(R.layout.activity_order_station_goods,null);
        TextView tvName = (TextView) viewStation.findViewById(R.id.tv_ordergoods_station_name);
        tvName.setText(orderGoods.name);

        TextView tvTotal = (TextView) viewStation.findViewById(R.id.tv_ordergoods_station_total);
        tvTotal.setText(orderGoods.item_amount);

        TextView tvAddress = (TextView) viewStation.findViewById(R.id.tv_ordergoods_station_time);
        tvAddress.setText(orderGoods.address);

        LinearLayout ll = (LinearLayout) viewStation.findViewById(R.id.ll_order_station_goods_item);
        List<OrderGoodsItem> goodsItems = GsonUtil.jsonToList(orderGoods.list,OrderGoodsItem.class);
        for (int i=0;i<goodsItems.size();i++){
            View viewGoods = LayoutInflater.from(this).inflate(R.layout.activity_order_item_goods, null);
            OrderGoodsItem goods = goodsItems.get(i);
            TextView tvGoodsName = (TextView) viewGoods.findViewById(R.id.tv_order_goods_item_title);
            tvGoodsName.setText(goods.title);
            TextView tvGoodsNum = (TextView) viewGoods.findViewById(R.id.tv_order_goods_item_num);
            tvGoodsNum.setText(goods.num);
            TextView tvGoodsTotal = (TextView) viewGoods.findViewById(R.id.tv_order_goods_item_total);
            tvGoodsTotal.setText(goods.total);
            ImageView iv = (ImageView) viewGoods.findViewById(R.id.iv_order_goods_item_pic);
            x.image().bind(iv, goods.pic, BitmapUtil.getOptionCommon());
            ll.addView(viewGoods);
        }
        llGoodsContent.addView(viewStation);
    }
    @Override
    public void setOilEmpty() {
        llOilContent.setVisibility(View.GONE);
    }
    @Override
    public void setGoodsEmpty() {
        llGoodsContent.setVisibility(View.GONE);
    }
    @Override
    public void aliPayResult(Map<String, String> stringStringMap) {
        this.result = stringStringMap;
        handler.sendEmptyMessage(0x001);
    }
    @Override
    public void showBalance(Order order) {
        llBalance.setVisibility(View.VISIBLE);
        tvBalance.setText("账户余额：" + order.balance + "元");
        tvBalanceUse.setText(order.balance_use+"元");
        tvBalancePercent.setText("(最多使用购买总额的"+order.percent+")");
    }
    @Override
    public void setBalancePay(int ic_station_check) {
        ivCheckBalance.setImageResource(ic_station_check);
    }
    @Override
    public void showPsdDialog() {
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
                Intent intent = new Intent(OrderActivity.this,SafeCenterActivity.class);
                startActivity(intent);
            }
        });
        payPsdDialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        dialog.dismiss();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventWxPaySuccess paySuccess){//支付成功
        //EventBus事件方法,充值成功finish
//        showToast("支付成功");
        finish();
    }
}