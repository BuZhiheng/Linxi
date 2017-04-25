package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.util.Map;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.AliToken;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.OrderDetail;
import cn.linxi.iu.com.model.WXPayToken;
import cn.linxi.iu.com.presenter.ipresenter.IOrderDetailPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.WindowUtil;
import cn.linxi.iu.com.view.iview.IOrderDetailView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/22.
 */
public class OrderDetailPresenter implements IOrderDetailPresenter {
    private IOrderDetailView view;
    private AppCompatActivity context;
    private OrderDetail order;
    private String selectBalance = "";
    private int type;
    private boolean balanceEnoughAndSelect = false;
    private String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
    public OrderDetailPresenter(IOrderDetailView view,AppCompatActivity context){
        this.view = view;
        this.context = context;
    }
    @Override
    public void getOrderDetail(Intent intent) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (intent != null && intent.getSerializableExtra(CommonCode.INTENT_REGISTER_USER) != null){
            order = (OrderDetail) intent.getSerializableExtra(CommonCode.INTENT_REGISTER_USER);
            String url = HttpUrl.getOrderDetailUrl + OkHttpUtil.getSign() + "&user_id=" +userId + "&out_trade_no=" + order.out_trade_no;
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
                        order = GsonUtil.jsonToObject(result.getResult(), OrderDetail.class);
                        if (order.balance == 0) {
                            view.setBalanceCantPay();
                            view.setBalance(R.drawable.ic_station_check);
                            view.setAlipay(R.drawable.ic_station_checked);
                            view.setWxpay(R.drawable.ic_station_check);
                            view.setPayTv("合计:" + order.amount + "元");
                            type = CommonCode.PAY_BY_ZFB;
                        } else if (order.balance < order.amount) {//订单总额大于账户余额
                            view.setBalance(R.drawable.ic_station_checked);
                            view.setAlipay(R.drawable.ic_station_checked);
                            view.setPayTv("合计:" + WindowUtil.getRoundFloat(order.amount - order.balance) + "元");
                            selectBalance = "1";
                            type = CommonCode.PAY_BY_ZFB;
                        } else if (order.balance >= order.amount) {
                            view.setBalance(R.drawable.ic_station_checked);
                            view.setAlipay(R.drawable.ic_station_check);
                            view.setWxpay(R.drawable.ic_station_check);
                            view.setPayTv("合计:" + 0 + "元");
                            balanceEnoughAndSelect = true;
                        }
                        if (!StringUtil.isNull(order.address) && order.address.length() > 12) {
                            order.address = order.address.substring(0, 8) + "...";
                        }
                        view.setOrder(order);
                    } else {
                        view.showToast(result.error);
                    }
                }
            });
        }
    }
    @Override
    public void payOrder() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (balanceEnoughAndSelect){
            if (StringUtil.isNull(PrefUtil.getString(CommonCode.SP_USER_PHONE, ""))){
                view.showNotBindPhone();
                return;
            }
            if (PrefUtil.getInt(CommonCode.SP_USER_PAYPSDISBIND,0) == 0){
                view.showNotBindPayPsd();
                return;
            }
            view.showPayPsdDialog();//余额充足,弹出密码框,余额支付
            return;
        }
        switch (type) {//余额不足
            case CommonCode.PAY_BY_ZFB:
                payAli();
                break;
            case CommonCode.PAY_BY_WX:
                payWx();
                break;
        }
    }
    @Override
    public void payByPsd(String psd) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (order == null || order.out_trade_no == null){
            view.showToast("获取订单失败");
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("pay_password",psd)
                .add("out_trade_no",order.out_trade_no)
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
    public void onBalanceClick() {
        if (order.balance == 0){
            return;
        }
        if (order.balance >= order.amount){
            view.setBalance(R.drawable.ic_station_checked);
            view.setAlipay(R.drawable.ic_station_check);
            view.setWxpay(R.drawable.ic_station_check);
            view.setPayTv("合计:" + 0 + "元");
            balanceEnoughAndSelect = true;
        } else {
            if ("0".equals(selectBalance)){
                view.setBalance(R.drawable.ic_station_checked);
                view.setPayTv("合计:" + WindowUtil.getRoundFloat(order.amount - order.balance) + "元");
                selectBalance = "1";
            } else {
                view.setBalance(R.drawable.ic_station_check);
                selectBalance = "0";
                view.setPayTv("合计:"+order.amount+"元");
            }
        }
    }
    @Override
    public void onAliClick() {
        view.setAlipay(R.drawable.ic_station_checked);
        view.setWxpay(R.drawable.ic_station_check);
        type = CommonCode.PAY_BY_ZFB;
        if (order.balance >= order.amount){
            view.setBalance(R.drawable.ic_station_check);
            view.setPayTv("合计:"+order.amount+"元 ");
            balanceEnoughAndSelect = false;
        }
    }
    @Override
    public void onWxClick() {
        view.setAlipay(R.drawable.ic_station_check);
        view.setWxpay(R.drawable.ic_station_checked);
        type = CommonCode.PAY_BY_WX;
        if (order.balance >= order.amount){
            view.setBalance(R.drawable.ic_station_check);
            view.setPayTv("合计:" + order.amount + "元 ");
            balanceEnoughAndSelect = false;
        }
    }
    private void payAli(){
        if (order == null || order.out_trade_no == null){
            return;
        }
        if ("0".equals(userId)){
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("out_trade_no", order.out_trade_no)
                .add("action","order")
                .add("type","alipay")
                .add("balance",selectBalance)
                .build();
        OkHttpUtil.post(HttpUrl.payByAli, body, new Subscriber<String>() {
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
                    final AliToken token = GsonUtil.jsonToObject(result.getResult(), AliToken.class);
                    OkHttpUtil.executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            PayTask pay = new PayTask(context);
                            Map<String, String> result = pay.payV2(token.token, true);
                            String resultStatus = result.get("resultStatus");
//                            String memo = result.get("memo");
                            if ("9000".equals(resultStatus)) {
                                view.payOrderSuccess();
                            } else {
                            }
                        }
                    });
                }
            }
        });
    }
    private void payWx(){
//        ToastUtil.show("未集成微信SDK");
        if (order == null || order.out_trade_no == null){
            return;
        }
        if ("0".equals(userId)){
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("out_trade_no", order.out_trade_no)
                .add("action","order")
                .add("type","wxpay")
                .add("balance",selectBalance)
                .build();
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
                    IWXAPI api = WXAPIFactory.createWXAPI(context, CommonCode.APP_ID_WX, false);
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
    public void cancelOrder() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (order == null || order.out_trade_no == null){
            view.showToast("获取订单失败");
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        String url = HttpUrl.cancelOrderUrl+OkHttpUtil.getSign()+"&user_id="+userId+"&out_trade_no="+order.out_trade_no;
        OkHttpUtil.get(url, new Subscriber<String>() {
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
                    view.cancelOrderSuccess();
                }
            }
        });
    }
}