package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import org.greenrobot.eventbus.EventBus;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventCashBindSuccess;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBindUnPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBindUnView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/25.
 */
public class BindUnPresenter implements IBindUnPresenter {
    private IBindUnView view;
    public BindUnPresenter(IBindUnView view){
        this.view = view;
    }
    @Override
    public void bind(EditText name, EditText account, EditText bankName) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String sName = name.getText().toString();
        final String sAccount = account.getText().toString();
        final String sBank = bankName.getText().toString();
        if (StringUtil.isNull(sName)){
            view.showToast("请输入收款人户名");
            return;
        } else if (StringUtil.isNull(sAccount)){
            view.showToast("请输入收款人储蓄卡号");
            return;
        } else if (StringUtil.isNull(sBank)){
            view.showToast("银行名称不能为空");
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("realname", sName)
                .add("bank_number",sAccount)
                .add("bank_name",sBank)
                .build();
        OkHttpUtil.post(HttpUrl.bindUnCardUrl, body, new Subscriber<String>() {
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
                    if (!StringUtil.isNull(sAccount)&&sAccount.length()>4){
                        PrefUtil.putString(CommonCode.SP_USER_LAST_BANKACCOUNT,sAccount);
                        int l = sAccount.length();
                        String str = sAccount.substring(l-4,l);
                        bind.union = sBank+"("+str+")";
                        EventBus.getDefault().post(bind);
                    }
                }
                view.showToast(result.error);
            }
        });
    }
}