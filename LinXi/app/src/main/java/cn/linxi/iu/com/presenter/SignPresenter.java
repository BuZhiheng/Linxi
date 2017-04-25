package cn.linxi.iu.com.presenter;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.Sign;
import cn.linxi.iu.com.model.SignReward;
import cn.linxi.iu.com.presenter.ipresenter.ISignPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.view.iview.ISignView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/29.
 */
public class SignPresenter implements ISignPresenter {
    private ISignView view;
    public SignPresenter(ISignView view){
        this.view = view;
    }
    @Override
    public void sign() {
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .build();
        OkHttpUtil.post(HttpUrl.userSign, body, new Subscriber<String>() {
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
                    SignReward reward = GsonUtil.jsonToObject(result.getResult(),SignReward.class);
                    view.signSuccess(reward);
                }
                view.showToast(result.error);
            }
        });
    }
    @Override
    public void getSigned() {
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
        String url = HttpUrl.userSignList+ OkHttpUtil.getSign()+"&user_id="+userId;
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
                    List<Sign> list = GsonUtil.jsonToList(result.data.result,Sign.class);
                    if (list != null){
                        view.setSignView(list);
                    }
                }
            }
        });
    }
}