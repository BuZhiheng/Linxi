package cn.linxi.iu.com.presenter;
import android.widget.EditText;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.IBindInvitePresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.view.iview.IBindInviteView;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/25.
 */
public class BindInvitePresenter implements IBindInvitePresenter {
    private IBindInviteView view;
    public BindInvitePresenter(IBindInviteView view){
        this.view = view;
    }
    @Override
    public void bind(EditText phone) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        final String sPhone = phone.getText().toString();
        if (StringUtil.isNull(sPhone)){
            view.showToast("请输入推荐人手机号");
            return;
        } else if(!StringUtil.strEX(sPhone,StringUtil.EX_PHONE)){
            view.showToast("请输入正确的手机号");
            return;
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .add("invite_mobile", sPhone)
                .build();
        OkHttpUtil.post(HttpUrl.bindInviteUrl, body, new Subscriber<String>() {
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
                    PrefUtil.putString(CommonCode.SP_USER_INVITE_MOBILE, sPhone);
                    view.bindSuccess();
                }
                view.showToast(result.error);
            }
        });
    }
}