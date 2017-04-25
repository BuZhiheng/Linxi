package cn.linxi.iu.com.presenter;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import cn.linxi.iu.com.model.BaseResult;
import cn.linxi.iu.com.model.CommonCode;
import cn.linxi.iu.com.model.EventUserMsgChanged;
import cn.linxi.iu.com.model.HttpUrl;
import cn.linxi.iu.com.presenter.ipresenter.ICashPresenter;
import cn.linxi.iu.com.util.GsonUtil;
import cn.linxi.iu.com.util.OkHttpUtil;
import cn.linxi.iu.com.util.PrefUtil;
import cn.linxi.iu.com.util.StringUtil;
import cn.linxi.iu.com.util.SystemUtils;
import cn.linxi.iu.com.util.ToastUtil;
import cn.linxi.iu.com.view.iview.ICashView;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
/**
 * Created by buzhiheng on 2016/8/22.
 */
public class CashPresenter implements ICashPresenter {
    private ICashView view;
    public CashPresenter(ICashView view){
        this.view = view;
    }
    @Override
    public void cash(String channel,EditText etCout) {
        if (SystemUtils.networkState() == false){
            view.showToast(CommonCode.NOTICE_NETWORK_DISCONNECT);
            return;
        }
        String cout = etCout.getText().toString();
        if (StringUtil.isNull(cout)){
            view.showToast("请输入提现金额");
            return;
        }
        if (cout.startsWith(".")||cout.endsWith(".")){
            view.showToast("提现金额有误");
            return;
        }
        Float fCout = Float.valueOf(cout);
        String balance = PrefUtil.getString(CommonCode.SP_USER_BALANCE, "");
        if (fCout > Float.valueOf(balance)){
            view.showToast("提现金额大于账户余额");
            return;
        }
        if (fCout < 100){
            view.showToast("单笔提现金额不能小于100元");
            return;
        }
        if (fCout > 10000){
            view.showToast("单笔提现金额不能超过10000元");
            return;
        }
        String cardAli = PrefUtil.getString(CommonCode.SP_USER_LAST_ALIACCOUNT,"");
        String cardBank = PrefUtil.getString(CommonCode.SP_USER_LAST_BANKACCOUNT,"");
        if (CommonCode.HTTP_CASH_CARDALI.equals(channel)){
            if (StringUtil.isNull(cardAli)){
                view.showToast("未绑定支付宝提现账户");
                view.toBindAli();
                return;
            }
        }
        if (CommonCode.HTTP_CASH_CARDBANK.equals(channel)){
            if (StringUtil.isNull(cardBank)){
                view.showToast("未绑定银行卡提现账户");
                view.toBindBank();
                return;
            }
        }
        String userId = PrefUtil.getInt(CommonCode.SP_USER_USERID,0)+"";
        RequestBody body = new FormBody.Builder()
                .add("user_id",userId)
                .add("amount",cout)
                .add("pay_channel",channel)
                .build();
//        RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"),new File(""));
//        ToastUtil.show(userId+">>"+cout+">>"+channel);
        OkHttpUtil.post(HttpUrl.cash, body, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String s) {
//                ToastUtil.show(s);
                BaseResult result = GsonUtil.jsonToObject(s, BaseResult.class);
                if (result.success()) {
                    view.cashSuccess();
                    EventBus.getDefault().post(new EventUserMsgChanged());
                }
                view.showToast(result.error);
            }
        });
    }
}