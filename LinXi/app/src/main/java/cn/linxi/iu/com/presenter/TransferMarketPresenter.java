package cn.linxi.iu.com.presenter;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.SaleOilCard;
import cn.linxi.iu.com.presenter.ipresenter.ITransferMarketPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.ITransferMarketView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/5/4.
 */
public class TransferMarketPresenter implements ITransferMarketPresenter {
    private ITransferMarketView view;
    public TransferMarketPresenter(ITransferMarketView view){
        this.view = view;
    }
    @Override
    public void getData() {
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        String url = HttpUrl.transferSaleMarket+ OkHttpUtil.getSign()+"&user_id="+userId;
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
                    List<SaleOilCard> list = GsonUtil.jsonToList(result.data.result,SaleOilCard.class);
                    view.setTransferMarket(list);
                } else {
                }
            }
        });
    }
}