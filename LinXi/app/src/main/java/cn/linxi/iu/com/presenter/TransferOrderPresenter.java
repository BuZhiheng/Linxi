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
import cn.linxi.iu.com.model.TransferOrder;
import cn.linxi.iu.com.model.TransferOrderDetail;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.model.WXPayToken;
import cn.linxi.iu.com.presenter.ipresenter.ITransferOrderPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.ITransferOrderView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/5/22.
 */
public class TransferOrderPresenter implements ITransferOrderPresenter {
    private ITransferOrderView view;
    private String oid;
    public TransferOrderPresenter(ITransferOrderView view,Intent intent){
        this.view = view;
        oid = intent.getStringExtra(CommonCode.INTENT_ORDER_ID);
    }
    @Override
    public void getOrder() {
        if (StringUtil.isNull(oid)){
            return;
        }
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String url = HttpUrl.transferSaleMarketDetailGetOrder+ OkHttpUtil.getSign()+"&user_id="+ User.getUserId()+"&oid="+oid;
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
                    TransferOrder order = GsonUtil.jsonToObject(result.getResult(),TransferOrder.class);
                    view.setOrderData(order);
                    List<TransferOrderDetail> list = GsonUtil.jsonToList(order.json,TransferOrderDetail.class);
                    for (int i=0;i<list.size();i++){
                        view.setOrderItem(list.get(i));
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void pay(int type) {
        if (StringUtil.isNull(oid)){
            return;
        }
        if (type == CommonCode.PAY_BY_ZFB){
            payByAli();
        } else {
            payByWx();
        }
    }

    private void payByAli() {
        String url = HttpUrl.transferSaleMarketOrderPayZFB+OkHttpUtil.getSign()+"&oid="+oid+"&user_id="+User.getUserId();
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
    private void payByWx() {
        String url = HttpUrl.transferSaleMarketOrderPayWX+OkHttpUtil.getSign()+"&oid="+oid+"&user_id="+User.getUserId();
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
}