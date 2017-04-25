package cn.linxi.iu.com.presenter;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HistoryOrder;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IOrderHistoryPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IOrderHistoryView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/22.
 */
public class OrderHistoryPresenter implements IOrderHistoryPresenter {
    private IOrderHistoryView view;
    public OrderHistoryPresenter(IOrderHistoryView view){
        this.view = view;
    }
    @Override
    public void getOrderList(final int page) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
        String url = HttpUrl.getOrderListUrl + OkHttpUtil.getSign() + "&state=" + 1 + "&user_id=" + userId+"&page="+page;
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
                    List<HistoryOrder> list = GsonUtil.jsonToList(result.data.result,HistoryOrder.class);
                    if (list != null && list.size() > 0){
                        view.setOrderList(list);
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