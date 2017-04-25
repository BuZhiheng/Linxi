package cn.linxi.iu.com.util;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONException;
import org.json.JSONObject;

import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.TencentUser;
import cn.linxi.iu.com.presenter.LoginPresenter;
import cn.linxi.iu.com.view.activity.LoginActivity;
/**
 * Created by buzhiheng on 2016/7/27.
 */
public class QQLoginListener implements IUiListener {
    private Tencent tencent;
    private LoginActivity context;
    private LoginPresenter presenter;
    private String openid;
    public QQLoginListener(Tencent tencent, LoginActivity context){
        this.tencent = tencent;
        this.context = context;
        presenter = new LoginPresenter(context);
    }
    public void onCancel() {
        ToastUtil.show("已取消");
    }
    public void onComplete(Object response) {
        try {
            openid= ((JSONObject) response).getString("openid");
            String expires= ((JSONObject) response).getString("expires_in");
            String access_token= ((JSONObject) response).getString("access_token");
            tencent.setOpenId(openid);
            tencent.setAccessToken(access_token, expires);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UserInfo info = new UserInfo(context, tencent.getQQToken());
        //这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON
        info.getUserInfo(new UserUiListener());
    }
    public void onError(UiError err) {
        ToastUtil.show(err.errorMessage);
    }
    private class UserUiListener implements IUiListener {
        public void onCancel() {
        }
        public void onComplete(Object response) {
            TencentUser login = GsonUtil.jsonToObject(response.toString(),TencentUser.class);
            login.openid = openid;
            login.headimgurl = login.figureurl_qq_2;
            //QQ登录
            presenter.loginByService(login, CommonCode.LOGIN_BY_QQ);
        }
        public void onError(UiError arg0) {
        }
    }
}