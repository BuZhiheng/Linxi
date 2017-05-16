package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.widget.EditText;

import java.util.List;

import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.Order;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.model.TransferBuyCaculate;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.ipresenter.ITransferBuyPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.WindowUtil;
import cn.linxi.iu.com.view.iview.ITransferBuyView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by buzhiheng on 2017/5/12.
 */
public class TransferBuyPresenter implements ITransferBuyPresenter {
    private ITransferBuyView view;
    private String stationId;
    public TransferBuyPresenter(ITransferBuyView view, Intent intent){
        this.view = view;
        stationId = intent.getStringExtra(CommonCode.INTENT_STATION_ID);
    }
    @Override
    public void getData() {
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String url = HttpUrl.transferSaleMarketDetail+OkHttpUtil.getSign()+"&user_id="+User.getUserId()+"&station_id="+stationId;
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
                    SaleOilCard sale = GsonUtil.jsonToObject(result.getResult(),SaleOilCard.class);
                    view.setData(sale);
                    if (sale.tags != null){
                        List<StationOilType> list = GsonUtil.jsonToList(sale.tags,StationOilType.class);
                        for (int i=0;i<list.size();i++){
                            view.addOilType(list.get(i),i);
                        }
                    }
                }
            }
        });
    }
    @Override
    public String onCoutSub(EditText editText, StationOilType selecedPrice) {
        String cout = editText.getText().toString();
        if (StringUtil.isWrongNum(cout)){
            return  "1";
        }
        float f = Float.parseFloat(cout);
        float f_ = f - 1;
        if (f_ <= 0){
            return "1";
        } else {
            if (selecedPrice == null){
                return WindowUtil.getRoundFloat(f_)+"";
            } else {
                if (Float.parseFloat(selecedPrice.max_purchase) > f_){
                    return WindowUtil.getRoundFloat(f_)+"";
                }
                return selecedPrice.max_purchase;
            }
        }
    }
    @Override
    public String onCoutAdd(EditText editText, StationOilType selecedPrice) {
        String cout = editText.getText().toString();
        if (StringUtil.isWrongNum(cout)){
            return  "1";
        }
        float f = Float.parseFloat(cout);
        f = f + 1;
        if (selecedPrice == null){
            return WindowUtil.getRoundFloat(f)+"";
        } else {
            if (Float.parseFloat(selecedPrice.max_purchase) > f){
                return WindowUtil.getRoundFloat(f)+"";
            }
            return selecedPrice.max_purchase;
        }
    }
    @Override
    public void sure(EditText price, EditText cout) {
    }
    @Override
    public void getTransferMoney(EditText editText, StationOilType selecedPrice) {
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (selecedPrice == null){
            return;
        }
        String sPurchase = editText.getText().toString();
        if (StringUtil.isWrongNum(sPurchase)){
            return;
        }
        float fPurchase = Float.valueOf(sPurchase);
        float fMax = Float.valueOf(selecedPrice.max_purchase);
        if (fPurchase > fMax){
            return;
        }
        String type = selecedPrice.oil_type.replace("#","");
        String url = HttpUrl.transferSaleMarketDetailGetMoney+OkHttpUtil.getSign()+"&user_id="+User.getUserId()+"&station_id="+stationId+"&oil_type="+type+"&purchase="+sPurchase;
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
                    TransferBuyCaculate caculate = GsonUtil.jsonToObject(result.getResult(),TransferBuyCaculate.class);
                    view.setCaculate(caculate);
                }
            }
        });
    }
    @Override
    public void order(EditText editText, StationOilType selecedPrice) {
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (selecedPrice == null){
            return;
        }
        String sPurchase = editText.getText().toString();
        if (StringUtil.isWrongNum(sPurchase)){
            view.showToast("输入有误");
            return;
        }
        float fPurchase = Float.valueOf(sPurchase);
        float fMax = Float.valueOf(selecedPrice.max_purchase);
        if (fPurchase > fMax){
            view.showToast("最多输入"+selecedPrice.max_purchase+"L");
            return;
        }
        String type = selecedPrice.oil_type.replace("#","");
        RequestBody body = new FormBody.Builder()
                .add("user_id",User.getUserId())
                .add("station_id",stationId)
                .add("oil_type",type)
                .add("purchase",sPurchase)
                .build();
        OkHttpUtil.post(HttpUrl.transferSaleMarketDetailOrder, body, new Subscriber<String>() {
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
                    Order order = GsonUtil.jsonToObject(result.getResult(), Order.class);
                    view.orderSuccess(order);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}