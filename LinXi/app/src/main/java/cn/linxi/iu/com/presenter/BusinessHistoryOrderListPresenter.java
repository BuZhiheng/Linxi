package cn.linxi.iu.com.presenter;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.BusinessHistoryOrder;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessHistoryOrderListPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessHistoryOrderListView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/26.
 */
public class BusinessHistoryOrderListPresenter implements IBusinessHistoryOrderListPresenter {
    private IBusinessHistoryOrderListView view;
    public BusinessHistoryOrderListPresenter(IBusinessHistoryOrderListView view){
        this.view = view;
    }
    @Override
    public void getOrderList(String status, final int page) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        int operatId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0);
        String url = HttpUrl.businessHistoryOrderList+OkHttpUtil.getSign()+"&status="+status+"&operat_id="+operatId+"&page="+page;
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
                    List<BusinessHistoryOrder> list = GsonUtil.jsonToList(result.data.result,BusinessHistoryOrder.class);
                    if (list != null && list.size() > 0){
                        view.getOrderListSuccess(list);
                    } else {
                        if (page == 1){
                            view.setNoData();
                        }
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}