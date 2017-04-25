package cn.linxi.iu.com.presenter;
import android.app.Dialog;
import android.widget.EditText;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBusinessInitPsdPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBusinessInitPsdView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/4.
 */
public class BusinessInitPsdPresenter implements IBusinessInitPsdPresenter {
    private IBusinessInitPsdView view;
    public BusinessInitPsdPresenter(IBusinessInitPsdView view){
        this.view = view;
    }
    @Override
    public void init(EditText psd, EditText psdConfirm,Dialog dialog) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        final String sPsd = psd.getText().toString();
        if (StringUtil.isNull(sPsd)){
            view.showToast("请输入新密码");
            return;
        }
        if (!StringUtil.strEX(sPsd, StringUtil.EX_NUMBER_ABC)){
            view.showToast("密码只能为数字字母组合");
            return;
        }
        if (sPsd.length() < 8){
            view.showToast("密码长度最低8位");
            return;
        }
        String sPsdConfirm = psdConfirm.getText().toString();
        if (StringUtil.isNull(sPsdConfirm)){
            view.showToast("请再次输入新密码");
            return;
        }
        if (!sPsd.equals(sPsdConfirm)){
            view.showToast("两次密码输入不一致");
            return;
        }
        setTimeOut(dialog);
        String operaId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("operat_id", operaId)
                .add("password", sPsd)
                .add("repassword", sPsdConfirm)
                .build();
        OkHttpUtil.post(HttpUrl.businessInitPsd, body, new Subscriber<String>() {
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
                    view.initSuccess();
                    PrefUtil.putBoolean(CommonCode.SP_IS_LOGIN_BUSINESS, true);
                    PrefUtil.putBoolean(CommonCode.SP_IS_BUSINESS_PSDISINIT,true);
                    PrefUtil.putString(CommonCode.SP_IS_BUSINESS_PSD, sPsd);
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