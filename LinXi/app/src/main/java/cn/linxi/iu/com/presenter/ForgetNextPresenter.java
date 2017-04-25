package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.widget.EditText;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.ipresenter.IForgetNextPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IForgetNextView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public class ForgetNextPresenter implements IForgetNextPresenter {
    private IForgetNextView view;
    public ForgetNextPresenter(IForgetNextView view){
        this.view = view;
    }
    @Override
    public void forget(Intent intent, EditText psd, EditText psdConfirm) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (intent == null || intent.getSerializableExtra(CommonCode.INTENT_REGISTER_USER) == null){
            return;
        }
        String strPsd = psd.getText().toString();
        final String strPsdConfirm = psdConfirm.getText().toString();
        if (StringUtil.isNull(strPsd)){
            view.showToast("请输入密码");
            return;
        }
        if (StringUtil.isNull(strPsdConfirm)){
            view.showToast("请再次输入密码");
            return;
        }
        if (StringUtil.strEXChinese(strPsd, StringUtil.EX_CHINESE)){
            view.showToast("密码不能包含中文");
            return;
        }
        if (!strPsd.equals(strPsdConfirm)){
            view.showToast("两次密码输入不一致");
            return;
        }
        if (strPsd.length() < 8){
            view.showToast("密码长度至少8位");
            return;
        }
        User user = (User) intent.getSerializableExtra(CommonCode.INTENT_REGISTER_USER);
        RequestBody body = new FormBody.Builder()
                .add("mobile", user.mobile)
                .add("password", strPsd)
                .add("repassword", strPsdConfirm)
                .build();
        OkHttpUtil.post(HttpUrl.findLoginPsdUrl, body, new Subscriber<String>() {
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
                    view.showToast(result.error);
                    view.findPsdSuccess();
                    PrefUtil.putString(CommonCode.SP_USER_PASSWORD,strPsdConfirm);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}