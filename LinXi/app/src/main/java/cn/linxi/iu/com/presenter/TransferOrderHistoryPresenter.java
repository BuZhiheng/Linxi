package cn.linxi.iu.com.presenter;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.TransferOrderDetail;
import cn.linxi.iu.com.presenter.ipresenter.IOrderHistoryPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.ITransferOrderDetailView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2017/5/17.
 */
public class TransferOrderHistoryPresenter implements IOrderHistoryPresenter {
    private ITransferOrderDetailView view;
    public TransferOrderHistoryPresenter(ITransferOrderDetailView view) {
        this.view = view;
    }
    @Override
    public void getOrderList(final int page) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID, 0);
        String url = HttpUrl.transferSaleMarketOrderDetail + OkHttpUtil.getSign() + "&status=" + 1 + "&user_id=" + userId+"&page="+page;
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
                if (result.success()){
                    List<TransferOrderDetail> list = GsonUtil.jsonToList(result.data.result,TransferOrderDetail.class);
                    if (list != null && list.size() > 0){
                        view.setData(list);
                    } else {
                        if (page == 1){
                            view.setNoData();
                        }
                    }
                }
            }
        });
    }
}