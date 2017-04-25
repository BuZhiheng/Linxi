package cn.linxi.iu.com.presenter;
import android.support.v4.widget.SwipeRefreshLayout;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.SaleOilResult;
import cn.linxi.iu.com.presenter.ipresenter.ISalePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.ISaleView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/25.
 */
public class SalePresenter implements ISalePresenter {
    private ISaleView view;
    public SalePresenter(ISaleView view){
        this.view = view;
    }
    @Override
    public void getSaleCard(final int page, final SwipeRefreshLayout refresh) {
        if (!PrefUtil.getBoolean(CommonCode.SP_WAIT_IS_OPEN_SALE,false)){
            view.haveNoOpen();
            return;
        }
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        setTimeOut(refresh);
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
        String url = HttpUrl.getSaleOilCardUrl + OkHttpUtil.getSign() + "&user_id=" +userId + "&page="+page;
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
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    SaleOilResult r = GsonUtil.jsonToObject(result.getResult(), SaleOilResult.class);
                    if (page == 1){
                        if (r == null || r.list == null || r.list.size() == 0){
                            view.noneOilCard(result.error);
                        } else {
                            view.haveOilCard();
                            view.setSaleOilCard(r.list);
                            view.setTotal(r.total);
                        }
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    public void setTimeOut(final SwipeRefreshLayout refresh) {
        OkHttpUtil.executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(CommonCode.HTTP_TIMEOUT);
                            if (refresh.isRefreshing()) {
                                view.timeOut();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}