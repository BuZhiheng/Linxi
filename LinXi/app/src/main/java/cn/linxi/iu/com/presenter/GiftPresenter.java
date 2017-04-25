package cn.linxi.iu.com.presenter;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EnvelopUrl;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IGiftPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IGiftView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/12/22.
 */
public class GiftPresenter implements IGiftPresenter {
    private IGiftView view;
    private String userId;
    public GiftPresenter(IGiftView view){
        this.view = view;
        userId = PrefUtil.getInt(CommonCode.SP_USER_USERID, 0)+"";
    }
    @Override
    public void getEnvelop() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (StringUtil.isNull(userId)){
            view.showToast("用户信息有误");
            return;
        }
        String url = HttpUrl.personalEnvelopUrl + OkHttpUtil.getSign() + "&user_id=" + userId;
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
                    EnvelopUrl url = GsonUtil.jsonToObject(result.getResult(),EnvelopUrl.class);
                    if (url.chance){
                        view.toWebView("抢红包",url.location,PrefUtil.getString(CommonCode.SP_USER_ENVELOP_RULE, ""));
                    } else {
                        view.showToast("您还没有机会");
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void getPrize() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (StringUtil.isNull(userId)){
            view.showToast("用户信息有误");
            return;
        }
        String url = HttpUrl.personalPrizeUrl + OkHttpUtil.getSign() + "&user_id=" + userId;
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
                    EnvelopUrl url = GsonUtil.jsonToObject(result.getResult(),EnvelopUrl.class);
                    if (url.chance){
                        view.toWebView("抽奖机",url.location,PrefUtil.getString(CommonCode.SP_USER_GAME_RULE,""));
                    } else {
                        view.showToast("您还没有机会");
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}