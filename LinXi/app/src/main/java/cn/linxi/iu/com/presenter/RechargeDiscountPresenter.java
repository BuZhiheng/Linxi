package cn.linxi.iu.com.presenter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

import cn.linxi.iu.com.model.AliToken;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.RechargeDiscount;
import cn.linxi.iu.com.model.WXPayToken;
import cn.linxi.iu.com.presenter.ipresenter.IRechargeDiscountPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IRechargeDiscountView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/3/8.
 */
public class RechargeDiscountPresenter implements IRechargeDiscountPresenter {
    private IRechargeDiscountView view;
    private Intent intent;
    private RechargeDiscount selectDis;
    public RechargeDiscountPresenter(IRechargeDiscountView view, Intent intent){
        this.view = view;
        this.intent = intent;
    }
    @Override
    public void getDiscount() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID, 0)+"";
        if (StringUtil.isNull(userId)){
            view.showToast("用户信息有误");
            return;
        }
        String cid = intent.getStringExtra(CommonCode.INTENT_STATION_CID);
        if (StringUtil.isNull(cid)){
            view.showToast("集团信息有误");
            return;
        }
        String url = HttpUrl.rechargeDiscount + OkHttpUtil.getSign() + "&user_id=" + userId+ "&cid=" + cid;
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
                if (result.success()){
                    List<RechargeDiscount> list = GsonUtil.jsonToList(result.data.result, RechargeDiscount.class);
                    selectDis = list.get(0);
                    view.setDiscount(list);
                    if (list.size() == 0){
                        view.showToast("该加油站未提供储值卡业务");
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void onItemClick(RechargeDiscount discount) {
        selectDis = discount;
    }

    @Override
    public void pay(int payType) {
        if (selectDis == null){
            return;
        }
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        String sid = selectDis.dsid;
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("dsid",sid)
                .build();
        if (payType == CommonCode.PAY_BY_ZFB){
            payByZFB(body);
        } else {
            payByWX(body);
        }
    }
    private void payByWX(RequestBody body) {
        OkHttpUtil.post(HttpUrl.payByWXRecharge, body, new Subscriber<String>() {
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
    private void payByZFB(RequestBody body) {
        OkHttpUtil.post(HttpUrl.payByAliRecharge, body, new Subscriber<String>() {
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
}