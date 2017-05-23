package cn.linxi.iu.com.presenter;
import android.content.Intent;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.TransferOrder;
import cn.linxi.iu.com.model.User;
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
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void pay(String type) {
    }
}