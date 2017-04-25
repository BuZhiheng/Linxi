package cn.linxi.iu.com.presenter;
import android.content.Context;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMTextElem;
import com.tencent.TIMUser;
import com.tencent.TIMUserStatusListener;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.model.TIModelCustomerOrderSure;
import cn.linxi.iu.com.model.User;
import cn.linxi.iu.com.presenter.ipresenter.ITIMPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ITIMView;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/9/7.
 */
    public class TIMPresenter implements ITIMPresenter {
    private ITIMView view;
    public TIMPresenter(ITIMView view){
        this.view = view;
    }
    @Override
    public void timInit(Context context, final int type) {
        TIMManager.getInstance().init(context.getApplicationContext());
        TIMManager.getInstance().disableStorage();
        TIMUser user = new TIMUser();
        user.setAccountType(CommonCode.TIM_ACCOUNT_TYPE);
        user.setAppIdAt3rd(CommonCode.TIM_APPID + "");
        String identify = "";
        if (type == CommonCode.TIM_INIT_TYPE_CLIENT){
            identify = PrefUtil.getInt(CommonCode.SP_USER_USERID, 0)+"";
        } else if (type == CommonCode.TIM_INIT_TYPE_STATION){
            identify = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID, 0)+"";
        }
        user.setIdentifier(identify);
        String sign = PrefUtil.getString(CommonCode.SP_USER_IM_TOKEN,"");
        TIMManager.getInstance().login(CommonCode.TIM_APPID, user, sign, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
//                ToastUtil.show(i+s);
            }
            @Override
            public void onSuccess() {
//                ToastUtil.show("tim login success");
                view.timLoginSuccess();
            }
        });
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(List<TIMMessage> list) {
                TIMMessage msg = list.get(0);
                for (int i = 0; i < msg.getElementCount(); i++) {
                    if (msg.isRead()) {
                        break;
                    }
                    TIMElem elem = msg.getElement(i);
                    //获取当前元素的类型
                    TIMElemType elemType = elem.getType();
                    if (elemType == TIMElemType.Text) {
                        TIMTextElem text = (TIMTextElem) elem;
                        //ToastUtil.show(list.size() + "size>>>>>>>>" + text.getText());
                        String s = text.getText();
                        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, "admin-oil");
                        conversation.setReadMessage(msg);
                        if (!StringUtil.isNull(s) && StringUtil.isJson(s)) {//处理文本消息
                            BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                            if (result != null && result.code != null) {
                                switch (result.code) {
                                    case 8000:
                                        TIModelCustomerOrderSure orderSure = GsonUtil.jsonToObject(result.getResult(), TIModelCustomerOrderSure.class);
                                        EventBus.getDefault().post(orderSure);//关闭之前的弹窗
                                        view.timOrderSure(orderSure);
                                        int voice = PrefUtil.getInt(CommonCode.SP_APP_VOICE,0);
                                        int vib = PrefUtil.getInt(CommonCode.SP_APP_VIB,0);
                                        if (voice == 1){
                                            view.showVoice();
                                        }
                                        if (vib == 1){
                                            view.showVib();
                                        }
                                        break;
                                }
                            }
                        }
                    } else if (elemType == TIMElemType.Image) {
                        //处理图片消息
                    }//...处理更多消息
                }
                return false;
            }
        });
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                /**
                 * 被踢下线时回调
                 */
                view.timOnForceOffline();
            }
            @Override
            public void onUserSigExpired() {
                /**
                 * 票据过期时回调
                 */
                gettimSign(type);
            }
        });
    }
    //TIM票据过期,重新签名登录
    private void gettimSign(int type){
        String userId = "";
        if (type == CommonCode.TIM_INIT_TYPE_CLIENT){
            userId = PrefUtil.getInt(CommonCode.SP_USER_USERID, 0)+"";
        } else if (type == CommonCode.TIM_INIT_TYPE_STATION){
            userId = PrefUtil.getInt(CommonCode.SP_USER_OPERA_ID, 0)+"";
        }
        String url = HttpUrl.getTIMSignUrl+OkHttpUtil.getSign()+"&user_id="+userId;
        OkHttpUtil.get(url, new Subscriber<String>() {
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
                    PrefUtil.putString(CommonCode.SP_USER_IM_TOKEN,user.im_token);
                    view.timOnUserSigExpired();
                }
            }
        });
    }
}