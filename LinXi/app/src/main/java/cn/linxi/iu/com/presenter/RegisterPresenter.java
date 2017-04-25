package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.ipresenter.IRegisterPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IRegisterView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public class RegisterPresenter implements IRegisterPresenter {
    private IRegisterView view;
    private User user;
    public RegisterPresenter(IRegisterView view){
        this.view = view;
        user = new User();
    }
    @Override
    public void register(EditText username, EditText code) {
        /**
         * 注册验证验证码,下一步
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        user.mobile = username.getText().toString();
        user.code = code.getText().toString();
        if (StringUtil.isNull(user.mobile)){
            view.showToast("请输入手机号");
            return;
        }
        if (StringUtil.isNull(user.code)){
            view.showToast("请输入验证码");
            return;
        }
        if (!StringUtil.strEX(user.mobile, StringUtil.EX_PHONE)){
            view.showToast("请输入正确的手机号");
            return;
        }
        if (user.code.length() != 6){
            view.showToast("请输入6位验证码");
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("mobile",user.mobile)
                .add("action", "register")
                .add("code",user.code)
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
                    view.toNextActivity(user);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void getCode(EditText username) {
        /**
         * 注册获取验证码方法
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String phone = username.getText().toString();
        if (StringUtil.isNull(phone)){
            view.showToast("请输入手机号");
            return;
        }
        if (!StringUtil.strEX(phone, StringUtil.EX_PHONE)){
            view.showToast("请输入正确的手机号");
            return;
        }
        user.mobile = username.getText().toString();
        String url = HttpUrl.getCodeUrl + OkHttpUtil.getCodeSign(phone) + "&action=register&mobile=" + user.mobile;
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