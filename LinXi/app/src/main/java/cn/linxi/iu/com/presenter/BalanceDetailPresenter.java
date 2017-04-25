package cn.linxi.iu.com.presenter;
import java.util.List;
import cn.linxi.iu.com.model.BalanceDetail;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBalanceDetailPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBalanceDetailView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/26.
 */
public class BalanceDetailPresenter implements IBalanceDetailPresenter {
    private IBalanceDetailView view;
    public BalanceDetailPresenter(IBalanceDetailView view){
        this.view = view;
    }
    @Override
    public void getBalanceList(int page) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
        String url = HttpUrl.getBalanceList+OkHttpUtil.getSign()+"&query=balance&user_id="+userId+"&page="+page;
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
//                view.showToast(s);
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    List<BalanceDetail> list = GsonUtil.jsonToList(result.data.result,BalanceDetail.class);
                    view.getListSuccess(list);
                }
            }
        });
    }
}