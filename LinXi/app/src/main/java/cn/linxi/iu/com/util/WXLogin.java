package cn.linxi.iu.com.util;
import org.greenrobot.eventbus.EventBus;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.TencentUser;
import cn.linxi.iu.com.model.WXToken;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/1.
 */
public class WXLogin {
    public static void wxLogin(String code){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + CommonCode.APP_ID_WX+
                "&secret=" + CommonCode.APP_SECRET_WX +
                "&code=" + code +
                "&grant_type=authorization_code";
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                ToastUtil.show(e.getMessage()+"err");
            }
            @Override
            public void onNext(String s) {
//                ToastUtil.show(s+"first");
                if (s != null){
                    WXToken token = GsonUtil.jsonToObject(s, WXToken.class);
                    if (token != null){
                        getUser(token);
                    }
                }
            }
        });
    }
    private static void getUser(WXToken token){
        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" +token.access_token+
                "&openid=" + token.openid;
        OkHttpUtil.get(url, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                ToastUtil.show(e.getMessage()+"err");
            }
            @Override
            public void onNext(String s) {
//                ToastUtil.show(s+"next");
                if (s != null){
                    TencentUser user = GsonUtil.jsonToObject(s,TencentUser.class);
                    if (user != null){
                        EventBus.getDefault().post(user);
                    }
                }
            }
        });
    }
}