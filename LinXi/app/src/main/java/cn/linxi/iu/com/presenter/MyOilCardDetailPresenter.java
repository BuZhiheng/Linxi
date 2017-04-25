package cn.linxi.iu.com.presenter;
import android.content.Intent;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.model.SaleOilCardMsg;
import cn.linxi.iu.com.presenter.ipresenter.IMyOilCardDetailPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IMyOilCardDetailView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/9/23.
 */
public class MyOilCardDetailPresenter implements IMyOilCardDetailPresenter {
    private IMyOilCardDetailView view;
    private SaleOilCard card;
    public MyOilCardDetailPresenter(IMyOilCardDetailView view){
        this.view = view;
    }
    @Override
    public void getCardMsg(Intent intent) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (intent != null && intent.getSerializableExtra(CommonCode.INTENT_REGISTER_USER) != null){
            card = (SaleOilCard) intent.getSerializableExtra(CommonCode.INTENT_REGISTER_USER);
            if (card != null){
                String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
                String url = HttpUrl.getSaleOilCardDataUrl+ OkHttpUtil.getSign()+"&user_id="+userId+"&station_id="+card.station_id+"&oil_type="+card.oil_type;
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
                            SaleOilCardMsg msg = GsonUtil.jsonToObject(result.getResult(),SaleOilCardMsg.class);
                            view.setContent(msg);
                        } else {
                            view.showToast(result.error);
                        }
                    }
                });
            }
        }
    }
    @Override
    public void checkPsdBind() {
        if (card == null){
            view.showToast("油卡信息获取失败,请重试");
            return;
        }
        if (PrefUtil.getInt(CommonCode.SP_USER_PHONEISBIND,0) == 0){
            view.showNotBindPhone();
            return;
        }
        if (PrefUtil.getInt(CommonCode.SP_USER_PAYPSDISBIND,0) == 0){
            view.showNotBindPayPsd();
            return;
        }
        view.showPayPsdDialog();
    }
    @Override
    public void sale(String psd) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("station_id", card.station_id)
                .add("oil_type",card.oil_type)
                .add("pay_password", psd)
                .build();
        OkHttpUtil.post(HttpUrl.saleOilCardUrl, body, new Subscriber<String>() {
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
                    view.saleSuccess();
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}