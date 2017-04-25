package cn.linxi.iu.com.presenter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.AliToken;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.Order;
import cn.linxi.iu.com.model.OrderGoods;
import cn.linxi.iu.com.model.OrderOil;
import cn.linxi.iu.com.model.WXPayToken;
import cn.linxi.iu.com.presenter.ipresenter.IOrderPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IOrderView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/4/17.
 */
public class OrderPresenter implements IOrderPresenter {
    private IOrderView view;
    private String orderId = "";
    private int type = CommonCode.PAY_BY_ZFB;
    private List<OrderOil> listOil;
    private Order order;
    private float amount = 0;
    private String json = "";
    public OrderPresenter(IOrderView view){
        this.view = view;
    }
    @Override
    public void getOrderDetail(Intent intent) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        orderId = intent.getStringExtra(CommonCode.INTENT_ORDER_ID);
        view.setOrderId("订单编号："+orderId);
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
        String url = HttpUrl.getOrderDetailUrl + OkHttpUtil.getSign() + "&user_id=" +userId + "&oid=" + orderId;
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    order = GsonUtil.jsonToObject(result.getResult(), Order.class);
                    if ("normal".equals(order.source)){
                        view.showBalance(order);
                    }
                    amount = Float.parseFloat(order.total_amount);
                    view.setTotalAmount("合计："+order.total_amount+"元");
                    listOil = GsonUtil.jsonToList(order.oil_list,OrderOil.class);
                    List<OrderGoods> listGoods = GsonUtil.jsonToList(order.goods_list,OrderGoods.class);
                    if (listOil.size() == 0){
                        view.setOilEmpty();
                    } else {
                        for (int i=0;i<listOil.size();i++){
                            view.addItemOil(listOil.get(i));
                        }
                    }
                    if (listGoods.size() == 0){
                        view.setGoodsEmpty();
                    } else {
                        for (int i=0;i<listGoods.size();i++){
                            view.addItemGoods(listGoods.get(i));
                        }
                    }
                }
            }
        });
    }
    @Override
    public void payOrder(LinearLayout llOilItem, ImageView ivCheckBalance) {
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (StringUtil.isNull(orderId)){
            return;
        }
        if (order == null){
            return;
        }
        if (listOil != null && listOil.size() > 0){
            JSONArray array = new JSONArray();
            for (int i=0;i<listOil.size();i++){
                JSONObject jsonObject = new JSONObject();
                View view = llOilItem.getChildAt(i);
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_order_station_select_card);
                String tag = (String) imageView.getTag();
                try {
                    jsonObject.put("station_id",listOil.get(i).station_id);
                    if ("0".equals(tag)){
                        jsonObject.put("select",false);
                    } else {
                        jsonObject.put("select",true);
                    }
                    array.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            json = array.toString();
        }
        String balanceIscheck = "0";
        String tag = (String) ivCheckBalance.getTag();
        if ("1".equals(tag)){
            balanceIscheck = "1";
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("oid",orderId)
                .add("json", json)
                .add("balance",balanceIscheck)
                .build();
        if (amount <= 0){//走密码支付逻辑
            if (StringUtil.isNull(PrefUtil.getString(CommonCode.SP_USER_PHONE, ""))){
                view.showNotBindPhone();
                return;
            }
            if (PrefUtil.getInt(CommonCode.SP_USER_PAYPSDISBIND,0) == 0){
                view.showNotBindPayPsd();
                return;
            }
            view.showPsdDialog();//弹出密码框,余额支付
        } else {//余额不足，走支付宝微信逻辑
            if (type == CommonCode.PAY_BY_ZFB){
                payAli(body);
            } else {
                payWx(body);
            }
        }
    }
    private void payAli(RequestBody body) {
        OkHttpUtil.post(HttpUrl.payByAli, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    final AliToken token = GsonUtil.jsonToObject(result.getResult(),AliToken.class);
                    OkHttpUtil.executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            PayTask pay = new PayTask((Activity) view);
                            view.aliPayResult(pay.payV2(token.token, true));
                        }
                    });
                }
            }
        });
    }
    private void payWx(RequestBody body) {
        OkHttpUtil.post(HttpUrl.payByWX, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    WXPayToken wxPay = GsonUtil.jsonToObject(result.getResult(), WXPayToken.class);
                    WXPayToken.Token token = wxPay.token;
                    IWXAPI api = WXAPIFactory.createWXAPI((Context) view, CommonCode.APP_ID_WX, false);
                    api.registerApp(CommonCode.APP_ID_WX);
                    PayReq request = new PayReq();
                    request.appId = token.appid;
                    request.partnerId = token.partnerid;
                    request.prepayId= token.prepayid;
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr= token.noncestr;
                    request.timeStamp= token.timestamp;
                    request.sign= token.sign;
                    api.sendReq(request);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void payByPsd(String psd) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("pay_password", psd)
                .add("oid", order.oid)
                .add("balance",0-amount+"")
                .add("json",json)
                .build();
        OkHttpUtil.post(HttpUrl.payByBalance, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    view.payOrderSuccess();
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void onBalanceClick(ImageView ivCheckBalance) {
        String tag = (String) ivCheckBalance.getTag();
        if ("0".equals(tag)){
            view.setBalancePay(R.drawable.ic_station_checked);
            ivCheckBalance.setTag("1");
            amount = amount - Float.parseFloat(order.balance_use);
            setFinalTotal();
        } else {
            view.setBalancePay(R.drawable.ic_station_check);
            ivCheckBalance.setTag("0");
            amount = amount + Float.parseFloat(order.balance_use);
            setFinalTotal();
        }
    }
    @Override
    public void onAliClick() {
        view.setAlipay(R.drawable.ic_station_checked);
        view.setWxpay(R.drawable.ic_station_check);
        type = CommonCode.PAY_BY_ZFB;
    }
    @Override
    public void onWxClick() {
        view.setAlipay(R.drawable.ic_station_check);
        view.setWxpay(R.drawable.ic_station_checked);
        type = CommonCode.PAY_BY_WX;
    }
    @Override
    public void updateOrder(OrderOil oil, final ImageView iv, UpdateOrderListener listener) {
        String sTag = (String) iv.getTag();
        String tag;
        final int draw;
        String pay;
        if ("0".equals(sTag)){
            pay = oil.pay_amount;
            tag = "1";
            draw = R.drawable.ic_station_checked;
            amount = amount - Float.parseFloat(oil.paid_amount);
            setFinalTotal();
        } else {
            pay = Float.parseFloat(oil.paid_amount)+Float.parseFloat(oil.pay_amount)+"";
            tag = "0";
            draw = R.drawable.ic_station_check;
            amount = amount + Float.parseFloat(oil.paid_amount);
            setFinalTotal();
        }
        iv.setTag(tag);
        listener.success(draw,pay);
    }
    private void setFinalTotal(){
        if (amount > 0){
            view.setTotalAmount("合计："+amount+"元");
        } else {
            view.setTotalAmount("合计：0元");
        }
    }
    public interface UpdateOrderListener {
        void success(int drawable,String pay);
    }
}