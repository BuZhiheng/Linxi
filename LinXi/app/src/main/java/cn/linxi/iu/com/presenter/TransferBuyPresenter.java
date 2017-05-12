package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.widget.EditText;

import java.util.List;

import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.model.StationOilType;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.ipresenter.ITransferBuyPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.ITransferBuyView;
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
    public String onCoutSub(EditText editText) {
        return null;
    }
    @Override
    public String onCoutAdd(EditText editText) {
        return null;
    }
    @Override
    public void sure(EditText price, EditText cout) {
    }
}