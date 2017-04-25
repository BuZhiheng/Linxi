package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import org.greenrobot.eventbus.EventBus;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.EventUserMsgChanged;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IChangeBindPhoneNextPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IChangeBindPhoneNextView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/19.
 * Desc 修改绑定手机下一步
 */
public class ChangeBindPhoneNextPresenter implements IChangeBindPhoneNextPresenter {
    private IChangeBindPhoneNextView view;
    public ChangeBindPhoneNextPresenter(IChangeBindPhoneNextView view){
        this.view = view;
    }
    @Override
    public void bind(EditText phone,EditText code) {
        /**
         * 修改绑定手机验证验证码,下一步
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        final String strPhone = phone.getText().toString();
        String strCode = code.getText().toString();
        if (StringUtil.isNull(strPhone)){
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
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                if (result.success()){
                    changeBindHttpResp(strPhone);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    @Override
    public void getCode(EditText etPhone) {
        /**
         * 解绑手机获取验证码方法
         *
         * */
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String strPhone = etPhone.getText().toString();
        if (StringUtil.isNull(strPhone)){
            view.showToast("请输入手机号");
            return;
        }
        if (!StringUtil.strEX(strPhone, StringUtil.EX_PHONE)){
            view.showToast("请输入正确的手机号");
            return;
        }
        OkHttpUtil.get(HttpUrl.getCodeUrl + OkHttpUtil.getCodeSign(strPhone)+"&action=binding&mobile=" + strPhone, new Subscriber<String>() {
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
    private void changeBindHttpResp(final String phone){
        String id = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        if (StringUtil.isNull(id)){
            view.showToast("用户ID不存在");
            return;
        }
        /**
         *
         * 绑定手机号
         *
         * */
        RequestBody body = new FormBody.Builder()
                .add("mobile", phone)
                .add("user_id", id)
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
                BaseResult result = GsonUtil.jsonToObject(s,BaseResult.class);
                view.showToast(result.error);
                if (result.success()){
                    PrefUtil.putString(CommonCode.SP_USER_PHONE,phone);
                    view.changeSuccess();
                    EventBus.getDefault().post(new EventUserMsgChanged());
                }
            }
        });
    }
}