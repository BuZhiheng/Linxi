package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.widget.EditText;
import org.xutils.common.util.MD5;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.ipresenter.IRegisterNextPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IRegisterNextView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public class RegisterNextPresenter implements IRegisterNextPresenter {
    private IRegisterNextView view;
    public RegisterNextPresenter(IRegisterNextView view){
        this.view = view;
    }
    @Override
    public void register(Intent intent, final EditText psd, EditText psdConfirm,EditText etInvite) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        if (intent == null || intent.getSerializableExtra(CommonCode.INTENT_REGISTER_USER) == null){
            return;
        }
        String strPsd = psd.getText().toString();
        String strPsdConfirm = psdConfirm.getText().toString();
        String strInvite = etInvite.getText().toString();
        if (StringUtil.isNull(strPsd)){
            view.showToast("请输入密码");
            return;
        }
        if (StringUtil.isNull(strPsdConfirm)){
            view.showToast("请再次输入密码");
            return;
        }
        if (StringUtil.strEXChinese(strPsd,StringUtil.EX_CHINESE)){
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
                .add("invite_mobile", strInvite)
                .build();
        OkHttpUtil.post(HttpUrl.registerUrl, body, new Subscriber<String>() {
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
                    PrefUtil.clear();
                    User user = GsonUtil.jsonToObject(result.getResult(),User.class);
                    PrefUtil.putString(CommonCode.SP_USER_USERNAME, user.mobile);
                    PrefUtil.putString(CommonCode.SP_USER_PASSWORD, user.password);
                    if (user.user_id != null){
                        PrefUtil.putInt(CommonCode.SP_USER_USERID, user.user_id);
                        PrefUtil.putString(CommonCode.SP_USER_IM_TOKEN, user.im_token);
                        String md5 = MD5.md5(user.user_id + CommonCode.APP_KEY);
                        if (md5.equals(user.hash)){
                            PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN, true);
                            PrefUtil.putInt(CommonCode.SP_USER_PHONEISBIND, 1);
                            view.registerSuccess();
                        } else {
                            view.showToast("注册失败");
                        }
                    } else {
                        view.showToast("注册失败");
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}