package cn.linxi.iu.com.presenter;
import android.app.Dialog;
import android.widget.EditText;
import org.xutils.common.util.MD5;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessLoginPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessLoginView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public class BusinessLoginPresenter implements IBusinessLoginPresenter {
    private IBusinessLoginView view;
    public BusinessLoginPresenter(IBusinessLoginView view){
        this.view = view;
    }
    @Override
    public void login(EditText username, EditText psd, Dialog dialog) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        final String sUsername = username.getText().toString();
        final String sPsd = psd.getText().toString();
        if (StringUtil.isNull(sUsername)){
            view.showToast("请输入用户名");
            return;
        } else if (StringUtil.isNull(sPsd)){
            view.showToast("请输入密码");
            return;
        }
        setTimeOut(dialog);
        RequestBody body = new FormBody.Builder()
                .add("account", sUsername)
                .add("password",sPsd)
                .build();
        OkHttpUtil.post(HttpUrl.businessLogin, body, new Subscriber<String>() {
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
                    if (user.operat_id != null){
                        PrefUtil.putInt(CommonCode.SP_USER_OPERA_ID, user.operat_id);
                        PrefUtil.putString(CommonCode.SP_USER_STATION_ID, user.station_id);
                        PrefUtil.putString(CommonCode.SP_USER_IM_TOKEN,user.im_token);
                        PrefUtil.putString(CommonCode.SP_IS_BUSINESS_USERNAME, sUsername);
                        PrefUtil.putString(CommonCode.SP_IS_BUSINESS_PSD, sPsd);
                        String md5 = MD5.md5(user.operat_id + CommonCode.APP_KEY);
                        if (md5.equals(user.hash)){
                            if (user.user_type != null && user.user_type == 2){
                                PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN_BOSS,true);
                                view.toBossActivity();
                                return;
                            }
                            boolean isInit = PrefUtil.getBoolean(CommonCode.SP_IS_BUSINESS_PSDISINIT,false);
                            if (isInit){
                                PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN_BUSINESS,true);
                                view.toStationActivity();
                            } else {
                                view.toInitPsdActivity();
                            }
                        } else {
                            view.showToast("登录失败");
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
    public void setTimeOut(final Dialog dialog) {
        OkHttpUtil.executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(CommonCode.HTTP_TIMEOUT);
                            if (dialog.isShowing()) {
                                view.timeOut();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}