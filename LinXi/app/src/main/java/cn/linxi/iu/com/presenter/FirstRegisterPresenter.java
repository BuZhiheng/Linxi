package cn.linxi.iu.com.presenter;
import cn.linxi.iu.com.R;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EnvelopChance;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IFirstRegisterPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IFirstRegisterView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by buzhiheng on 2016/12/23.
 */
public class FirstRegisterPresenter implements IFirstRegisterPresenter {
    private IFirstRegisterView view;
    public FirstRegisterPresenter(IFirstRegisterView view){
        this.view = view;
        PrefUtil.putBoolean(CommonCode.SP_IS_NEW_USER, false);
    }
    @Override
    public void getEnvelop() {
        if (!SystemUtils.networkState()){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .build();
        OkHttpUtil.post(HttpUrl.firstLoginEnvelopUrl, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    EnvelopChance chance = GsonUtil.jsonToObject(result.getResult(), EnvelopChance.class);
                    view.showToast(result.error);
                    if (chance.chance) {
                        view.showEnvelopResult(R.drawable.ic_firstregiser_envelop_get);
                    } else {
                        view.showEnvelopResult(R.drawable.ic_firstregiser_envelop_none);
                    }
                } else {
                    view.showToast(result.error);
                    view.showEnvelopResult(R.drawable.ic_firstregiser_envelop_none);
                }
            }
        });
    }
}