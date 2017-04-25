package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import org.greenrobot.eventbus.EventBus;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventCashBindSuccess;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBindAliPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBindAliView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/25.
 */
public class BindAliPresenter implements IBindAliPresenter {
    private IBindAliView view;
    public BindAliPresenter(IBindAliView view){
        this.view = view;
    }
    @Override
    public void bind(EditText name, EditText account) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        final String sName = name.getText().toString();
        final String sAccount = account.getText().toString();
        if (StringUtil.isNull(sName)){
            view.showToast("请输入收款人真实姓名");
            return;
        } else if (StringUtil.isNull(sAccount)){
            view.showToast("请输入收款人支付宝账号");
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("realname", sName)
                .add("account",sAccount)
                .build();
        OkHttpUtil.post(HttpUrl.bindAliUrl, body, new Subscriber<String>() {
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
                    view.bindSuccess();
                    EventCashBindSuccess bind = new EventCashBindSuccess();
                    if (!StringUtil.isNull(sAccount)&&sAccount.length()>3){
                        PrefUtil.putString(CommonCode.SP_USER_LAST_ALIACCOUNT,sAccount);
                        String str = sAccount.substring(0,3);
                        bind.ali = sName+"("+str+"...)";
                        EventBus.getDefault().post(bind);
                    }
                }
                view.showToast(result.error);
            }
        });
    }
}