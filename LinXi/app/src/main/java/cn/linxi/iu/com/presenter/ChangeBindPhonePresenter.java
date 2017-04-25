package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IChangeBindPhonePresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IChangeBindPhoneView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public class ChangeBindPhonePresenter implements IChangeBindPhonePresenter {
    private IChangeBindPhoneView view;
    public ChangeBindPhonePresenter(IChangeBindPhoneView view){
        this.view = view;
    }
    @Override
    public void changeBind(EditText code) {
        /**
         * 修改绑定手机验证验证码,下一步
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String phone = PrefUtil.getString(CommonCode.SP_USER_PHONE,"");
        String strCode = code.getText().toString();
        if (StringUtil.isNull(phone)){
            view.showToast("请重新登录");
            return;
        }
        if (StringUtil.isNull(strCode)){
            view.showToast("请输入验证码");
            return;
        }
        if (strCode.length() != 6){
            view.showToast("请输入6位验证码");
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("mobile", phone)
                .add("action", "unbinding")
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
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    view.toChangeNextActivity();
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void getCode() {
        /**
         * 解绑手机获取验证码方法
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String phone = PrefUtil.getString(CommonCode.SP_USER_PHONE,"");
        if (StringUtil.isNull(phone)){
            view.showToast("请重新登录");
            return;
        }
        OkHttpUtil.get(HttpUrl.getCodeUrl + OkHttpUtil.getCodeSign(phone) + "&action=unbinding&mobile=" + phone, new Subscriber<String>() {
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
}