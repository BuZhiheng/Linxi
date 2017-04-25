package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.linxi.iu.com.model.AliToken;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.WXPayToken;
import cn.linxi.iu.com.presenter.ipresenter.IRechargePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.activity.RechargeActivity;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/22.
 */
public class RechargePresenter implements IRechargePresenter {
    private RechargeActivity view;
    public RechargePresenter(RechargeActivity view){
        this.view = view;
    }
    @Override
    public void recharge(int type, EditText etCout) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String cout = etCout.getText().toString();
        if (StringUtil.isNull(cout)){
            view.showToast("请输入金额");
            return;
        }
        if (cout.startsWith(".")||cout.endsWith(".")){
            view.showToast("输入金额金额有误");
            return;
        }
        if (type == CommonCode.PAY_BY_ZFB){
            rechargeByAli(cout);
        } else if (type == CommonCode.PAY_BY_WX) {
            rechargeByWx(cout);
        }
    }
    private void rechargeByAli(String amount){
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        if ("0".equals(userId)){
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("amount", amount)
                .add("action","recharge")
                .add("pay_channel","alipay")
                .build();
        OkHttpUtil.post(HttpUrl.recharge, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
//                ToastUtil.show(s);
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    AliToken token = GsonUtil.jsonToObject(result.getResult(),AliToken.class);
                    payAli(token.token);
                }
            }
        });
    }
    private void rechargeByWx(String amount){
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        if ("0".equals(userId)){
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("amount", amount)
                .add("action","recharge")
                .add("pay_channel","wxpay")
                .build();
        OkHttpUtil.post(HttpUrl.recharge, body, new Subscriber<String>() {
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
                    IWXAPI api = WXAPIFactory.createWXAPI(view, CommonCode.APP_ID_WX, false);
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
    private void payAli(final String token){
        OkHttpUtil.executor.execute(new Runnable() {
            @Override
            public void run() {
                PayTask pay = new PayTask(view);
                view.aliPayResult(pay.payV2(token, true));
            }
        });
    }
}