package cn.linxi.iu.com.presenter;
import android.app.Activity;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.WaitForOpen;
import cn.linxi.iu.com.presenter.ipresenter.IMainPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.view.iview.IMainView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/10/20.
 */
public class MainPresenter implements IMainPresenter {
    private IMainView view;
    private Activity context;
    public MainPresenter(IMainView view){
        this.view = view;
        this.context = (Activity) view;
        PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN, true);
    }
    @Override
    public void getUnfinishOrder() {
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID, 0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .build();
        OkHttpUtil.post(HttpUrl.getTIMOrderUrl, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
//                ToastUtil.show(s);
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    WaitForOpen open = GsonUtil.jsonToObject(result.getResult(), WaitForOpen.class);
                    if (open != null) {
                        PrefUtil.putBoolean(CommonCode.SP_WAIT_IS_OPEN_SALE, open.sale);
                        PrefUtil.putBoolean(CommonCode.SP_WAIT_IS_OPEN_PAST, open.past);
                        PrefUtil.putBoolean(CommonCode.SP_WAIT_IS_OPEN_ENVELOP, open.envelop);
                        if (open.share != null){
                            PrefUtil.putString(CommonCode.SP_SHARE_TITLE, open.share.getTitle());
                            PrefUtil.putString(CommonCode.SP_SHARE_DESC, open.share.getDesc());
                            PrefUtil.putString(CommonCode.SP_SHARE_URL,open.share.getUrl());
                        }
                    }
                }
            }
        });
    }
}