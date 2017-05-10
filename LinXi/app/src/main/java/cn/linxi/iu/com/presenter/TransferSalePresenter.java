package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.widget.EditText;

import java.util.List;

import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.TransferSaleDetail;
import cn.linxi.iu.com.model.TransferSaleTagPrice;
import cn.linxi.iu.com.presenter.ipresenter.ITransferSalePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.WindowUtil;
import cn.linxi.iu.com.view.iview.ITransferSaleView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/5/10.
 */
public class TransferSalePresenter implements ITransferSalePresenter {
    private ITransferSaleView view;
    private Intent intent;
    public TransferSalePresenter(ITransferSaleView view,Intent intent){
        this.view = view;
        this.intent = intent;
    }
    @Override
    public void getData() {
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (StringUtil.isNull(intent.getStringExtra(CommonCode.INTENT_ORDER_ID))){
            return;
        }
        String cardId = intent.getStringExtra(CommonCode.INTENT_ORDER_ID);
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        String url = HttpUrl.oilCardTransferSaleDetail+OkHttpUtil.getSign()+"&user_id="+userId+"&card_id="+cardId;
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
                    TransferSaleDetail detail = GsonUtil.jsonToObject(result.getResult(),TransferSaleDetail.class);
                    view.setData(detail);
                    if (detail.tags != null){
                        List<TransferSaleTagPrice> prices = GsonUtil.jsonToList(detail.tags,TransferSaleTagPrice.class);
                        view.setTagPrice(prices);
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public String onPriceSub(EditText editText) {
        String cout = editText.getText().toString();
        if (StringUtil.isWrongNum(cout)){
            return  "1";
        }
        float f = Float.parseFloat(cout);
        float f_ = (float) (f - 0.01);
        if (f_ < 0){
            return "1";
        } else {
            return WindowUtil.getRoundFloat(f_)+"";
        }
    }
    @Override
    public String onPriceAdd(EditText editText) {
        String cout = editText.getText().toString();
        if (StringUtil.isWrongNum(cout)){
            return  "1";
        }
        float f = Float.parseFloat(cout);
        f = (float) (f + 0.01);
        return WindowUtil.getRoundFloat(f)+"";
    }
    @Override
    public String onCoutSub(EditText editText) {
        String cout = editText.getText().toString();
        if (StringUtil.isWrongNum(cout)){
            return  "1";
        }
        float f = Float.parseFloat(cout);
        float f_ = f - 1;
        if (f_ < 0){
            return "1";
        } else {
            return WindowUtil.getRoundFloat(f_)+"";
        }
    }
    @Override
    public String onCoutAdd(EditText editText) {
        String cout = editText.getText().toString();
        if (StringUtil.isWrongNum(cout)){
            return  "1";
        }
        float f = Float.parseFloat(cout);
        f = f + 1;
        return WindowUtil.getRoundFloat(f)+"";
    }
    @Override
    public void sure(EditText price, EditText purchase) {
        String sPrice = price.getText().toString();
        String sPurchase = purchase.getText().toString();
        if (StringUtil.isWrongNum(sPrice)){
            view.showToast("价格输入有误");
            return;
        }
        if (StringUtil.isWrongNum(sPurchase)){
            view.showToast("油量输入有误");
            return;
        }
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (StringUtil.isNull(intent.getStringExtra(CommonCode.INTENT_ORDER_ID))){
            return;
        }
        String cardId = intent.getStringExtra(CommonCode.INTENT_ORDER_ID);
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("card_id", cardId)
                .add("price",sPrice)
                .add("purchase",sPurchase)
                .build();
        OkHttpUtil.post(HttpUrl.oilCardTransferSaleCreate, body, new Subscriber<String>() {
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
                    view.saleSuccess();
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}