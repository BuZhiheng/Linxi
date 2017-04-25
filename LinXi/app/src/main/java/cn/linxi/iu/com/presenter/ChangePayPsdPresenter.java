package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IChangePayPsdPresenter;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IChangePayPsdView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/18.
 */
public class ChangePayPsdPresenter implements IChangePayPsdPresenter {
    private IChangePayPsdView view;
    public ChangePayPsdPresenter(IChangePayPsdView view){
        this.view = view;
    }
    @Override
    public void change(EditText code,EditText psd,EditText psdConfirm) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        final String phone = PrefUtil.getString(CommonCode.SP_USER_PHONE,"");
        String strCode = code.getText().toString();
        final String strPsd = psd.getText().toString();
        final String strPsdConfirm = psdConfirm.getText().toString();
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
                .add("action", "reset_pay")
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
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    changePayPsdHttpResp(strPsd,strPsdConfirm);
                } else {
                    view.showToast(result.error);
                }
            }
        });
    }
    private void changePayPsdHttpResp(String psd, String psdConfirm) {
        String id = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", id)
                .add("password", psd)
                .add("repassword", psdConfirm)
                .build();
        OkHttpUtil.post(HttpUrl.changePayPsdUrl, body, new Subscriber<String>() {
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
                    PrefUtil.putInt(CommonCode.SP_USER_PAYPSDISBIND,1);
                    view.changeSuccess();
                }
                view.showToast(result.error);
            }
        });
    }
    @Override
    public void getCode() {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String phone = PrefUtil.getString(CommonCode.SP_USER_PHONE,"");
        if (StringUtil.isNull(phone)){
            view.showToast("请重新登录");
            return;
        }
        OkHttpUtil.get(HttpUrl.getCodeUrl + OkHttpUtil.getCodeSign(phone)+"&action=reset_pay&mobile=" + phone, new Subscriber<String>() {
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