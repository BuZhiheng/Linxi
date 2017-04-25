package cn.linxi.iu.com.presenter;
import android.app.Dialog;
import android.widget.EditText;
import org.xutils.common.util.MD5;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.TencentUser;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.ipresenter.ILoginPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.ILoginView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public class LoginPresenter implements ILoginPresenter {
    private ILoginView view;
    public LoginPresenter(ILoginView view){
        this.view = view;
        String type = PrefUtil.getString(CommonCode.SP_USER_LAST_LOGIN_TYPE, "");
        if (type.equals(CommonCode.LOGIN_BY_QQ)){
            view.setLastLoginQQ();
        } else if (type.equals(CommonCode.LOGIN_BY_WX)){
            view.setLastLoginWX();
        } else {
            view.setLastUsername(PrefUtil.getString(CommonCode.SP_USER_PHONE, ""));
        }
    }
    @Override
    public void login(String loginType, EditText username, final EditText psd, Dialog dialog) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String strUsername = username.getText().toString();
        final String strPassword = psd.getText().toString();
        if (StringUtil.isNull(strUsername)){
            view.showToast("请输入手机号");
            return;
        }
        if (StringUtil.isNull(strPassword)){
            view.showToast("请输入验证码");
            return;
        }
        setTimeOut(dialog);
        RequestBody body = new FormBody.Builder()
                .add("mobile", strUsername)
                .add("code", strPassword)
                .add("login_type", loginType)
                .build();
        OkHttpUtil.post(HttpUrl.loginUrl, body, new Subscriber<String>() {
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
                    User user = GsonUtil.jsonToObject(result.getResult(),User.class);
                    PrefUtil.putString(CommonCode.SP_USER_USERNAME,user.user_name);
                    PrefUtil.putString(CommonCode.SP_USER_PASSWORD, strPassword);
                    if (user.user_id != null){
                        PrefUtil.putInt(CommonCode.SP_USER_USERID, user.user_id);
                        PrefUtil.putInt(CommonCode.SP_USER_PHONEISBIND, user.is_bind);
                        PrefUtil.putInt(CommonCode.SP_USER_PAYPSDISBIND, user.pay_is_bind);
                        PrefUtil.putString(CommonCode.SP_USER_IM_TOKEN, user.im_token);
                        PrefUtil.putString(CommonCode.SP_USER_PHONE, user.mobile);
                        PrefUtil.putString(CommonCode.SP_USER_NICKNAME, "");
                        PrefUtil.putBoolean(CommonCode.SP_IS_NEW_USER, user.is_first);
                        String md5 = MD5.md5(user.user_id+CommonCode.APP_KEY);
                        if (md5.equals(user.hash)){
                            view.toMainActivity();
                        } else {
                            view.showToast("登录失败!");
                        }
                    } else {
                        view.showToast("登录失败");
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void getCode(EditText username) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String strPhone = username.getText().toString();
        if (StringUtil.isNull(strPhone)){
            view.showToast("请输入手机号");
            return;
        }
        if (!StringUtil.strEX(strPhone, StringUtil.EX_PHONE)){
            view.showToast("请输入正确的手机号");
            return;
        }
        OkHttpUtil.get(HttpUrl.getCodeUrl + OkHttpUtil.getCodeSign(strPhone)+ "&action=login&mobile=" + strPhone, new Subscriber<String>() {
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
                    view.showToast("验证码发送成功");
                    view.setCodeBtnCanNotClick();
                    OkHttpUtil.executor.execute(new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        try {
                                                            for (int i = 60; i > 0; i--) {
                                                                view.refreshCodeButton(i + "");
                                                                Thread.sleep(1000);
                                                            }
                                                            view.setCodeBtnCanClick();
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                    );
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void checkLoginType(String type) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String lastType = PrefUtil.getString(CommonCode.SP_USER_LAST_LOGIN_TYPE, "");
        if (CommonCode.LOGIN_BY_QQ.equals(type)){
            if (lastType.equals("")){
                view.loginByQQ();
            } else {
                if (lastType.equals(type)){
                    view.loginByQQ();
                } else {
                    view.showQQDialog("您上次使用的微信登录,本次确定用QQ?");
                }
            }
        } else if (CommonCode.LOGIN_BY_WX.equals(type)){
            if (lastType.equals("")){
                view.loginByWX();
            } else {
                if (lastType.equals(type)){
                    view.loginByWX();
                } else {
                    view.showWXDialog("您上次使用的QQ登录,本次确定用微信?");
                }
            }
        }
    }
    public void setTimeOut(final Dialog dialog) {
        OkHttpUtil.executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(CommonCode.HTTP_TIMEOUT);
                            if (dialog.isShowing()){
                                view.setTimeOut();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
    public void loginByService(final TencentUser tUser, final String type){
        /**
         * 第三方登录的方法
         * */
        FormBody.Builder builder = new FormBody.Builder()
                .add("nickname", tUser.nickname)
                .add("avatar", tUser.headimgurl)
                .add("login_type", type);
        if (CommonCode.LOGIN_BY_QQ.equals(type)){
            builder.add("openid", tUser.openid);
        } else {
            builder.add("openid", tUser.unionid);
        }
        RequestBody body = builder.build();
        OkHttpUtil.post(HttpUrl.loginUrl, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
//                ToastUtil.show(s);
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    User user = GsonUtil.jsonToObject(result.getResult(),User.class);
                    if (user.user_id != null){
                        String md5 = MD5.md5(user.user_id+CommonCode.APP_KEY);
                        if (md5.equals(user.hash)){
                            PrefUtil.putInt(CommonCode.SP_USER_USERID, user.user_id);
                            PrefUtil.putInt(CommonCode.SP_USER_PHONEISBIND, user.is_bind);
                            PrefUtil.putInt(CommonCode.SP_USER_PAYPSDISBIND, user.pay_is_bind);
                            PrefUtil.putString(CommonCode.SP_USER_LAST_LOGIN_TYPE, type);
                            PrefUtil.putString(CommonCode.SP_USER_IM_TOKEN, user.im_token);
                            PrefUtil.putString(CommonCode.SP_USER_PHONE, user.mobile);
                            PrefUtil.putString(CommonCode.SP_USER_NICKNAME, tUser.nickname);
                            PrefUtil.putBoolean(CommonCode.SP_IS_NEW_USER,user.is_first);
                            if (user.is_bind == 0){
                                view.toBindPhoneActivity();
                                return;
                            } else {
                                view.toMainActivity();
                            }
                        }
                    }
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
}