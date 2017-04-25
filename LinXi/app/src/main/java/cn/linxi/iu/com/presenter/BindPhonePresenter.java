package cn.linxi.iu.com.presenter;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBindPhonePresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBindPhoneView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public class BindPhonePresenter implements IBindPhonePresenter {
    private IBindPhoneView view;
    public BindPhonePresenter(IBindPhoneView view){
        this.view = view;
    }
    @Override
    public void bind(final EditText phone, EditText code) {
        /**
         * 验证验证码
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        final String strPhone = phone.getText().toString();
        final String strCode = code.getText().toString();
        if (StringUtil.isNull(strPhone)){
            view.showToast("请输入手机号");
            return;
        }
        if (StringUtil.isNull(strCode)){
            view.showToast("请输入验证码");
            return;
        }
        if (!StringUtil.strEX(strPhone, StringUtil.EX_PHONE)){
            view.showToast("请输入正确的手机号");
            return;
        }
        if (strCode.length() != 6){
            view.showToast("请输入6位验证码");
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("mobile", strPhone)
                .add("action", "binding")
                .add("code", strCode)
                .build();
        OkHttpUtil.post(HttpUrl.verifyCodeUrl, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
                Log.i(">>>>>>",s);
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    //绑定
                    bindPhone(strPhone);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    private void bindPhone(final String phone) {
        /**
         * 绑定手机号
         * */
        RequestBody body = new FormBody.Builder()
                .add("mobile", phone)
                .add("user_id", PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"")
                .build();
        OkHttpUtil.post(HttpUrl.bindPhoneUrl, body, new Subscriber<String>() {
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
                    //绑定成功
                    PrefUtil.putString(CommonCode.SP_USER_PHONE, phone);
                    PrefUtil.putInt(CommonCode.SP_USER_PHONEISBIND, 1);
                    PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN, true);
                    view.bindSuccess();
                    view.showToast(result.error);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void getCode(EditText phone) {
        /**
         * 绑定手机号获取验证码方法
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String strPhone = phone.getText().toString();
        if (StringUtil.isNull(strPhone)){
            view.showToast("请输入手机号");
            return;
        }
        if (!StringUtil.strEX(strPhone, StringUtil.EX_PHONE)){
            view.showToast("请输入正确的手机号");
            return;
        }
        OkHttpUtil.get(HttpUrl.getCodeUrl + OkHttpUtil.getCodeSign(strPhone)+ "&action=binding&mobile=" + strPhone, new Subscriber<String>() {
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
    public void showJump(Intent intent) {
        if (intent != null && intent.getStringExtra(CommonCode.INTENT_BIND_PHONE_FROM) != null){
//            view.setJumpBtn();
        }
    }
}