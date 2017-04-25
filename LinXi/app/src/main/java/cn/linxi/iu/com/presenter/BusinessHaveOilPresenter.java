package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.BusinessOrderCreated;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.CustomerOilCard;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessHaveOilPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessHaveOilView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/25.
 */
public class BusinessHaveOilPresenter implements IBusinessHaveOilPresenter {
    private IBusinessHaveOilView view;
    private String code;
    public BusinessHaveOilPresenter(IBusinessHaveOilView view){
        this.view = view;
    }
    @Override
    public void getCustomerMsg(Intent intent) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (intent == null || intent.getStringExtra(CommonCode.INTENT_QRCODE) == null){
            view.showToast("获取用户信息失败");
            return;
        }
        code = intent.getStringExtra(CommonCode.INTENT_QRCODE);
        String stationId = PrefUtil.getString(CommonCode.SP_USER_STATION_ID, "");
        String operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0)+"";
        RequestBody body = new FormBody.Builder()
//                .add("token",token)
                .add("station_id",stationId)
                .add("user_id",operatId)
                .build();
        OkHttpUtil.post(HttpUrl.finalUrl+code, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
//                ToastUtil.show(s);
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    List<CustomerOilCard> list = GsonUtil.jsonToList(result.data.result,CustomerOilCard.class);
                    if (list == null || list.size() == 0){
                        view.havanoOil();
                    } else {
                        view.getCustomerMsgSuccess(list);
                    }
                } else {
                    view.customerHaveNoOilCard(result.error);
                }
            }
        });
    }
    @Override
    public void order(CustomerOilCard selectCard, EditText purchase) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (selectCard == null){
            view.showToast("请选择购买类型");
            return;
        }
        String sPurchase = purchase.getText().toString();
        if (StringUtil.isNull(sPurchase)){
            view.showToast("请输入油/气量");
            return;
        }
        if (sPurchase.startsWith(".")||sPurchase.endsWith(".")) {
            view.showToast("输入油/气量有误");
            return;
        }
        if (StringUtil.isNull(selectCard.purchase)){
            view.showToast("该油卡剩余油/气量不足");
            return;
        }
        try {
            if (Float.parseFloat(sPurchase) == 0){
                view.showToast("输入油/气量有误");
                return;
            }
            if (Float.parseFloat(sPurchase) < 1){
                view.showToast("输入数值至少为1");
                return;
            }
        } catch (Exception e){
            view.showToast("输入油/气量有误");
            return;
        }
        String stationId = PrefUtil.getString(CommonCode.SP_USER_STATION_ID, "");
        String operaId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", selectCard.user_id)
                .add("operat_id", operaId)
                .add("station_id", stationId)
                .add("details_id", selectCard.details_id)
                .add("card_id",selectCard.card_id)
                .add("channel",selectCard.channel)
                .add("purchase", sPurchase)
                .build();
        String url = HttpUrl.businessOrderCreate;
        if (!StringUtil.isNull(code) && code.contains("source=weixin")){
            url = HttpUrl.businessOrderCreateWX;
        }
        OkHttpUtil.post(url, body, new Subscriber<String>() {
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
                    BusinessOrderCreated order = GsonUtil.jsonToObject(result.getResult(), BusinessOrderCreated.class);
                    if (order != null){
                        view.orderCreatedSuccess(order);
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}