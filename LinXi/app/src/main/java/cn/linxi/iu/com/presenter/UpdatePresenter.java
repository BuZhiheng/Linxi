package cn.linxi.iu.com.presenter;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.UpdateMsg;
import cn.linxi.iu.com.presenter.ipresenter.IUpdatePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.WindowUtil;
import cn.linxi.iu.com.view.iview.IUpdateView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/9/22.
 */
public class UpdatePresenter implements IUpdatePresenter {
    private IUpdateView view;
    private String url;
    public UpdatePresenter(IUpdateView view){
        this.view = view;
        int userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0);
        url = HttpUrl.updateUrl+ OkHttpUtil.getSign() + "&user_id="+userId;
    }
    @Override
    public void getUpdateCommon() {
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
                    UpdateMsg msg = GsonUtil.jsonToObject(result.getResult(),UpdateMsg.class);
                    if (msg.version != null && msg.version > WindowUtil.getAppVersionCode()){
                        if (msg.is_compel == 1){
                            view.showUpdate(msg);
                        } else {
                            String name = PrefUtil.getString(CommonCode.SP_APP_IGNORE_VERSION,"");
                            if (name.equals(msg.version_number)){
                                return;
                            }
                            view.showUpdate(msg);
                        }
                    }
                }
            }
        });
    }
    @Override
    public void getUpdateSetting() {
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
                    UpdateMsg msg = GsonUtil.jsonToObject(result.getResult(),UpdateMsg.class);
                    if (msg.version != null && msg.version > WindowUtil.getAppVersionCode()){
                        view.showUpdate(msg);
                    } else {
                        view.showToast("您安装的已经是最新版本");
                    }
                }
            }
        });
    }
}