package cn.linxi.iu.com.presenter;
import android.content.Intent;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.TIModelCustomerOrderSure;
import cn.linxi.iu.com.presenter.ipresenter.ICustomerOrderSurePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.ICustomerOrderSureView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/9/9.
 */
public class CustomerOrderSurePresenter implements ICustomerOrderSurePresenter {
    private ICustomerOrderSureView view;
    private TIModelCustomerOrderSure order;
    public CustomerOrderSurePresenter(ICustomerOrderSureView view){
        this.view = view;
    }
    @Override
    public void getOrder(Intent intent) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (intent != null){
            TIModelCustomerOrderSure order = (TIModelCustomerOrderSure) intent.getSerializableExtra(CommonCode.INTENT_REGISTER_USER);
            if (order != null){
                this.order = order;
                view.setIntentOrder(order);
            }
        }
    }
    @Override
    public void orderConfirm() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (order == null){
            view.showToast("订单有误");
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("out_trade_no",order.out_trade_no)
                .build();
        OkHttpUtil.post(HttpUrl.addOrderConfirmUrl, body, new Subscriber<String>() {
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
                    view.orderConfirmSuccess();
                }
                view.showToast(result.error);
            }
        });
    }
}