package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.BusinessHistoryOrder;
import cn.linxi.iu.com.model.BusinessResetOrderMsg;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.CustomerOilCard;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessResetOrderPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessResetOrderView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/25.
 */
public class BusinessResetOrderPresenter implements IBusinessResetOrderPresenter {
    private IBusinessResetOrderView view;
    private BusinessHistoryOrder order;
    public BusinessResetOrderPresenter(IBusinessResetOrderView view){
        this.view = view;
    }
    @Override
    public void getCustomerMsg(Intent intent) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (intent == null){
            view.showToast("获取用户信息失败");
            return;
        }
        order = (BusinessHistoryOrder) intent.getSerializableExtra(CommonCode.INTENT_REGISTER_USER);
        final String url = HttpUrl.businessGetResetUnfinishOrder+OkHttpUtil.getSign()+"&user_id="+order.user_id+"&out_trade_no="+order.out_trade_no;
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
//                ToastUtil.show(s);
                Log.i(">>>>>",url);
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    BusinessResetOrderMsg msg = GsonUtil.jsonToObject(result.getResult(),BusinessResetOrderMsg.class);
                    if (msg != null && msg.order != null && msg.oil != null){
                        List<CustomerOilCard> cards = GsonUtil.jsonToList(msg.oil,CustomerOilCard.class);
                        view.getCustomerOrderSuccess(msg.order,cards);
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
        if (order == null){
            view.showToast("获取订单失败");
            return;
        }
        if (selectCard == null){
            view.showToast("请选择类型");
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
        String operaId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", order.user_id)
                .add("operat_id", operaId)
                .add("card_id", selectCard.card_id)
                .add("out_trade_no",order.out_trade_no)
                .add("details_id", selectCard.details_id)
                .add("purchase", sPurchase)
                .build();
        OkHttpUtil.post(HttpUrl.businessResetUnfinishOrder, body, new Subscriber<String>() {
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
                    view.orderCreatedSuccess();
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}