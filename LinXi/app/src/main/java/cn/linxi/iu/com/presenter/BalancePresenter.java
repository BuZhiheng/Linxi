package cn.linxi.iu.com.presenter;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.UserCenterInfo;
import cn.linxi.iu.com.presenter.ipresenter.IBalancePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBalanceView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/18.
 */
public class BalancePresenter implements IBalancePresenter {
    private IBalanceView view;
    public BalancePresenter(IBalanceView view){
        this.view = view;
    }
    @Override
    public void getUserInfo() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String url = HttpUrl.personalCenterUrl + OkHttpUtil.getSign() + "&user_id=" + PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
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
                    UserCenterInfo info = GsonUtil.jsonToObject(result.getResult(),UserCenterInfo.class);
                    PrefUtil.putString(CommonCode.SP_USER_BALANCE, info.balance);
                    view.getInfoSuccess(info);
                }
            }
        });
    }
}