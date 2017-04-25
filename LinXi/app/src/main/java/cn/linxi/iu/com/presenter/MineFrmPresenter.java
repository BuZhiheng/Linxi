package cn.linxi.iu.com.presenter;
import android.support.v4.widget.SwipeRefreshLayout;

import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.UserCenterInfo;
import cn.linxi.iu.com.presenter.ipresenter.IMineFrmPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IMineFrmView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/18.
 */
public class MineFrmPresenter implements IMineFrmPresenter {
    private IMineFrmView view;
    public MineFrmPresenter(IMineFrmView view){
        this.view = view;
    }
    @Override
    public void getUserInfo(SwipeRefreshLayout refresh) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        setTimeOut(refresh);
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
                    PrefUtil.putString(CommonCode.SP_USER_PHONE, info.mobile);
                    PrefUtil.putString(CommonCode.SP_USER_USERNAME, info.user_name);
                    PrefUtil.putString(CommonCode.SP_USER_BALANCE, info.balance);
                    PrefUtil.putString(CommonCode.SP_USER_PHOTO, info.avatar);
                    PrefUtil.putString(CommonCode.SP_USER_LAST_ALIACCOUNT, info.pay_account);
                    PrefUtil.putString(CommonCode.SP_USER_LAST_BANKACCOUNT, info.bank_number);
                    PrefUtil.putString(CommonCode.SP_USER_INVITE_MOBILE, info.invite_mobile);
                    PrefUtil.putString(CommonCode.SP_USER_NICKNAME, info.nickname);
                    PrefUtil.putString(CommonCode.SP_USER_VIPDESC, info.vip_desc);//
                    PrefUtil.putString(CommonCode.SP_USER_GAME_RULE, info.game_rule);
                    PrefUtil.putString(CommonCode.SP_USER_ENVELOP_RULE, info.envelope_rule);
                    if (StringUtil.isNull(info.mobile)){
                        info.mobile = info.nickname;
                    }
                    view.getInfoSuccess(info);
                    if (info.is_vip != null){
                        PrefUtil.putInt(CommonCode.SP_USER_USER_IS_VIP, info.is_vip);
                        if (info.is_vip == 0){
                            view.isVip(R.drawable.ic_minefrm_vip0,"");
                        } else if (info.is_vip == 1){
                            view.isVip(R.drawable.ic_minefrm_vip1,"");
                        } else if (info.is_vip == 2){
                            view.isVip(R.drawable.ic_minefrm_vip2,"");
                        } else if (info.is_vip == 3){
                            view.isVip(R.drawable.ic_minefrm_vip3,"");
                        }
                    } else {
                        PrefUtil.putInt(CommonCode.SP_USER_USER_IS_VIP, 0);
                        view.isVip(R.drawable.ic_minefrm_vip0, "");
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